/*
 * File: IsNaNFunction.java
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
package mardlucca.jsel.builtin.global;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELValue;

import java.util.List;

public class IsNaNFunction extends JSELFunction {
    public static final String NAME = "isNaN";

    public IsNaNFunction() {
        super(NAME);
    }

    @Override
    public JSELBoolean call(JSELValue aInThis, List<JSELValue> aInArguments,
                            ExecutionContext aInExecutionContext) {
        return Double.isNaN(getArgument(aInArguments).toNumber())
                ? JSELBoolean.TRUE
                : JSELBoolean.FALSE;
    }
}
