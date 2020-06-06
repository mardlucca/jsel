/*
 * File: JSELNumber.java
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

import mardlucca.jsel.type.wrapper.JSELNumberObject;
import mardlucca.jsel.util.DecimalFormat;

/**
 * This represents the number data type in JSEL. Numbers in ECMAScript 5.1 are
 * double precision values as specified by IEEE 754. This implementation uses a
 * Java double precision value, which is based on the same standard.
 */
public class JSELNumber extends JSELValue {
    /**
     * Constant for NaN JSELNumber
     */
    public static final JSELNumber NAN = new JSELNumber(Double.NaN);

    /**
     * Constant for positive infinity in JSEL.
     */
    public static final JSELNumber INFINITY =
            new JSELNumber(Double.POSITIVE_INFINITY);

    /**
     * The underlying double number.
     */
    private double number;

    /**
     * Constructor
     * @param aInNumber the underlying double value.
     */
    public JSELNumber(double aInNumber) {
        number = aInNumber;
    }

    @Override
    public Type getType() {
        return Type.NUMBER;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean toBoolean() {
        return !(number == 0 || Double.isNaN(number));
    }

    @Override
    public int toInt32() {
        if (Double.isNaN(number) || Double.isInfinite(number)) {
            return 0;
        }

        return (int) number;
    }

    @Override
    public int toInteger() {
        return toInteger(number);
    }

    @Override
    public double toNumber() {
        return number;
    }

    @Override
    public JSELObject toObject() {
        return new JSELNumberObject(this);
    }

    @Override
    public String toString() {
        return DecimalFormat.format(number);
    }

    @Override
    public long toUInt32() {
        return toUInt32(number);
    }

    @Override
    public boolean equals(JSELValue aInValue) {
        aInValue = aInValue.getValue();
        if (aInValue == this && !Double.isNaN(number)) {
            return true;
        }

        if (aInValue.getType() == Type.BOOLEAN
                || aInValue.getType() == Type.STRING) {
            return equals(new JSELNumber(aInValue.toNumber()));
        }

        if (aInValue.getType() == Type.NUMBER) {
            double lThatNumber = aInValue.toNumber();
            if (Double.isNaN(number) || Double.isNaN(lThatNumber)) {
                return false;
            }
            return number == lThatNumber;
        }

        if (aInValue.getType() == Type.OBJECT) {
            return equals(aInValue.toPrimitive(GetHint.NUMBER));
        }

        return false;
    }

    @Override
    public boolean strictEquals(JSELValue aInValue) {
        aInValue = aInValue.getValue();
        if (aInValue == this && !Double.isNaN(number)) {
            return true;
        }

        if (aInValue.getType() == Type.NUMBER) {
            return number == aInValue.toNumber();
        }

        return false;
    }

    /**
     * Converts this number to Java char value. This is equivalent to method
     * "ToUInt16" described in the specs.
     * @param aInNumber the number to convert
     * @return the converted character (which can be viewed as a 16-bit
     * character code)
     */
    public static char toChar(double aInNumber) {
        if (Double.isNaN(aInNumber) || Double.isInfinite(aInNumber)) {
            return 0;
        }

        return (char) ((int)aInNumber & 0xffff);
    }

    /**
     * Converts a double value to an integer. This is equivalent to method
     * "ToInteger" in the spec. Since "ToInteger" in the spec is typically used
     * to index arrays and since Java uses 32 bit ints to do the same thing, a
     * decision was made to use Java type "int" to represent the same thing even
     * though the spec implies that bigger numbers should be supported.
     *
     * @param aInNumber the number to convert
     * @return the converted 32 bit integer.
     */
    public static int toInteger(double aInNumber) {
        return (int) aInNumber;
    }

    /**
     * Converts a double value to an unsigned integer.
     * @param aInNumber the number to convert
     * @return the converted value.
     */
    public static long toUInt32(double aInNumber) {
        if (Double.isNaN(aInNumber) || Double.isInfinite(aInNumber)) {
            return 0;
        }

        return (long) aInNumber & 0xffffffffL;
    }
}
