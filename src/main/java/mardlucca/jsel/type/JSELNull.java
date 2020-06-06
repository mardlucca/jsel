/*
 * File: JSELNull.java
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

import static mardlucca.jsel.JSELRuntimeException.typeError;

/**
 * This represents the null data type in JSEL.
 */
public class JSELNull extends JSELValue {
    /**
     * Singleton for the null value
     */
    private static final JSELNull instance = new JSELNull();

    /**
     * Private constructor
     */
    private JSELNull() {
    }

    /**
     * Gets the singleton instance for  "null".
     * @return the singleton.
     */
    public static JSELNull getInstance() {
        return instance;
    }

    @Override
    public boolean isObjectCoercible() {
        return false;
    }

    @Override
    public Type getType() {
        return Type.NULL;
    }

    @Override
    public boolean toBoolean() {
        return false;
    }

    @Override
    public double toNumber() {
        return 0.0;
    }

    @Override
    public JSELObject toObject() {
        // a Javascript nuance, as typeof null == 'object' but we cannot convert
        // null to an object.
        throw typeError("null cannot be converted to object");
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean equals(JSELValue aInObject) {
        aInObject = aInObject.getValue();
        // "null == undefined" is true
        return aInObject == this || aInObject == JSELUndefined.getInstance();
    }
}
