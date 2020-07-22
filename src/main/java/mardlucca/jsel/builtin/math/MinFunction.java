/*
 * File: MinFunction.java
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

package mardlucca.jsel.builtin.math;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELValue;

import java.util.List;

import static java.util.Arrays.asList;

public class MinFunction extends JSELFunction {
    public static final String NAME = "min";

    public MinFunction() {
        super(NAME, asList("x", "y"));
    }

    @Override
    public JSELValue call(JSELValue aInThis,
                          List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        double lMin = Double.POSITIVE_INFINITY;

        for (JSELValue lValue: aInArguments) {
            double lDouble = lValue.toNumber();
            if (Double.isNaN(lDouble)) { return JSELNumber.NAN; }
            lMin = Math.min(lMin, lDouble);
        }

        return new JSELNumber(lMin);
    }
}
