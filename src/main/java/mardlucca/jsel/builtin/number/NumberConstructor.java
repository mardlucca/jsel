/*
 * File: NumberConstructor.java
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
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.wrapper.JSELNumberObject;

import java.util.Collections;
import java.util.List;

public class NumberConstructor extends JSELFunction {
    public static final String MAX_VALUE_PROPERTY = "MAX_VALUE";
    public static final String MIN_VALUE_PROPERTY = "MIN_VALUE";
    public static final String NAN_PROPERTY = "NaN";
    public static final String POSITIVE_INFINITY_PROPERTY = "POSITIVE_INFINITY";
    public static final String NEGATIVE_INFINITY_PROPERTY = "NEGATIVE_INFINITY";

    public NumberConstructor() {
        super(JSELNumberObject.CLASS, Collections.singletonList("value"));

        defineOwnProperty(
                JSELFunction.PROTOTYPE, ExecutionContext.getNumberPrototype(),
                false, false, false);

        defineOwnProperty(MAX_VALUE_PROPERTY, new JSELNumber(Double.MAX_VALUE),
                false, false, false);
        defineOwnProperty(MIN_VALUE_PROPERTY, new JSELNumber(Double.MIN_VALUE),
                false, false, false);
        defineOwnProperty(NAN_PROPERTY, JSELNumber.NAN,
                false, false, false);
        defineOwnProperty(POSITIVE_INFINITY_PROPERTY, JSELNumber.INFINITY,
                false, false, false);
        defineOwnProperty(NEGATIVE_INFINITY_PROPERTY,
                new JSELNumber(Double.NEGATIVE_INFINITY),
                false, false, false);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (aInArguments.isEmpty()) {
            return new JSELNumber(0);
        }

        return new JSELNumber(getArgument(aInArguments).toNumber());
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
                                  ExecutionContext aInExecutionContext) {
        if (aInArguments.isEmpty()) {
            return new JSELNumberObject(new JSELNumber(0));
        }

        return new JSELNumberObject(getArgument(aInArguments));
    }
}
