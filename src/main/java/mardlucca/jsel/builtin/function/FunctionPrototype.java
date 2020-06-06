/*
 * File: FunctionPrototype.java
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
package mardlucca.jsel.builtin.function;

import mardlucca.jsel.builtin.DefaultToStringFunction;
import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.builtin.DefaultToStringFunction;
import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.builtin.object.ToStringFunction;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;

import java.util.List;

import static mardlucca.jsel.builtin.function.CallFunction.CALL;
import static mardlucca.jsel.builtin.object.ObjectPrototype.CONSTRUCTOR_PROPERTY;
import static mardlucca.jsel.builtin.object.ToStringFunction.TO_STRING;

public class FunctionPrototype extends JSELFunction {
    public FunctionPrototype(
            ObjectPrototype aInPrototype) {
        super(aInPrototype, null, null);
    }

    public void initialize() {
        defineOwnProperty(ObjectPrototype.CONSTRUCTOR_PROPERTY, new FunctionConstructor(),
                false, true, true);

        defineOwnProperty(
                ToStringFunction.TO_STRING, new DefaultToStringFunction(JSELFunction.CLASS),
                false, true, true);
        defineOwnProperty(
                CALL, new CallFunction(), false, true, true);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        return JSELUndefined.getInstance();
    }
}
