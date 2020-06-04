/*
 * File: FunctionCallExpression.java
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
package mardlucca.jsel.expr;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.JSELRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallExpression implements JSELExpression
{
    private JSELExpression functionExpression;
    private List<JSELExpression> argumentExpressions;

    public FunctionCallExpression(
            JSELExpression aInFunctionExpression,
            List<JSELExpression> aInArgumentExpressions)
    {
        functionExpression = aInFunctionExpression;
        argumentExpressions = aInArgumentExpressions;
    }

    @Override
    public JSELValue execute(ExecutionContext aInContext)
    {
        JSELValue lFunctionObject =
                functionExpression.execute(aInContext);
        if (!lFunctionObject.isCallable())
        {
            throw JSELRuntimeException.typeError(lFunctionObject + " is not a function");
        }

        List<JSELValue> lArguments =
                new ArrayList<>(argumentExpressions.size());
        for (JSELExpression lArgumentExpression : argumentExpressions)
        {
            // function arguments are never property references
            lArguments.add(lArgumentExpression.execute(aInContext));
        }

        // function calls never return property references in JS
        return lFunctionObject.call(
                JSELUndefined.getInstance(), lArguments, aInContext).getValue();
    }
}
