/*
 * File: ApplyFunction.java
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

import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class ApplyFunction extends JSELFunction {
    public static final String NAME = "apply";


    public ApplyFunction() {
        super(NAME, asList("thisArg", "argArray"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (!aInThis.isCallable()) {
            throw JSELRuntimeException.typeError(
                    aInThis + " is not a function");
        }

        JSELValue lThisArg = getArgument(aInArguments);
        JSELValue lArgArray = getArgument(aInArguments, 1);
        if (lArgArray.getType() == Type.NULL
                || lArgArray.getType() == Type.UNDEFINED) {
            return aInThis.call(
                    lThisArg, Collections.emptyList(), aInExecutionContext);
        }

        return aInThis.call(
                lThisArg,
                lArgArray.toList(),
                aInExecutionContext);
    }
}
