/*
 * File: ToPrecisionFunction.java
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

package mardlucca.jsel.builtin.number;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.type.wrapper.JSELNumberObject;
import mardlucca.jsel.type.wrapper.JSELPrimitiveWrapper;

import java.util.Collections;
import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.rangeError;
import static mardlucca.jsel.JSELRuntimeException.typeError;

public class ToPrecisionFunction extends JSELFunction {
    public static final String NAME = "toPrecision";
    private static final int MAX_DIGITS = 20;

    public ToPrecisionFunction() {
        super(NAME, Collections.singletonList("precision"));
    }

    @Override
    public JSELValue call(
            JSELValue aInThis,
            List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext) {

        if (aInThis.getType() != Type.NUMBER
                && !aInThis.isObjectClass(JSELNumberObject.CLASS)) {
            throw typeError("Number.prototype.toFixed requires that 'this' " +
                    "be a Number");
        }

        int lPrecision = getArgument(aInArguments).toInteger();

        if (lPrecision < 1 || lPrecision > MAX_DIGITS) {
            throw rangeError(
                    "toPrecision() argument must be between 1 and 20");
        }

        double lNumber = aInThis.getType() == Type.NUMBER
                ? aInThis.toNumber()
                : ((JSELPrimitiveWrapper) aInThis).getPrimitiveValue()
                .toNumber();

        if (Double.isNaN(lNumber)) {
            return new JSELString("NaN");
        }

        return new JSELString(
                String.format("%1." + lPrecision + "g", lNumber));
    }
}
