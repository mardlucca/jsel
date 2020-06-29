/*
 * File: ObjectPrototype.java
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
package mardlucca.jsel.builtin.object;

import mardlucca.jsel.type.JSELObject;

import static mardlucca.jsel.builtin.object.GetOwnPropertyDescriptorFunction.NAME;

public class ObjectPrototype extends JSELObject {
    public static final String CONSTRUCTOR_PROPERTY = "constructor";

    /**
     * This is the only object in JSEL and ECMAScript 5.1 to not have an
     * internal [[Prototype]] property;
     * @see <a
     * href="http://www.ecma-international.org/ecma-262/5.1/#sec-15.2.4">
     * ECMA-262, 5.1, Section 15.2.4 - Properties of the Object Prototype
     * Object</a>
     */
    public ObjectPrototype() {
        super(null);
    }

    public void initialize() {
        defineOwnProperty(CONSTRUCTOR_PROPERTY, new ObjectConstructor(),
                false, true, true);

        defineOwnProperty(ToStringFunction.NAME, new ToStringFunction(),
                false, true, true);
        defineOwnProperty(ToLocaleStringFunction.NAME,
                new ToLocaleStringFunction(), false, true, true);
        defineOwnProperty(ValueOfFunction.NAME,
                new ValueOfFunction(), false, true, true);
        defineOwnProperty(HasOwnPropertyFunction.NAME,
                new HasOwnPropertyFunction(), false, true, true);
        defineOwnProperty(IsPrototypeOfFunction.NAME,
                new IsPrototypeOfFunction(), false, true, true);
        defineOwnProperty(PropertyIsEnumerableFunction.NAME,
                new PropertyIsEnumerableFunction(), false, true, true);
    }
}
