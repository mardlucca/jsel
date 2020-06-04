/*
 * File: JSELPropertyReference.java
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

import java.util.List;

public class JSELPropertyReference extends JSELValue
{
    private JSELValue base;
    private String propertyName;
    private JSELValue value;

    public JSELPropertyReference(
            JSELValue aInBase, String aInPropertyName)
    {
        base = aInBase.getValue();  // we don't want base to be a reference
        propertyName = aInPropertyName;
        value = base.toObject().get(propertyName);
    }

    @Override
    public boolean equals(Object aInValue)
    {
        return value.equals(aInValue);
    }

    @Override
    public boolean equals(JSELValue aInValue)
    {
        return value.equals(aInValue);
    }

    @Override
    public Type getType()
    {
        return value.getType();
    }

    @Override
    public boolean isPrimitive()
    {
        return value.isPrimitive();
    }

    @Override
    public boolean isCallable()
    {
        return value.isCallable();
    }

    @Override
    public boolean strictEquals(JSELValue aInValue)
    {
        return value.strictEquals(aInValue);
    }

    @Override
    public boolean toBoolean()
    {
        return value.toBoolean();
    }

    @Override
    public int toInt32()
    {
        return value.toInt32();
    }

    @Override
    public double toNumber()
    {
        return value.toNumber();
    }

    @Override
    public JSELValue toPrimitive(GetHint aInHint)
    {
        return value.toPrimitive(aInHint);
    }

    @Override
    public JSELObject toObject()
    {
        return value.toObject();
    }

    @Override
    public String toString()
    {
        return value.toString();
    }

    @Override
    public long toUInt32()
    {
        return value.toUInt32();
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext)
    {
        return value.call(
                aInThis.getType() == Type.NULL
                        || aInThis.getType() == Type.UNDEFINED
                        ? base.toObject()
                        : aInThis,
                aInArguments,
                aInExecutionContext);
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext)
    {
        return value.instantiate(aInArguments, aInExecutionContext);
    }

    @Override
    public boolean isObjectClass(String aInString)
    {
        return value.isObjectClass(aInString);
    }

    @Override
    public MatchResult match(String aInString, int aInIndex)
    {
        return value.match(aInString, aInIndex);
    }

    @Override
    public int toInteger()
    {
        return value.toInteger();
    }

    @Override
    public boolean isReference()
    {
        return true;
    }

    @Override
    public JSELValue getValue()
    {
        return value;
    }
}
