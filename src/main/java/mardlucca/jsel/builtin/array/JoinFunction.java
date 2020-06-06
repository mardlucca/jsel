/*
 * File: JoinFunction.java
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
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.List;

import static java.util.Collections.singletonList;

public class JoinFunction extends JSELFunction {
    public static final String JOIN = "join";


    public JoinFunction() {
        super(JOIN, singletonList("separator"));
    }

    @Override
    public JSELString call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                           ExecutionContext aInExecutionContext) {
        JSELObject lThis = aInThisValue.toObject();
        JSELValue lSeparatorArg = getArgument(aInArguments);
        String lSeparator = (lSeparatorArg.getType() == Type.UNDEFINED)
                ? ","
                : lSeparatorArg.toString();

        int lLength = (int) lThis.get(JSELArray.LENGTH).toUInt32();
        StringBuilder lStringBuilder = new StringBuilder();

        for (int i = 0; i < lLength; i++) {
            if (i > 0) {
                lStringBuilder.append(lSeparator);
            }
            JSELValue lValue = lThis.get(String.valueOf(i));
            if (lValue.getType() == Type.NULL
                    || lValue.getType() == Type.UNDEFINED) {
                lStringBuilder.append("");
            }
            else {
                lStringBuilder.append(lValue.toString());
            }
        }

        return new JSELString(lStringBuilder.toString());
    }
}
