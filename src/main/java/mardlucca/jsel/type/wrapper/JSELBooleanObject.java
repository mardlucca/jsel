/*
 * File: JSELBooleanObject.java
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
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;

public class JSELBooleanObject extends JSELPrimitiveWrapper {
    public static final String CLASS = "Boolean";

    public JSELBooleanObject(JSELValue aInPrimitive) {
        this(ExecutionContext.getBooleanPrototype(), aInPrimitive);
    }

    protected JSELBooleanObject(JSELObject aInPrototype, JSELValue aInPrimitive) {
        super(aInPrimitive != null && aInPrimitive.getType() == Type.BOOLEAN
                        ? aInPrimitive
                        :  new JSELBoolean(
                        aInPrimitive != null && aInPrimitive.toBoolean()),
                aInPrototype);
    }

    public String getObjectClass() {
        return CLASS;
    }

}
