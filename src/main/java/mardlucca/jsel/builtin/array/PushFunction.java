/*
 * File: PushFunction.java
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
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;

import java.util.List;

public class PushFunction extends JSELFunction {
    public static final String NAME = "push";


    public PushFunction() {
        super(NAME, null);
    }

    @Override
    public JSELValue call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        JSELObject lThis = aInThisValue.toObject();
        int lLength = lThis.get(JSELArray.LENGTH).toInteger();

        for (int i = 0; i < aInArguments.size(); i++) {
            lThis.put(String.valueOf(lLength + i), aInArguments.get(i));
        }

        return new JSELNumber(aInArguments.size());
    }
}
