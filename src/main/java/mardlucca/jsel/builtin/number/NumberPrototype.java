/*
 * File: NumberPrototype.java
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

import mardlucca.jsel.builtin.DefaultValueOfFunction;
import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.wrapper.JSELNumberObject;
import mardlucca.jsel.builtin.object.ValueOfFunction;

public class NumberPrototype extends JSELNumberObject {

    public NumberPrototype(
            ObjectPrototype aInPrototype) {
        super(aInPrototype, new JSELNumber(0));
    }

    public void initialize() {
        defineOwnProperty(ObjectPrototype.CONSTRUCTOR_PROPERTY,
                new NumberConstructor(),
                false, true, true);

        defineOwnProperty(ToStringFunction.NAME,
                new ToStringFunction(),
                false, true, true);
        defineOwnProperty(ToLocaleStringFunction.NAME,
                new ToLocaleStringFunction(),
                false, true, true);
        defineOwnProperty(ValueOfFunction.NAME,
                new DefaultValueOfFunction(JSELNumberObject.CLASS),
                false, true, true);
        defineOwnProperty(ToFixedFunction.NAME,
                new ToFixedFunction(),
                false, true, true);
        defineOwnProperty(ToExponentialFunction.NAME,
                new ToExponentialFunction(),
                false, true, true);
        defineOwnProperty(ToPrecisionFunction.NAME,
                new ToPrecisionFunction(),
                false, true, true);
    }
}
