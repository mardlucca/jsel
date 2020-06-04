/*
 * File: ConditionalOperatorExpression.java
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
import mardlucca.jsel.type.JSELValue;

public class ConditionalOperatorExpression implements JSELExpression
{
    private JSELExpression booleanExpression;
    private JSELExpression trueExpression;
    private JSELExpression falseExpression;


    public ConditionalOperatorExpression(
            JSELExpression aInBooleanExpression,
            JSELExpression aInTrueExpression,
            JSELExpression aInFalseExpression)
    {
        booleanExpression = aInBooleanExpression;
        trueExpression = aInTrueExpression;
        falseExpression = aInFalseExpression;
    }

    @Override
    public JSELValue execute(ExecutionContext aInContext)
    {
        JSELValue lResult = booleanExpression.execute(aInContext);

        if (lResult.toBoolean())
        {
            return trueExpression.execute(aInContext);
        }

        return falseExpression.execute(aInContext);
    }
}
