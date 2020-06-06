/*
 * File: SliceFunction.java
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
import mardlucca.jsel.type.Type;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.util.Arrays.asList;

public class SliceFunction extends JSELFunction {
    public static final String SLICE = "slice";

    public SliceFunction() {
        super(SLICE, asList("start", "end"));
    }

    @Override
    public JSELArray call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        JSELObject lThis = aInThisValue.toObject();
        int lLength = lThis.get(JSELArray.LENGTH).toInteger();
        int lStartArgument = getArgument(aInArguments, 0).toInteger();
        int lFrom = lStartArgument < 0
                ? max(lLength + lStartArgument, 0)
                : min (lStartArgument, lLength);

        JSELValue lEndArgument = getArgument(aInArguments, 1);
        int lEnd = lEndArgument.getType() == Type.UNDEFINED
                ? lLength
                : lEndArgument.toInteger();
        int lTo = lEnd < 0
                ? max(lLength + lEnd, 0)
                : min(lEnd, lLength);

        List<JSELValue> lValues = new ArrayList<>();
        for (int i = lFrom; i < lTo; i++) {
            lValues.add(lThis.get(String.valueOf(i)));
        }

        return new JSELArray(lValues);
    }
}
