/*
 * File: JSELUndefined.java
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


import mardlucca.jsel.JSELRuntimeException;

import static mardlucca.jsel.JSELRuntimeException.typeError;

public class JSELUndefined extends JSELValue
{
    private static final JSELUndefined instance = new JSELUndefined();

    private JSELUndefined()
    {
    }

    public static JSELUndefined getInstance()
    {
        return instance;
    }

    public Type getType()
    {
        return Type.UNDEFINED;
    }

    @Override
    public boolean isObjectCoercible()
    {
        return false;
    }

    @Override
    public boolean isPrimitive()
    {
        return true;
    }

    @Override
    public boolean toBoolean()
    {
        return false;
    }

    @Override
    public double toNumber()
    {
        return Double.NaN;
    }

    @Override
    public JSELObject toObject()
    {
        throw JSELRuntimeException.typeError("undefined cannot be converted to object");
    }

    @Override
    public String toString()
    {
        return "undefined";
    }

    @Override
    public boolean equals(JSELValue aInObject)
    {
        aInObject = aInObject.getValue();
        return aInObject == this || aInObject == JSELNull.getInstance();
    }
}
