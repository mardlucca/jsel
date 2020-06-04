/*
 * File: FomCharCodeFunction.java
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
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;

import java.util.Collections;
import java.util.List;

import static mardlucca.jsel.type.JSELNumber.toChar;

public class FomCharCodeFunction extends JSELFunction
{
    public static final String FROM_CHAR_CODE = "fromCharCode";

    public FomCharCodeFunction()
    {
        super(FROM_CHAR_CODE, Collections.singletonList("chars"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext)
    {
        if (aInArguments.isEmpty())
        {
            return JSELString.EMPTY_STRING;
        }

        char[] lChars = new char[aInArguments.size()];
        for (int i = 0; i < lChars.length; i++)
        {
            lChars[i] = JSELNumber.toChar(aInArguments.get(i).toNumber());
        }

        return new JSELString(new String(lChars));
    }
}
