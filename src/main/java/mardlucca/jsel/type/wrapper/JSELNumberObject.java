/*
 * File: JSELNumberObject.java
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
package mardlucca.jsel.type.wrapper;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;

public class JSELNumberObject extends JSELPrimitiveWrapper
{
    public static final String CLASS = "Number";

    public JSELNumberObject(JSELValue aInPrimitive)
    {
        this(ExecutionContext.getNumberPrototype(), aInPrimitive);
    }

    protected JSELNumberObject(JSELObject aInPrototype, JSELValue aInPrimitive)
    {
        super((aInPrimitive != null && aInPrimitive.getType() == Type.NUMBER)
                        ? aInPrimitive
                        :  new JSELNumber(aInPrimitive == null
                        ? 0.0
                        : aInPrimitive.toNumber()),
                aInPrototype);
    }

    @Override
    public String getObjectClass()
    {
        return CLASS;
    }
}
