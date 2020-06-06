/*
 * File: JSELStringObject.java
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
package mardlucca.jsel.type.wrapper;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.util.DecimalFormat;
import mardlucca.jsel.type.*;
import mardlucca.jsel.util.DecimalFormat;

public class JSELStringObject extends JSELPrimitiveWrapper {
    public static final String LENGTH = "length";

    public static final String CLASS = "String";

    public JSELStringObject(JSELValue aInPrimitive) {
        this(ExecutionContext.getStringPrototype(), aInPrimitive);
    }

    protected JSELStringObject(JSELObject aInPrototype, JSELValue aInPrimitive) {
        super((aInPrimitive != null && aInPrimitive.getType() == Type.STRING)
                ? aInPrimitive
                : aInPrimitive == null
                        ? JSELString.EMPTY_STRING
                        : new JSELString(aInPrimitive.toString()),
                aInPrototype);

        defineOwnProperty(
                LENGTH,
                new JSELNumber(
                        getPrimitiveValue().toString().length()),
                        false, false, false);
    }

    @Override
    public String getObjectClass() {
        return CLASS;
    }

    @Override
    public PropertyDescriptor getOwnProperty(String aInProperty) {
        PropertyDescriptor lDescriptor = super.getOwnProperty(aInProperty);
        if (lDescriptor != null) {
            return lDescriptor;
        }

        long lNumber = JSELNumber.toInteger(DecimalFormat.parse(aInProperty));
        if (aInProperty.equals(DecimalFormat.format(lNumber))) {
            // property is an index
            String lString = getPrimitiveValue().toString();
            if (lNumber >= 0 && lNumber < lString.length()) {
                return new PropertyDescriptor(
                        new JSELString(String.valueOf(
                                lString.charAt((int) lNumber))),
                        true, false, false);
            }
        }

        return null;
    }
}
