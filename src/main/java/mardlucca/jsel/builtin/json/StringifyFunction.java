/*
 * File: StringifyFunction.java
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

package mardlucca.jsel.builtin.json;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.List;

import static java.util.Arrays.asList;

public class StringifyFunction extends JSELFunction {
    public static final String NAME = "stringify";

    public StringifyFunction() {
        super(NAME, asList("value", "replacer", "space"));
    }

    @Override
    public JSELValue call(JSELValue aInThis,
                          List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {

        StringBuilder lStringBuilder = new StringBuilder();
        JSELObject lHolder = new JSELObject();
        lHolder.defineOwnProperty("", getArgument(aInArguments), true, true, true, false);
        stringify(lHolder,
                JSELString.EMPTY_STRING,
                getArgument(aInArguments, 1),
                getArgument(aInArguments, 2),
                0,
                lStringBuilder);
        return new JSELString(lStringBuilder.toString());
    }

    private static void stringify(JSELObject aInObject,
                                  JSELString aInProperty,
                                  JSELValue InReplacer,
                                  JSELValue aInSpace,
                                  int aInIndent,
                                  StringBuilder aOutString) {

        JSELValue lValue = aInObject.get(aInProperty);
    }
}
