/*
 * File: RegExpConstructor.java
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
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELRegExp;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.List;

import static java.util.Arrays.asList;

public class RegExpConstructor extends JSELFunction
{
    public RegExpConstructor()
    {
        super(JSELRegExp.CLASS, asList("pattern", "flags"));

        defineOwnProperty(
                JSELFunction.PROTOTYPE, ExecutionContext.getRetExpPrototype(),
                false, false, false);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext)
    {
        JSELValue lPattern = getArgument(aInArguments, 0);
        JSELValue lFlags = getArgument(aInArguments, 1);

        if (lFlags.getType() == Type.UNDEFINED
                && lPattern.getType() == Type.OBJECT
                && lPattern.toObject().getObjectClass().equals(
                        JSELRegExp.CLASS))
        {
            // pattern is a RegExp, so we return it directly
            return lPattern;
        }

        return instantiate(aInArguments, aInExecutionContext);
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
                                  ExecutionContext aInExecutionContext)
    {
        JSELValue lPattern = getArgument(aInArguments, 0);
        JSELValue lFlags = getArgument(aInArguments, 1);
        String lFlagsString = lFlags.getType() == Type.UNDEFINED
                ? ""
                : lFlags.toString();

        if (lPattern.getType() == Type.OBJECT
                && lPattern.toObject().getObjectClass().equals(
                        JSELRegExp.CLASS))
        {
            JSELRegExp lRegExp = (JSELRegExp) lPattern;
            lPattern = new JSELString(lRegExp.getBody());

            if (lFlags.getType() == Type.UNDEFINED)
            {
                // no flags specified so we pick the original ones. This is the
                // behavior in chrome, which is different from ECMA 5. Maybe
                // this is ECMA 6 behavior, which, in my mind, is better than
                // ECMA 5
                lFlagsString = lRegExp.getFlagsString();
            }
        }

        return new JSELRegExp(
                aInArguments.size() == 0 ? null : lPattern.toString(),
                lFlagsString);
    }
}
