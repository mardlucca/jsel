/*
 * File: JSELString.java
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

import mardlucca.jsel.type.wrapper.JSELStringObject;
import mardlucca.jsel.util.DecimalFormat;
import mardlucca.jsel.util.DecimalFormat;

/**
 * This represents the string data type in JSEL.
 */
public class JSELString extends JSELValue {
    /**
     * Constant for an empty JSELString
     */
    public static final JSELString EMPTY_STRING = new JSELString("");

    /**
     * The underlying string value.
     */
    private String string;

    /**
     * Constructor
     * @param aInString the underlying string value.
     */
    public JSELString(String aInString) {
        string = aInString;
    }

    @Override
    public Type getType() {
        return Type.STRING;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean toBoolean() {
        // note that "string" will never be null.
        // empty string is equivalent to false.
        return !string.equals("");
    }

    /**
     * While ECMAScript 5.1 specifies a grammar for the strings that can be
     * converted to a double, JSEL uses essentially {@link Double#parseDouble(
     * String)} with {@link DecimalFormat#parse(String) a few differences}.
     * @return this string as a double.
     */
    @Override
    public double toNumber() {
        return DecimalFormat.parse(string);
    }

    @Override
    public JSELObject toObject() {
        return new JSELStringObject(this);
    }

    @Override
    public JSELValue toPrimitive(GetHint aInHint) {
        return this;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public boolean equals(JSELValue aInValue) {
        aInValue = aInValue.getValue();
        if (aInValue == this) {
            return true;
        }

        if (aInValue.getType() == Type.BOOLEAN) {
            return new JSELNumber(toNumber()).equals(
                    new JSELNumber(aInValue.toNumber()));
        }

        if (aInValue.getType() == Type.NUMBER) {
            return new JSELNumber(toNumber()).equals(aInValue);
        }

        if (aInValue.getType() == Type.STRING) {
            return string.equals(aInValue.toString());
        }

        if (aInValue.getType() == Type.OBJECT) {
            return equals(aInValue.toPrimitive(GetHint.STRING));
        }

        return false;
    }

    @Override
    public boolean strictEquals(JSELValue aInValue) {
        aInValue = aInValue.getValue();
        if (aInValue == this) {
            return true;
        }

        if (aInValue.getType() == Type.STRING) {
            return string.equals(aInValue.toString());
        }

        return false;
    }

    /**
     * Matching a JSELString against another string essentially tries to find
     * this JSELString inside the given string, starting from a given position
     * in the input string.
     *
     * @param aInString a input string to match this JSELValue against.
     * @param aInIndex an index inside input string aInString to start the
     *                 matching execution from.
     * @return a MatchResult value with the result of the operation or null if
     * this JSELString cannot be found in aInString starting from aInIndex.
     */
    @Override
    public MatchResult match(String aInString, int aInIndex) {
        int lMatchIndex = string.indexOf(aInString, aInIndex);
        return lMatchIndex == -1
                ? null
                : new MatchResult(
                        lMatchIndex,
                        lMatchIndex + aInString.length(),
                        new String[] {aInString});
    }
}
