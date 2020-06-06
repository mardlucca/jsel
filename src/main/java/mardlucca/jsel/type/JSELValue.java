/*
 * File: JSELValue.java
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

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.JSELRuntimeException;

import java.util.List;

/**
 * This is the base class for the JSEL type system. All value types in the
 * language (including primitive and object types) extend from this.
 */
public abstract class JSELValue {
    @Override
    public boolean equals(Object aInValue) {
        if (this.getValue() == aInValue) {
            return true;
        }

        return aInValue instanceof JSELValue
                && equals((JSELValue) aInValue);
    }

    /**
     * This implements value equality as specified in
     * <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-11.9.1">
     *     ECMA-262, 5.1, Section-11.9.1</a> and
     * <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-11.9.3">
     *     ECMA-262, 5.1, Section-11.9.3</a>.
     * Different JSELValue subclasses implement their own appropriate subsection
     * in Section-11.9.3.
     * <p>As specified in Section 11.9.1, implementations of this must first
     * {@link #getValue() de-reference} the value passed in as an argument to
     * this as the argument may be a {@link JSELPropertyReference property
     * reference}.</p>
     *
     * @param aInValue the value we're comparing this value to
     * @return true if the two values are equals (but not necessarily strictly
     * equals).
     * @see #strictEquals(JSELValue)
     */
    public abstract boolean equals(JSELValue aInValue);

    /**
     * Returns this value's type.
     * @return this value's {@link Type}
     */
    public abstract Type getType();

    /**
     * Returns whether or not this value is a primitive.
     * @return true if this value is a primitive
     */
    public boolean isPrimitive() {
        return true;
    }

    /**
     * Returns whether or not this value is "callable". Only {@link JSELFunction
     * functions} are callable in JSEL (and Javascript).
     * @return true if this value is callable
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.11">
     * ECMA-262, 5.1, Section 9.11"</a>
     */
    public boolean isCallable() {
        return false;
    }

    /**
     * Returns whether or not this value can be coerced (or converted) to an
     * object. All types can be converted to an object in Javascript, except
     * <b>null</b> and <b>undefined</b>.
     * @return true if this value is coercible to object.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.10">
     * ECMA-262, 5.1, Section 9.10"</a>
     */
    public boolean isObjectCoercible() {
        return true;
    }

    /**
     * Objects in Javascript have an internal property called "[[Class]]". This
     * compares the given class with this values' class. Primitive types do not
     * have an object class, of course, so this default implementation always
     * returns false.
     *
     * @param aInString an object class to compare to this values' class.
     * @return true if this value has the same object class as the argument.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.6.2">
     * ECMA-262, 5.1, Section 8.6.2"</a>
     */
    public boolean isObjectClass(String aInString) {
        return false;
    }

