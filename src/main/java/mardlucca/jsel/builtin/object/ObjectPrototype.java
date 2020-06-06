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
import mardlucca.jsel.type.JSELObject;

import static mardlucca.jsel.builtin.object.GetOwnPropertyDescriptorFunction.GET_OWN_PROPERTY_DESCRIPTOR;
import static mardlucca.jsel.builtin.object.ToStringFunction.TO_STRING;
import static mardlucca.jsel.builtin.object.ValueOfFunction.VALUE_OF;

public class ObjectPrototype extends JSELObject {
    public static final String CONSTRUCTOR_PROPERTY = "constructor";

    /**
     * This is the only object in JSEL and ECMAScript 5.1 to not have an
     * internal [[Prototype]] property;
     */
    public ObjectPrototype() {
        super(null);
    }

    public void initialize() {
        defineOwnProperty(CONSTRUCTOR_PROPERTY, new ObjectConstructor(),
                false, true, true);

        defineOwnProperty(GET_OWN_PROPERTY_DESCRIPTOR,
                new GetOwnPropertyDescriptorFunction(), false, true, true);
        defineOwnProperty(ToStringFunction.TO_STRING, new ToStringFunction(),
                false, true, true);
        defineOwnProperty(ValueOfFunction.VALUE_OF, new ValueOfFunction(),
                false, true, true);


    }
}
