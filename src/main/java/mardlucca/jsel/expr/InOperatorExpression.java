/*
 * File: InOperatorExpression.java
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
import mardlucca.jsel.type.Type;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;

import static mardlucca.jsel.JSELRuntimeException.typeError;

public class InOperatorExpression extends BinaryOperatorExpression {
    public InOperatorExpression(
            JSELExpression aInFirstOperandExpression,
            JSELExpression aInSecondOperandExpression) {
        super(aInFirstOperandExpression, aInSecondOperandExpression);
    }

    @Override
    protected JSELValue operate(JSELValue aInFirstOperand,
                                JSELValue aInSecondOperand) {
        if (aInSecondOperand.getType() != Type.OBJECT) {
            throw JSELRuntimeException.typeError("Cannot use 'in' operator to search for '"
                    + aInFirstOperand + "' in " + aInSecondOperand);
        }

        return new JSELBoolean(aInSecondOperand.toObject()
                .hasProperty(aInFirstOperand));
    }
}
