/*
 * File: JSELDate.java
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
package mardlucca.jsel.type;

import mardlucca.jsel.type.JSELValue.GetHint;

/**
 * This represents the Date object type in JSEL.
 */
public class JSELDate extends JSELObject {
    /**
     * Constant used for the internal [[Class]] property for objects of this
     * type.
     */
    public static final String CLASS = "Date";

    /**
     * Converts this Date value to a primitive.
     * @param aInHint a hint to use in the conversion. If not specified,
     *                GetHint.STRING is used.
     * @return the default/primitive value.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.12.8">
     * ECMA-262, 5.1, Section 8.12.8"</a>
     */
    @Override
    protected JSELValue defaultValue(GetHint aInHint) {
        return aInHint == null ?
                defaultValue(GetHint.STRING) :
                super.defaultValue(aInHint);
    }

    @Override
    public String getObjectClass() {
        return CLASS;
    }
}
