/*
 * File: RegExpPrototype.java
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
package mardlucca.jsel.builtin.regexp;

import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.type.JSELRegExp;

import static mardlucca.jsel.builtin.object.ObjectPrototype.CONSTRUCTOR_PROPERTY;

public class RegExpPrototype extends JSELRegExp {
    public RegExpPrototype(
            ObjectPrototype aInPrototype) {
        super(aInPrototype, null, null);
    }

    public void initialize() {
        defineOwnProperty(CONSTRUCTOR_PROPERTY, new RegExpConstructor(),
                false, true, true);

        defineOwnProperty(mardlucca.jsel.builtin.object.ToStringFunction.NAME,
                new ToStringFunction(),
                false, true, true);
        defineOwnProperty(ExecFunction.NAME, new ExecFunction(),
                false, true, true);
        defineOwnProperty(TestFunction.NAME, new TestFunction(),
                false, true, true);
    }
}
