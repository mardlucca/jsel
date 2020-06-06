/*
 * File: ConcatFunction.java
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
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class ConcatFunction extends JSELFunction {
    public static final String CONCAT = "concat";


    public ConcatFunction() {
        super(CONCAT, singletonList("items"));
    }

    @Override
    public JSELArray call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        List<JSELValue> lItems = new ArrayList<>();

        for (JSELValue lElement : aInArguments) {
            if (lElement.isObjectClass(JSELArray.CLASS)) {
                JSELObject lArray = lElement.toObject();
                int lLength = lArray.get(JSELArray.LENGTH).toInteger();
                for (int i = 0; i < lLength; i++) {
                    String lStringIndex = String.valueOf(i);
                    if (lArray.hasProperty(lStringIndex)) {
                        lItems.add(lArray.get(lStringIndex));
                    }
                }
            }
            else {
                lItems.add(lElement);
            }
        }

        return new JSELArray(lItems);
    }
}
