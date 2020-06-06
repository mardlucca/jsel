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

import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.rangeError;

/**
 * This represents an array object type in JSEL.
 */
public class JSELArray extends JSELObject {
    /**
     * Constant used for the internal [[Class]] property for objects of this
     * type.
     */
    public static final String CLASS = "Array";

    /**
     * Constant for the name of property "length"
     */
    public static final String LENGTH = "length";

    /**
     * Creates a new JSELArray with length 0.
     */
    public JSELArray() {
        this(0);
    }

    /**
     * Creates a new JSELArray with a given length.
     * @param aInLength the length of the array.
     */
    public JSELArray(int aInLength) {
        this(ExecutionContext.getArrayPrototype(), aInLength);
    }

    /**
     * Creates a new JSELArray, initializing the array with the given values.
     * @param aInValues the values to initialize the array with.
     */
    public JSELArray(List<JSELValue> aInValues) {
        this(aInValues.size());

        for (int i = 0; i < aInValues.size(); i++) {
            put(i, aInValues.get(i));
        }
    }

    /**
     * Creates a new JSELArray with a given length, using a given prototype
     * Object.
     * @param aInLength the length of the array.
     * @param aInPrototype
     */
    protected JSELArray(JSELObject aInPrototype, long aInLength) {
        super(aInPrototype);
        super.defineOwnProperty(
                LENGTH, new JSELNumber(aInLength), false, true, false, false);
    }

    @Override
    public String getObjectClass() {
        return CLASS;
    }

    /**
     * Gets a value at a specified index in the array object.
     * @param aInIndex the index
     * @return the value read or JSELUndefined.getInstance() if undefined.
     */
    public JSELValue get(long aInIndex) {
        return get(String.valueOf(aInIndex));
    }

    /**
     * Puts a value into the specified index, potentially growing the array.
     * @param aInIndex the index to add to
     * @param aInJSELValue the value to add.
     */
    public void put(long aInIndex, JSELValue aInJSELValue) {
        put(String.valueOf(aInIndex), aInJSELValue);
    }

    /**
     * Defines a new property in the array, as specified in the spec.
     * @param aInProperty the property to add
     * @param aInValue the value
     * @param aInEnumerable whether or not the property is enumerable. If null
     *                      then the property is not updated or will default to
     *                      false on creation.
     * @param aInWritable whether or not the property is writable. If null then
     *                    the property is not updated or will default to
     *      *                      false on creation.
     * @param aInConfigurable whether or not the property is configurable. If
     *                        null then the property is not updated or will
     *                        default to false on creation.
     * @param aInThrow if true this will cause a TypeError to be raised when the
     *                 property cannot be added. If this is false, this method
     *                 will return false in this case.
     * @return true if the property can be defined and false if it cannot be
     * defined and aInThrow is false
     * @throws mardlucca.jsel.JSELRuntimeException a TypeError if the property
     * cannot be defined and aInThrow is true.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-15.4.5.1">
     * ECMA-262, 5.1, Section 15.4.5.1"</a>
     */
    @Override
    public boolean defineOwnProperty(String aInProperty, JSELValue aInValue,
            Boolean aInEnumerable, Boolean aInWritable, Boolean aInConfigurable,
            boolean aInThrow) {
        if (LENGTH.equals(aInProperty)) {
            if (aInValue == null) {
                return super.defineOwnProperty(aInProperty, null,
                        aInEnumerable, aInWritable, aInConfigurable, aInThrow);
            }

            PropertyDescriptor lLengthDescriptor = getOwnProperty(LENGTH);
            int lLength = lLengthDescriptor.getValue().toInteger();

            // they're trying to change "length" manually, which is ok

            int lNewLen = (int) aInValue.toUInt32();
            if (lNewLen != aInValue.toNumber()) {
                // the provided a floating point, NaN, word, etc, as a new value
                throw rangeError("Invalid array length");
            }

            if (lNewLen >= lLength) {
                // this will also take care of any configuration changes, etc
                return super.defineOwnProperty(
                        aInProperty, aInValue, aInEnumerable,
                        aInWritable, aInConfigurable, aInThrow);
            }
            if (!lLengthDescriptor.isWritable()) {
                return reject(aInThrow, "Cannot redefine property length");
            }

            // ok, first we try to save the new length. At this point we save it
            // as writable as we may need to rewrite this if deletion, which
            // follows, fails
            boolean lSucceeded = super.defineOwnProperty(
                    aInProperty, aInValue, aInEnumerable, true,
                    aInConfigurable, aInThrow);
            if (!lSucceeded) {
                // if aInThrow was true the call above would have thrown
                return false;
            }

            for (int i = lLength - 1; i >= lNewLen; i--) {
                lSucceeded = delete(String.valueOf(i), false);
                if (!lSucceeded) {
                    // failed to delete element. will have to stop, set the
                    // length where we stopped and reject
                    super.defineOwnProperty(LENGTH, new JSELNumber(i + 1),
                            aInEnumerable, aInWritable, aInConfigurable, false);
                    return reject(aInThrow, "Cannot redefine property length");
                }
            }

            // succeeded, so we finally set the writable field to false, if
            // required
            if (aInWritable != null && !aInWritable) {
                // this will always return true as we know at this point that
                // the property is writable (as we forced it earlier to true)
                super.defineOwnProperty(
                        LENGTH, aInValue, aInEnumerable, false,
                        aInConfigurable, aInThrow);
            }

            return true;
        }

        // else, are we setting an array index?
        int lIndex = (int) JSELNumber.toUInt32(
                DecimalFormat.parse(aInProperty));
        if (aInProperty.equals(String.valueOf(lIndex))) {
            PropertyDescriptor lLengthDescriptor = getOwnProperty(LENGTH);
            int lLength = lLengthDescriptor.getValue().toInteger();

            // property is an array index.
            if (lIndex >= lLength && !lLengthDescriptor.isWritable()) {
                // length has been marked as not writable.
                return reject(aInThrow, "Cannot redefine property length");
            }

            // let's write the property first
            boolean lSucceeded = super.defineOwnProperty(
                    aInProperty, aInValue, aInEnumerable, aInWritable,
                    aInConfigurable, false);
            if (!lSucceeded) {
                return reject(aInThrow, "Cannot redefine property "
                        + aInProperty);
            }

            // and then adjust length, if required
            if (lIndex >= lLength) {
                // this should never fail as we're only changing the value and
                // we know length is writable (as we tested this above)
                lLengthDescriptor.setValue(new JSELNumber(lIndex + 1));
            }

            return true;
        }

        // else we're setting a regular property
        return super.defineOwnProperty(aInProperty, aInValue, aInEnumerable,
                aInWritable, aInConfigurable, aInThrow);
    }
}
