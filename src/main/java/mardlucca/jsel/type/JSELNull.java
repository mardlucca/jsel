/*
 * File: JSELNull.java
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

public class JSELNull extends JSELValue
{
    private static final JSELNull instance = new JSELNull();

    private JSELNull()
    {
    }

    public static JSELNull getInstance()
    {
        return instance;
    }

    @Override
    public boolean isObjectCoercible()
    {
        return false;
    }

    @Override
    public Type getType()
    {
        return Type.NULL;
    }

    @Override
    public boolean toBoolean()
    {
        return false;
    }

    @Override
    public double toNumber()
    {
        return 0.0;
    }

    @Override
    public JSELObject toObject()
    {
        // a Javascript nuance, as typeof null == 'object'
        throw JSELRuntimeException.typeError("null cannot be converted to object");
    }

    @Override
    public String toString()
    {
        return "null";
    }

    @Override
    public boolean equals(JSELValue aInObject)
    {
        aInObject = aInObject.getValue();
        return aInObject == this || aInObject == JSELUndefined.getInstance();
    }
}
