/*
 * File: IndexOfFunction.java
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
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.Collections;
import java.util.List;

import static java.lang.Integer.max;

public class IndexOfFunction extends JSELFunction {
    public static final String SLICE = "indexOf";

    public IndexOfFunction() {
        super(SLICE, Collections.singletonList("searchElement"));
    }

    @Override
    public JSELNumber call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                           ExecutionContext aInExecutionContext) {
        JSELObject lThis = aInThisValue.toObject();
        int lLength = lThis.get(JSELArray.LENGTH).toInteger();
        if (lLength == 0) {
            return new JSELNumber(-1);
        }

        int lFromIndexArg = getArgument(aInArguments, 1).toInteger();
        if (lFromIndexArg >= lLength) {
            return new JSELNumber(-1);
        }

        JSELValue lSearchElement = getArgument(aInArguments, 0);
        int lFromIndex = lFromIndexArg < 0
                ? max(lLength + lFromIndexArg, 0)
                : lFromIndexArg;

        for (int i = lFromIndex; i < lLength; i++) {
            String lIndex = String.valueOf(i);
            if (lThis.hasProperty(lIndex)) {
                if (lSearchElement.strictEquals(lThis.get(lIndex))) {
                    return new JSELNumber(i);
                }
            }
        }

        return new JSELNumber(-1);
    }
}
