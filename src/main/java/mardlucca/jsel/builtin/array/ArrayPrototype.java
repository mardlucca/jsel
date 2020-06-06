/*
 * File: ArrayPrototype.java
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
package mardlucca.jsel.builtin.array;

import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.type.JSELArray;

import static mardlucca.jsel.builtin.object.ObjectPrototype.CONSTRUCTOR_PROPERTY;

public class ArrayPrototype extends JSELArray {
    public ArrayPrototype(
            ObjectPrototype aInPrototype) {
        super(aInPrototype, 0);
    }

    public void initialize() {
        defineOwnProperty(ObjectPrototype.CONSTRUCTOR_PROPERTY, new ArrayConstructor(),
                false, true, true);
    }
}
