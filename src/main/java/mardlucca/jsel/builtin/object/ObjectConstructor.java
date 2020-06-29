/*
 * File: ObjectConstructor.java
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
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;

import java.util.List;

import static java.util.Collections.singletonList;
import static mardlucca.jsel.builtin.object.GetOwnPropertyDescriptorFunction.NAME;

public class ObjectConstructor extends JSELFunction {
    public ObjectConstructor() {
        super(JSELObject.CLASS, singletonList("value"));

        defineOwnProperty(
                JSELFunction.PROTOTYPE, ExecutionContext.getObjectPrototype(),
                false, false, false);

        defineOwnProperty(GetPrototypeOfFunction.NAME,
                new GetPrototypeOfFunction(), false, true, true);
        defineOwnProperty(GetOwnPropertyDescriptorFunction.NAME,
                new GetOwnPropertyDescriptorFunction(), false, true, true);
        defineOwnProperty(GetOwnPropertyNamesFunction.NAME,
                new GetOwnPropertyNamesFunction(), false, true, true);
        defineOwnProperty(CreateFunction.NAME,
                new CreateFunction(), false, true, true);
        defineOwnProperty(DefinePropertiesFunction.NAME,
                new DefinePropertiesFunction(), false, true, true);
        defineOwnProperty(DefinePropertyFunction.NAME,
                new DefinePropertyFunction(), false, true, true);
        defineOwnProperty(SealFunction.NAME,
                new SealFunction(), false, true, true);
        defineOwnProperty(FreezeFunction.NAME,
                new FreezeFunction(), false, true, true);
        defineOwnProperty(PreventExtensionFunction.NAME,
                new PreventExtensionFunction(), false, true, true);
        defineOwnProperty(IsSealedFunction.NAME,
                new IsSealedFunction(), false, true, true);
        defineOwnProperty(IsFrozenFunction.NAME,
                new IsFrozenFunction(), false, true, true);
        defineOwnProperty(IsExtensibleFunction.NAME,
                new IsExtensibleFunction(), false, true, true);
        defineOwnProperty(KeysFunction.NAME,
                new KeysFunction(), false, true, true);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        JSELValue lArgument = getArgument(aInArguments);
        if (lArgument.getType() == Type.NULL
                || lArgument.getType() == Type.UNDEFINED) {
            return instantiate(aInArguments, aInExecutionContext);
        }

        return lArgument.toObject();
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext) {
        JSELValue lArgument = getArgument(aInArguments);
        if (lArgument.getType() != Type.NULL
                && lArgument.getType() != Type.UNDEFINED) {
            return lArgument.toObject();
        }

        // Always use object prototype from global object
        return new JSELObject();
    }
}
