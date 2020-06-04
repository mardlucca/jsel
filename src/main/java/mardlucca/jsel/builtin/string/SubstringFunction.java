/*
 * File: SubstringFunction.java
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

import java.util.List;

import static java.lang.Integer.min;
import static java.lang.Integer.max;

public class SubstringFunction extends JSELFunction
{
    public static final String SUBSTRING = "substring";

    public SubstringFunction()
    {
        super(SUBSTRING);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext)
    {
        if (aInThis.getType() == Type.NULL
                || aInThis.getType() == Type.UNDEFINED)
        {
            throw JSELRuntimeException.typeError(
                    "String.prototype.substring called on null or undefined");
        }

        String lString = aInThis.toString();
        int lStart = min(
                max(getArgument(aInArguments, 0).toInteger(), 0),
                lString.length());
        JSELValue lEndArgument = getArgument(aInArguments, 1);
        int lEnd = lEndArgument.getType() == Type.UNDEFINED
                ? lString.length()
                : min(max(lEndArgument.toInteger(), 0), lString.length());

        // return substring, swapping the order of min and max if required
        return new JSELString(aInThis.toString().substring(
                min(lStart, lEnd),
                max(lStart, lEnd)));
    }
}