    /**
     * This is a function invocation call. Only {@link JSELFunction} and its
     * subclasses implement this. This default implementation throws an
     * exception when invoked
     * @param aInThis the "this" value. This may potentially be a primitive
     *                value. Many library functions in Javascript can actually
     *                convert primitives to objects and use those as the "this"
     *                object
     * @param aInArguments the argument values for the function call.
     * @param aInExecutionContext the current execution context.
     * @return the invocation's return value.
     * @throws JSELRuntimeException the default implementation of this throws a
     * "TypeError".     */
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext) {
        throw JSELRuntimeException.typeError("cannot invoke object of type "
                + getType().name().toLowerCase());
    }

    /**
     * This can be called on values that are constructors to instantiate an
     * object. The default implemenation of this method always throws a
     * "TypeError".
     *
     * @param aInArguments the constructor arguments
     * @param aInExecutionContext the execution context
     * @return a newly instantiated JSELObject
     * @throws JSELRuntimeException if this JSELValue is not a constructor.
     */
    public JSELObject instantiate(List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext) {
        throw JSELRuntimeException.typeError(this + " is not a constructor");
    }

    /**
     * Used to find a match of this JSELValue inside an input string. This
     * implementation will convert this JSELValue into a {@link JSELString} and
     * will return the result of {@link JSELString#match(String, int)}.
     *
     * <p>Note that while in Javascript (ECMA 5.1) only regular expression
     * objects implement a [[Match]] operation, in JSEL it's possible to use any
     * value types to match against a string (though the syntax to execute such
     * operation may be non-trivial).</p>
     *
     * @param aInString a input string to match this JSELValue against.
     * @param aInIndex an index inside input string aInString to start the
     *                 matching execution from.
     * @return a {@link MatchResult} object indicating the result of the
     * matching process.
     */
    public MatchResult match(String aInString, int aInIndex) {
        return new JSELString(toString()).match(aInString, aInIndex);
    }

    /**
     * Returns whether or not this value is a reference type.
     *
     * @return true if this a reference type (e.g. {@link JSELPropertyReference}
     */
    public boolean isReference() {
        return false;
    }

    /**
     * On a {@link #isReference() reference} this returns the referenced value.
     * On a regular value this returns the value itself.
     * @return the de-referenced value.
     */
    public JSELValue getValue() {
        return this;
    }

    /**
     * This implements strict equality. Two values are strictly equal in JSEL
     * (and Javascript) if they are both references to the same instance
     * or if they are both primitives of the same type, with the same primitive
     * values.
     *
     * @param aInValue the value to compare. Note that if aInValue is a
     * {@link JSELPropertyReference}, the value is {@link #getValue()
     * de-referenced} before the comparison.
     * @return true if the input value is strictly equal to this value.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-11.9.4">
     * ECMA-262, 5.1, Section 11.9.4"</a>
     */
    public boolean strictEquals(JSELValue aInValue) {
        return aInValue.getValue() == this;
    }

    /**
     * Converts this JSELValue to a boolean
     * @return the boolean value representing this value.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.2">
     * ECMA-262, 5.1, Section 9.2"</a>
     */
    public abstract boolean toBoolean();

    /**
     * Converts this value to an Int32. This is very similar to
     * {@link #toInteger()}, except that NaN and Infinity values are converted
     * to zero.
     *
     * @return the Int32 value representing this value.
     * @throws JSELRuntimeException a TypeError if this value cannot be
     * converted {@link #toNumber() to a number}.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.5">
     * ECMA-262, 5.1, Section 9.5"</a>
     */
    public int toInt32() {
        return new JSELNumber(toNumber()).toInt32();
    }

    /**
     * Converts this value to an Integer. This is typically used before using
     * values as indexes to arrays in ECMAScript 5.1. If the number is too large
     * to be represented as an int, this will return either Integer.MAX_VALUE or
     * Integer.MIN_VALUE, depending on the sign of the value.
     *
     * <p>Note that the spec doesn't describe the size of this integral type. It
     * seems to assume that whatever underlying data type used to represent this
     * should be able to hold and arbitrarily sized Number. Since this method is
     * typically used to obtain index values for arrays and since array types in
     * Java are indexed by type int (i.e. 32 bits in Java), a decision was made
     * to also use a Java int to represent this.</p>
     *
     * @return the Integer value representing this value.
     * @throws JSELRuntimeException a TypeError if this value cannot be
     * converted {@link #toNumber() to a number}.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.4">
     * ECMA-262, 5.1, Section 9.4"</a>
     */
    public int toInteger() {
        return new JSELNumber(toNumber()).toInteger();
    }

    /**
     * Converts this value to a number. First this value is converted to a
     * primitive in case it is an object and then the primitive is converted to
     * a number.
     * @return a Java double representing the number
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.2">
     * ECMA-262, 5.1, Section 9.2"</a> to find out how the different value types
     * must be converted to a number.
     * */
    public abstract double toNumber();

    /**
     * Converts this value to a primitive. If this value is a primitive, this
     * will return the value itself. Otherwise this will convert the JSELObject
     * to a primitive depending on the given hint.
     * @param aInHint the hint to decide how to prioritize what primitive type
     *                to use.
     * @return this value as a primitive.
     * @throws JSELRuntimeException a TypeError if this value cannot be
     * converted to a primitive.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.1">
     * ECMA-262, 5.1, Section 9.1"</a>
     */
    public JSELValue toPrimitive(GetHint aInHint) {
        return this;
    }

    /**
     * Converts this value to an object. If this is an Object already, this
     * value is returned as is. Otherwise, primitive values are wrapped in their
     * respective wrappers, with the exception of "null" and "undefined".
     * @return the object representing this value.
     * @throws JSELRuntimeException a TypeError if this value cannot be
     * converted to an Object (which is the case with "null" and "undefined".
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.9">
     * ECMA-262, 5.1, Section 9.9"</a>
     */
    public abstract JSELObject toObject();

    /**
     * Converts this value to a UInt32.
     * @return the UInt32 value representing this value.
     * @throws JSELRuntimeException a TypeError if this value cannot be
     * converted {@link #toNumber() to a number}.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.6">
     * ECMA-262, 5.1, Section 9.6"</a>
     */
    public long toUInt32() {
        return new JSELNumber(toNumber()).toUInt32();
    }

    /**
     * This is a hint used when converting a JSELObject value to primitive.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.12.8">
     * ECMA-262, 5.1, Section 8.12.8"</a>
     */
    public enum GetHint {
        NUMBER, STRING
    }

    /**
     * This class is used to hold the results of a Match operation. This
     * contains the start and end indexes of the matching region as well as an
     * array containing all the captured values.
     */
    public static class MatchResult {
        private int start;
        private int end;
        private String[] captures;

        /**
         * Constructor
         * @param aInStart start of the match region
         * @param aInEnd end of the match region
         * @param aInCaptures captured values
         */
        public MatchResult(int aInStart, int aInEnd, String[] aInCaptures) {
            start = aInStart;
            end = aInEnd;
            captures = aInCaptures;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String[] getCaptures() {
            return captures;
        }
    }
}
