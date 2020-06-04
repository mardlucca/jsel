/*
 * File: JSELArray.java
 *
 * Copyright 2020 Marcio D. Lucca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mardlucca.jsel.type;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.util.DecimalFormat;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.util.DecimalFormat;

import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.rangeError;

public class JSELArray extends JSELObject
{
    public static final String CLASS = "Array";

    public static final String LENGTH = "length";

    public JSELArray()
    {
        this(0);
    }

    public JSELArray(int aInLength)
    {
        this(ExecutionContext.getArrayPrototype(), aInLength);
    }

    public JSELArray(List<JSELValue> aInValues)
    {
        this(aInValues.size());

        for (int i = 0; i < aInValues.size(); i++)
        {
            put(i, aInValues.get(i));
        }
    }

    protected JSELArray(JSELObject aInPrototype, long aInLength)
    {
        super(aInPrototype);
        super.defineOwnProperty(
                LENGTH, new JSELNumber(aInLength), false, true, false, false);
    }

    @Override
    public String getObjectClass()
    {
        return CLASS;
    }

    public JSELValue get(long aInIndex)
    {
        return get(String.valueOf(aInIndex));
    }

    public void put(long aInIndex, JSELValue aInJSELValue)
    {
        put(String.valueOf(aInIndex), aInJSELValue);
    }

    @Override
    public boolean defineOwnProperty(String aInProperty, JSELValue aInValue,
            Boolean aInEnumerable, Boolean aInWritable, Boolean aInConfigurable,
            boolean aInThrow)
    {

        if (LENGTH.equals(aInProperty))
        {
            if (aInValue == null)
            {
                return super.defineOwnProperty(aInProperty, aInValue,
                        aInEnumerable, aInWritable, aInConfigurable, aInThrow);
            }

            PropertyDescriptor lLengthDescriptor = getOwnProperty(LENGTH);
            int lLength = lLengthDescriptor.getValue().toInteger();

            // they're trying to change "length" manually, which is ok

            int lNewLen = (int) aInValue.toUInt32();
            if (lNewLen != aInValue.toNumber())
            {
                // the provided a floating point, NaN, word, etc, as a new value
                throw JSELRuntimeException.rangeError("Invalid array length");
            }

            if (lNewLen >= lLength)
            {
                // this will also take care of any configuration changes, etc
                return super.defineOwnProperty(
                        aInProperty, aInValue, aInEnumerable,
                        aInWritable, aInConfigurable, aInThrow);
            }
            if (!lLengthDescriptor.isWritable())
            {
                return reject(aInThrow, "Cannot redefine property length");
            }

            // ok, first we try to save the new length. At this point we save it
            // as writable as we may need to rewrite this if deletion, which
            // follows, fails
            boolean lSucceeded = super.defineOwnProperty(
                    aInProperty, aInValue, aInEnumerable, true,
                    aInConfigurable, aInThrow);
            if (!lSucceeded)
            {
                // if aInThrow was true the call above would have thrown
                return false;
            }

            for (int i = lLength - 1; i >= lNewLen; i--)
            {
                lSucceeded = delete(String.valueOf(i), false);
                if (!lSucceeded)
                {
                    // failed to delete element. will have to stop, set the
                    // length where we stopped and reject
                    super.defineOwnProperty(LENGTH, new JSELNumber(i + 1),
                            aInEnumerable, aInWritable, aInConfigurable, false);
                    return reject(aInThrow, "Cannot redefine property length");
                }
            }

            // succeeded, so we finally set the writable field to false, if
            // required
            if (aInWritable == null || !aInWritable)
            {
                // this will always return true as we know at this point that
                // the property is writable (as we forced it earlier to true)
                super.defineOwnProperty(
                        LENGTH, aInValue, aInEnumerable, false,
                        aInConfigurable, aInThrow);
            }

            return true;
        }

        int lIndex = (int) JSELNumber.toUInt32(
                DecimalFormat.parse(aInProperty));
        if (aInProperty.equals(String.valueOf(lIndex)))
        {
            PropertyDescriptor lLengthDescriptor = getOwnProperty(LENGTH);
            int lLength = lLengthDescriptor.getValue().toInteger();

            // property is an array index.
            if (lIndex >= lLength && !lLengthDescriptor.isWritable())
            {
                // length has been marked as not writable.
                return reject(aInThrow, "Cannot redefine property length");
            }

            // let's write the property first
            boolean lSucceeded = super.defineOwnProperty(
                    aInProperty, aInValue, aInEnumerable, aInWritable,
                    aInConfigurable, false);
            if (!lSucceeded)
            {
                return reject(aInThrow, "Cannot redefine property "
                        + aInProperty);
            }

            // and then adjust length, if required
            if (lIndex >= lLength)
            {
                // this should never fail as we're only changing the value and
                // we know length is writable
                lLengthDescriptor.setValue(new JSELNumber(lIndex + 1));
            }

            return true;
        }

        return super.defineOwnProperty(aInProperty, aInValue, aInEnumerable,
                aInWritable, aInConfigurable, aInThrow);
    }
}
