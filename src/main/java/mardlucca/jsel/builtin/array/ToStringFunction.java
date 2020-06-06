/*
 * File: ToStringFunction.java
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
package mardlucca.jsel.builtin.array;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;

import java.util.List;

import static java.util.Collections.emptyList;

public class ToStringFunction
        extends mardlucca.jsel.builtin.object.ToStringFunction {
    public static final String TO_STRING = "toString";

    @Override
    public JSELString call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                           ExecutionContext aInExecutionContext) {
        JSELObject lThis = aInThisValue.toObject();
        JSELValue lJoin = lThis.get(JoinFunction.JOIN);
        if (!lJoin.isCallable()) {
            return super.call(lThis, aInArguments, aInExecutionContext);
        }

        return new JSELString(lJoin.call(
                lThis, emptyList(), aInExecutionContext).toString());
    }
}
