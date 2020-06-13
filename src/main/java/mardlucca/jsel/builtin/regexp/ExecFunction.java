/*
 * File: ExecFunction.java
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
package mardlucca.jsel.builtin.regexp;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNull;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELRegExp;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;

import java.util.List;
import java.util.stream.Collectors;

import static mardlucca.jsel.JSELRuntimeException.typeError;
import static mardlucca.jsel.type.JSELRegExp.GLOBAL;
import static mardlucca.jsel.type.JSELRegExp.LAST_INDEX;
import static java.util.Arrays.stream;

public class ExecFunction extends JSELFunction {
    public static final String NAME = "exec";
    private static final String INDEX = "index";
    private static final String INPUT = "input";

    public ExecFunction() {
        super(NAME, null);
    }

    @Override
    public JSELValue call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (!aInThisValue.isObjectCoercible()
                || !aInThisValue.toObject().getObjectClass().equals(
                JSELRegExp.CLASS)) {
            throw typeError("RegExp.prototype.exec called on " +
                    "incompatible receiver " + aInThisValue);
        }

        String lString = getArgument(aInArguments).toString();
        MatchResult lMatchResult = exec(aInThisValue.toObject(), lString);

        if (lMatchResult == null) {
            return JSELNull.getInstance();
        }

        JSELArray lReturn = new JSELArray(
                stream(lMatchResult.getCaptures())
                .map(JSELString::new)
                .collect(Collectors.toList()));
        lReturn.defineOwnProperty(INDEX,
                new JSELNumber(lMatchResult.getStart()),
                true, true, true);
        lReturn.defineOwnProperty(INPUT,
                new JSELString(getArgument(aInArguments).toString()),
                true, true, true);

        return lReturn;
    }

    public static MatchResult exec(JSELObject aInThis, String aInString) {
        int lLastIndex = aInThis.get(LAST_INDEX).toInteger();
        boolean lGlobal = aInThis.get(GLOBAL).toBoolean();
        if (!lGlobal) {
            lLastIndex = 0;
        }

        MatchResult lMatchResult = aInThis.match(aInString, lLastIndex);
        if (lMatchResult == null) {
            aInThis.put(LAST_INDEX, new JSELNumber(0));
        } else if (lGlobal) {
            aInThis.put(LAST_INDEX, new JSELNumber(lMatchResult.getEnd()));
        }

        return lMatchResult;
    }
}
