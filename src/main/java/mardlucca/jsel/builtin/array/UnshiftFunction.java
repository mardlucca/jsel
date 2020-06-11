/*
 * File: UnshiftFunction.java
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

import java.util.Collections;
import java.util.List;

public class UnshiftFunction extends JSELFunction {
    public static final String NAME = "unshift";

    public UnshiftFunction() {
        super(NAME, Collections.singletonList("items"));
    }

    @Override
    public JSELValue call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        JSELObject lThis = aInThisValue.toObject();
        int lLength = lThis.get(JSELArray.LENGTH).toInteger();
        int lArgCount = aInArguments.size();

        for (int i = lLength - 1; i >= 0; i--) {
            String lFrom = String.valueOf(i);
            String lTo = String.valueOf(i + lArgCount);
            if (lThis.hasProperty(lFrom)) {
                lThis.put(lTo, lThis.get(lFrom));
            }
            else {
                lThis.delete(lTo);
            }

        }

        for (int i = 0; i < lArgCount; i++) {
            lThis.put(String.valueOf(i), getArgument(aInArguments, i));
        }

        return new JSELNumber(lLength + lArgCount);
    }
}
