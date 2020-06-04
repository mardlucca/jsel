/*
 * File: RelationalOperatorExpression.java
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

import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.JSELValue.GetHint;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.JSELValue.GetHint;
import mardlucca.jsel.type.Type;

public abstract class RelationalOperatorExpression extends BinaryOperatorExpression
{
    public RelationalOperatorExpression(
            JSELExpression aInFirstOperandExpression,
            JSELExpression aInSecondOperandExpression)
    {
        super(aInFirstOperandExpression, aInSecondOperandExpression);
    }

    @Override
    protected JSELValue operate(JSELValue aInFirstOperand,
                                JSELValue aInSecondOperand)
    {
        aInFirstOperand = aInFirstOperand.toPrimitive(GetHint.NUMBER);
        aInSecondOperand = aInSecondOperand.toPrimitive(GetHint.NUMBER);

        return aInFirstOperand.getType() == Type.STRING
                && aInSecondOperand.getType() == Type.STRING
                ? operateAsString(aInFirstOperand, aInSecondOperand)
                : operateAsNumber(aInFirstOperand, aInSecondOperand);
    }

    protected abstract JSELBoolean operateAsString(JSELValue aInFirstOperand,
                                                   JSELValue aInSecondOperand);

    protected abstract JSELBoolean operateAsNumber(JSELValue aInFirstOperand,
            JSELValue aInSecondOperand);
}
