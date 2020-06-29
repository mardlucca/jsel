/*
 * File: SealFunction.java
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
package mardlucca.jsel.builtin.object;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;

import java.util.Collections;
import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.typeError;

public class SealFunction extends JSELFunction {
    public static final String NAME = "seal";

    public SealFunction() {
        super(NAME, Collections.singletonList("object"));
    }

    @Override
    public JSELObject call(JSELValue aInThis, List<JSELValue> aInArguments,
                           ExecutionContext aInContext) {
        JSELValue lArgument = getArgument(aInArguments);
        if (lArgument.getType() != Type.OBJECT) {
            throw typeError("Argument is not an object");
        }

        JSELObject lObject = lArgument.toObject();

        for (String lName : lObject.getOwnPropertyNames()) {
            PropertyDescriptor lDescriptor = lObject.getOwnProperty(lName);
            if (lDescriptor.isConfigurable()) {
                lObject.defineOwnProperty(lName,
                        lDescriptor.getValue(), lDescriptor.isEnumerable(),
                        lDescriptor.isWritable(), false, true);
            }
        }

        lObject.setExtensible(false);
        return lObject;
    }
}
