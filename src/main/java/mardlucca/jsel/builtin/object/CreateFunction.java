/*
 * File: CreateFunction.java
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

import static java.util.Arrays.asList;
import static mardlucca.jsel.JSELRuntimeException.typeError;

public class CreateFunction extends JSELFunction {
    public static final String NAME = "create";
    private static final DefinePropertiesFunction definePropertiesFunction =
            new DefinePropertiesFunction();

    public CreateFunction() {
        super(NAME, asList("object", "properties"));
    }

    @Override
    public JSELObject call(JSELValue aInThis, List<JSELValue> aInArguments,
                           ExecutionContext aInContext) {
            JSELValue lArgument = getArgument(aInArguments);
            if (lArgument.getType() != Type.OBJECT) {
                throw typeError("Argument is not an object");
            }

            JSELObject lObject = new JSELObject(lArgument.toObject());
            JSELValue lPropertiesArgument = getArgument(aInArguments, 1);

            if (lPropertiesArgument.getType() != Type.UNDEFINED) {
                definePropertiesFunction.call(
                        aInThis,
                        asList(lObject, lPropertiesArgument),
                        aInContext);
            }

            return lObject;
    }
}
