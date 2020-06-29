/*
 * File: BindFunction.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BindFunction extends JSELFunction {
    public static final String NAME = "bind";

    public BindFunction() {
        super(NAME, Collections.singletonList("thisArg"));
    }

    @Override
    public JSELValue call(JSELValue aInTarget, List<JSELValue> aInArguments,
                          ExecutionContext aInOriginalContext) {
        if (!aInTarget.isCallable()) {
            throw JSELRuntimeException.typeError(
                    aInTarget + " is not a function");
        }
        JSELValue lBoundThis = getArgument(aInArguments);
        List<JSELValue> lBoundArgs = aInArguments.stream().skip(1).collect(
                java.util.stream.Collectors.toList());
        int lTargetLength = aInTarget.toObject()
                .get(JSELFunction.LENGTH).toInt32();
        int lLength = Math.max(0, aInArguments.size() - 1);

        return new JSELFunction() {
            {
                defineOwnProperty(LENGTH,
                        new JSELNumber(Math.max(lTargetLength - lLength, 0)),
                        false, false, false);
            }

            @Override
            public JSELValue call(
                    JSELValue aInThis,
                    List<JSELValue> aInExtraArguments,
                    ExecutionContext aInNewContext) {
                List<JSELValue> lNewArgs = new ArrayList<>(lBoundArgs);
                lNewArgs.addAll(aInExtraArguments);

                return aInTarget.call(lBoundThis, lNewArgs, aInOriginalContext);
            }

            @Override
            public JSELObject instantiate(
                    List<JSELValue> aInExtraArguments,
                    ExecutionContext aInNewContext) {
                List<JSELValue> lNewArgs = new ArrayList<>(lBoundArgs);
                lNewArgs.addAll(aInExtraArguments);

                return aInTarget.instantiate(lNewArgs, aInOriginalContext);
            }

            @Override
            public JSELBoolean hasInstance(JSELValue aInValue) {
                return aInTarget.hasInstance(aInValue);
            }
        };
    }
}
