/*
 * File: JSELValue.java
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
import mardlucca.jsel.JSELRuntimeException;

import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.typeError;

public abstract class JSELValue
{
    @Override
    public boolean equals(Object aInValue)
    {
        if (this.getValue() == aInValue)
        {
            return true;
        }

        return aInValue instanceof JSELValue
                && equals((JSELValue) aInValue);
    }

    public abstract boolean equals(JSELValue aInValue);

    public abstract Type getType();

    public boolean isPrimitive()
    {
        return true;
    }

    public boolean isCallable()
    {
        return false;
    }

    public boolean isObjectCoercible()
    {
        return true;
    }

    public boolean isObjectClass(String aInString)
    {
        return false;
    }

    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext)
    {
        throw JSELRuntimeException.typeError("cannot invoke object of type "
                + getType().name().toLowerCase());
    }

    public JSELObject instantiate(List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext)
    {
        throw JSELRuntimeException.typeError(this + " is not a constructor");
    }

    public MatchResult match(String aInString, int aInIndex)
    {
        // this is slightly different than what the spec says, the spec says
        // only RegExp's implement this method.
        return new JSELString(toString()).match(aInString, aInIndex);
    }

    /**
     * Returns whether or not this value is a reference type.
     *
     * @return true if this a reference type (e.g. {@link JSELPropertyReference}
     */
    public boolean isReference()
    {
        return false;
    }

    /**
     * On a {@link #isReference() reference} this returns the referenced value.
     * On a regular value this returns the instance itself.
     * @return
     */
    public JSELValue getValue()
    {
        return this;
    }

    public boolean strictEquals(JSELValue aInValue)
    {
        return aInValue.getValue() == this;
    }

    public abstract boolean toBoolean();

    public int toInt32()
    {
        return new JSELNumber(toNumber()).toInt32();
    }

    public int toInteger()
    {
        return new JSELNumber(toNumber()).toInteger();
    }

    public double toNumber()
    {
        return toPrimitive(GetHint.NUMBER).toNumber();
    }

    public JSELValue toPrimitive(GetHint aInHint)
    {
        return this;
    }

    public abstract JSELObject toObject();

    public long toUInt32()
    {
        return new JSELNumber(toNumber()).toUInt32();
    }

    public enum GetHint
    {
        NUMBER, STRING
    }

    public static class MatchResult
    {
        private int start;
        private int end;
        private String[] captures;

        public MatchResult(int aInStart, int aInEnd, String[] aInCaptures)
        {
            start = aInStart;
            end = aInEnd;
            captures = aInCaptures;
        }

        public int getStart()
        {
            return start;
        }

        public int getEnd()
        {
            return end;
        }

        public String[] getCaptures()
        {
            return captures;
        }
    }
}
