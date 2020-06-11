/*
 * File: CallFunction.java
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

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.JSELRuntimeException;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CallFunction extends JSELFunction {
    public static final String NAME = "call";


    public CallFunction() {
        super(NAME, Collections.singletonList("thisArg"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (!aInThis.isCallable()) {
            throw JSELRuntimeException.typeError(aInThis + " is not a function");
        }

        return aInThis.getValue().call(
                getArgument(aInArguments),
                aInArguments.stream().skip(1).collect(toList()),
                aInExecutionContext);
    }
}
