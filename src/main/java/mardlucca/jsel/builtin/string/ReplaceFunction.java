/*
 * File: ReplaceFunction.java
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
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELRegExp;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.List;
import java.util.regex.Matcher;

import static mardlucca.jsel.JSELRuntimeException.typeError;
import static mardlucca.jsel.type.JSELRegExp.GLOBAL;
import static java.util.Arrays.asList;

public class ReplaceFunction extends JSELFunction
{
    public static final String REPLACE = "replace";

    /**
     * Being lazy but this is exactly how the spec describes the implementation
     * of this function
     */
    private ExecFunction execFunction = new ExecFunction();

    public ReplaceFunction()
    {
        super(REPLACE, asList("searchValue", "replaceValue"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext)
    {
        if (aInThis.getType() == Type.NULL
                || aInThis.getType() == Type.UNDEFINED)
        {
            throw JSELRuntimeException.typeError(
                    "String.prototype.replace called on null or undefined");
        }

        String lString = aInThis.toString();
        JSELValue lSearchValueParam = getArgument(aInArguments, 0);
        String lReplaceValue = getArgument(aInArguments, 1).toString();

        if (lSearchValueParam.getType() == Type.OBJECT
                && lSearchValueParam.toObject().getObjectClass().equals(
                        JSELRegExp.CLASS))
        {
            // search value is a regex
            JSELRegExp lRegExp = (JSELRegExp) lSearchValueParam;
            Matcher lMatcher = lRegExp.getPattern().matcher(lString);
            return new JSELString(lRegExp.get(JSELRegExp.GLOBAL).toBoolean()
                    ? lMatcher.replaceAll(lReplaceValue)
                    : lMatcher.replaceFirst(lReplaceValue));
        }

        // convert search value to string and replace
        return new JSELString(
                lString.replace(lSearchValueParam.toString(), lReplaceValue));
    }
}
