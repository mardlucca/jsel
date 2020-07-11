/*
 * File: SearchFunction.java
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
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELRegExp;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.JSELRuntimeException;

import java.util.List;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;

public class SearchFunction extends JSELFunction {
    public static final String NAME = "search";

    public SearchFunction() {
        super(NAME, singletonList("regexp"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (aInThis.getType() == Type.NULL
                || aInThis.getType() == Type.UNDEFINED) {
            throw JSELRuntimeException.typeError(
                    "String.prototype.match called on null or undefined");
        }

        JSELValue lRegExpParameter = getArgument(aInArguments);

        if (lRegExpParameter.getType() != Type.OBJECT
                || !lRegExpParameter.toObject().getObjectClass().equals(
                JSELRegExp.CLASS)) {
            lRegExpParameter = new JSELRegExp(
                    lRegExpParameter.toString(), emptySet());
        }

        JSELObject lRegExp = lRegExpParameter.toObject();
        MatchResult lResult = lRegExp.match(aInThis.toString(), 0);
        return lResult == null
                ? new JSELNumber(-1)
                : new JSELNumber(lResult.getStart());
    }
}
