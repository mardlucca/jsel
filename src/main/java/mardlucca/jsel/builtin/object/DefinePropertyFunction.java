/*
 * File: DefinePropertyFunction.java
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
import mardlucca.jsel.type.*;

import java.util.List;

import static java.util.Arrays.asList;
import static mardlucca.jsel.JSELRuntimeException.typeError;
import static mardlucca.jsel.type.JSELObject.PropertyDescriptor.toPropertyDescriptor;

public class DefinePropertyFunction extends JSELFunction {
    public static final String NAME = "defineProperty";

    public DefinePropertyFunction() {
        super(NAME, asList("object", "property", "attributes"));
    }

    @Override
    public JSELObject call(JSELValue aInThis, List<JSELValue> aInArguments,
                           ExecutionContext aInContext) {
        JSELValue lArgument = getArgument(aInArguments);
        if (lArgument.getType() != Type.OBJECT) {
            throw typeError("Argument is not an object");
        }

        JSELObject lObject = lArgument.toObject();
        String lProperty = getArgument(aInArguments, 1).toString();

        lObject.defineOwnProperty(
                lProperty,
                toPropertyDescriptor(getArgument(aInArguments, 2)),
                true);

        return lObject;
    }
}
