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
package mardlucca.jsel.builtin.string;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;

import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.typeError;
import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class SliceFunction extends JSELFunction {
    public static final String SLICE = "slice";

    public SliceFunction() {
        super(SLICE);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (aInThis.getType() == Type.NULL
                || aInThis.getType() == Type.UNDEFINED) {
            throw JSELRuntimeException.typeError(
                    "String.prototype.slice called on null or undefined");
        }

        String lString = aInThis.toString();
        int lIntStart = getArgument(aInArguments, 0).toInteger();

        JSELValue lEndArgument = getArgument(aInArguments, 1);
        int lIntEnd = lEndArgument.getType() == Type.UNDEFINED
                ? lString.length()
                : lEndArgument.toInteger();
        int lFrom = lIntStart < 0
                ? max(lString.length() + lIntStart, 0)
                : min(lIntStart, lString.length());
        int lTo = lIntEnd < 0
                ? max(lString.length() + lIntEnd, 0)
                : min(lIntEnd, lString.length());

        // return substring, swapping the order of min and max if required
        return new JSELString(lString.substring(lFrom, max(lFrom, lTo)));
    }
}
