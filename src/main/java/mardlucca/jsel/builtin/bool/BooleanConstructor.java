/*
 * File: BooleanConstructor.java
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
package mardlucca.jsel.builtin.bool;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.wrapper.JSELBooleanObject;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.wrapper.JSELBooleanObject;

import java.util.Collections;
import java.util.List;

public class BooleanConstructor extends JSELFunction {
    public BooleanConstructor() {
        super(JSELBooleanObject.CLASS, Collections.singletonList("value"));

        defineOwnProperty(
                JSELFunction.PROTOTYPE, ExecutionContext.getBooleanPrototype(),
                false, false, false);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        return getArgument(aInArguments).toBoolean()
                ? JSELBoolean.TRUE
                : JSELBoolean.FALSE;
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
                                  ExecutionContext aInExecutionContext) {
        return new JSELBooleanObject(getArgument(aInArguments));
    }
}
