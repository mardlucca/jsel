/*
 * File: JSELBoolean.java
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

import mardlucca.jsel.type.wrapper.JSELBooleanObject;

public class JSELBoolean extends JSELValue
{
    public static final JSELBoolean TRUE = new JSELBoolean(true);

    public static final JSELBoolean FALSE = new JSELBoolean(false);

    private boolean bool;

    public JSELBoolean(boolean aInBoolean)
    {
        bool = aInBoolean;
    }

    @Override
    public Type getType()
    {
        return Type.BOOLEAN;
    }

    @Override
    public boolean isPrimitive()
    {
        return true;
    }

    @Override
    public boolean toBoolean()
    {
        return bool;
    }

    @Override
    public double toNumber()
    {
        return bool ? 1.0 : 0.0;
    }

    @Override
    public JSELObject toObject()
    {
        return new JSELBooleanObject(this);
    }

    @Override
    public String toString()
    {
        return String.valueOf(bool);
    }

    @Override
    public boolean equals(JSELValue aInValue)
    {
        aInValue = aInValue.getValue();
        if (aInValue == this)
        {
            return true;
        }

        if (aInValue.getType() == Type.BOOLEAN)
        {
            return bool == aInValue.toBoolean();
        }
        return new JSELNumber(this.toNumber()).equals(aInValue);
    }

    @Override
    public boolean strictEquals(JSELValue aInValue)
    {
        aInValue = aInValue.getValue();
        if (aInValue == this)
        {
            return true;
        }

        if (aInValue.getType() == Type.BOOLEAN)
        {
            return bool == aInValue.toBoolean();
        }

        return false;
    }
}
