/*
 * File: MatchFunction.java
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

import mardlucca.jsel.builtin.regexp.ExecFunction;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNull;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELRegExp;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static mardlucca.jsel.JSELRuntimeException.typeError;
import static mardlucca.jsel.builtin.regexp.ExecFunction.exec;
import static mardlucca.jsel.type.JSELRegExp.GLOBAL;
import static mardlucca.jsel.type.JSELRegExp.LAST_INDEX;
import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;

public class MatchFunction extends JSELFunction {
    public static final String MATCH = "match";

    /**
     * Being lazy but this is exactly how the spec describes the implementation
     * of this function
     */
    private ExecFunction execFunction = new ExecFunction();

    public MatchFunction() {
        super(MATCH, Collections.singletonList("regexp"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (aInThis.getType() == Type.NULL
                || aInThis.getType() == Type.UNDEFINED) {
            throw JSELRuntimeException.typeError(
                    "String.prototype.match called on null or undefined");
        }

        String lString = aInThis.toString();
        JSELValue lRegExpParameter = getArgument(aInArguments);

        if (lRegExpParameter.getType() != Type.OBJECT
                || !lRegExpParameter.toObject().getObjectClass().equals(
                        JSELRegExp.CLASS)) {
            lRegExpParameter = new JSELRegExp(
                    lRegExpParameter.toString(), emptySet());
        }

        JSELObject lRegExp = lRegExpParameter.toObject();

        boolean lGlobal = lRegExp.get(JSELRegExp.GLOBAL).toBoolean();

        if (!lGlobal) {
            return execFunction.call(
                    lRegExp, singletonList(aInThis), aInExecutionContext);
        }

        // resets the reg exp
        lRegExp.put(JSELRegExp.LAST_INDEX, new JSELNumber(0));

        int lPreviousLastIndex = 0;
        List<String> lValues = new ArrayList<>();

        for (MatchResult lResult = exec(lRegExp, lString);
                lResult != null;
                lResult = exec(lRegExp, lString)) {
            int lThisIndex = lResult.getEnd();
            if (lThisIndex == lPreviousLastIndex) {
                // this is required for empty regexps, as matching the empty
                // string does not advance "lastIndex"
                lPreviousLastIndex = lThisIndex + 1;
                lRegExp.put(JSELRegExp.LAST_INDEX, new JSELNumber(lPreviousLastIndex));
            }
            else {
                lPreviousLastIndex = lThisIndex;
            }

            // only interested in the full match (index 0), not capturing groups
            lValues.add(lResult.getCaptures()[0]);
        }

        return lValues.isEmpty()
                ? JSELNull.getInstance()
                : new JSELArray(lValues.stream()
                        .map(JSELString::new)
                        .collect(Collectors.toList()));
    }
}
