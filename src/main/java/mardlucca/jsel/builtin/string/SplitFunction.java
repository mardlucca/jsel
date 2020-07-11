/*
 * File: SplitFunction.java
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
package mardlucca.jsel.builtin.string;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.JSELRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;

public class SplitFunction extends JSELFunction {
    public static final String NAME = "split";

    public SplitFunction() {
        super(NAME, asList("separator", "limit"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (aInThis.getType() == Type.NULL
                || aInThis.getType() == Type.UNDEFINED) {
            throw JSELRuntimeException.typeError(
                    "String.prototype.split called on null or undefined");
        }

        String lString = aInThis.toString();
        JSELValue lSeparator = getArgument(aInArguments, 0);

        if (lSeparator.getType() == Type.UNDEFINED || lString.isEmpty()) {
            if (lSeparator.match(lString, 0) != null) {
                return new JSELArray();
            }
            return new JSELArray(singletonList(new JSELString(lString)));
        }

        int lIndex = 0;
        List<String> lStrings = new ArrayList<>();
        while (lIndex < lString.length()) {
            MatchResult lResult = lSeparator.match(lString, lIndex);
            if (lResult == null) {
                break;
            }

            lStrings.addAll(asList(lResult.getCaptures()));
        }

        return new JSELArray(
                lStrings.stream()
                    .map(JSELString::new)
                    .collect(Collectors.toList()));
    }
}
