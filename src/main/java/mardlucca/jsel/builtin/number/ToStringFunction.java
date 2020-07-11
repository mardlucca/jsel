/*
 * File: ToStringFunction.java
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
import mardlucca.jsel.type.*;
import mardlucca.jsel.type.wrapper.JSELNumberObject;
import mardlucca.jsel.type.wrapper.JSELPrimitiveWrapper;
import mardlucca.jsel.util.DecimalFormat;

import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.rangeError;
import static mardlucca.jsel.JSELRuntimeException.typeError;

public class ToStringFunction extends JSELFunction {
    public static final String NAME = "toString";

    public ToStringFunction() {
        super(NAME);
    }

    @Override
    public JSELString call(JSELValue aInThis, List<JSELValue> aInArguments,
                           ExecutionContext aInExecutionContext) {
        if (aInThis.getType() != Type.NUMBER
                && !aInThis.isObjectClass(JSELNumberObject.CLASS)) {
            throw typeError("Number.prototype.toString requires that 'this' " +
                    "be a Number");
        }

        double lNumber = aInThis.getType() == Type.NUMBER
                ? aInThis.toNumber()
                : ((JSELPrimitiveWrapper) aInThis).getPrimitiveValue()
                        .toNumber();

        JSELValue lRadixArg = getArgument(aInArguments);
        int lRadix = lRadixArg.getType() == Type.UNDEFINED
                ? 10
                : lRadixArg.toInteger();

        if (lRadix == 10) {
            return new JSELString(DecimalFormat.format(lNumber));
        }

        if (lRadix < 2 || lRadix > 36) {
            throw rangeError("radix argument must be between 2 and 36");
        }

        return new JSELString(Long.toString((long) lNumber, lRadix));
    }
}
