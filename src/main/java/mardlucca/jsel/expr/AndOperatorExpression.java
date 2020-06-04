/*
 * File: AndOperatorExpression.java
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
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELValue;

public class AndOperatorExpression implements JSELExpression
{
    protected JSELExpression firstOperandExpression;
    protected JSELExpression secondOperandExpression;

    public AndOperatorExpression(
            JSELExpression aInFirstOperandExpression,
            JSELExpression aInSecondOperandExpression)
    {
        firstOperandExpression = aInFirstOperandExpression;
        secondOperandExpression = aInSecondOperandExpression;
    }

    public JSELValue execute(ExecutionContext aInContext)
    {
        boolean lFirst = firstOperandExpression.execute(aInContext).toBoolean();
        if (!lFirst)
        {
            return JSELBoolean.FALSE;
        }

        return new JSELBoolean(
                secondOperandExpression.execute(aInContext).toBoolean());
    }
}
