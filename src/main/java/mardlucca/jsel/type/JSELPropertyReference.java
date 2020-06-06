/*
 * File: JSELPropertyReference.java
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

import java.util.List;

/**
 * This class represents a property reference. All operations done to a
 * reference are delegated to the referenced value. Since JSEL cannot assign
 * values to properties, references in the language do not implement
 * [[PutValue]] internal method. Also, for the same reason, [[GetValue]] does
 * not need to deal to references whose base point to an Environment Record.
 */
public class JSELPropertyReference extends JSELValue {
    private JSELValue base;
    private String propertyName;
    private JSELValue value;

    /**
     * Constructor
     * @param aInBase the base object that contains the reference property
     * @param aInPropertyName the referenced property name
     */
    public JSELPropertyReference(
            JSELValue aInBase, String aInPropertyName) {
        base = aInBase.getValue();  // we don't want base to be a reference
        propertyName = aInPropertyName;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object aInValue) {
        return getValue().equals(aInValue);
    }

    @Override
    public boolean equals(JSELValue aInValue) {
        return getValue().equals(aInValue);
    }

    @Override
    public Type getType() {
        return getValue().getType();
    }

    @Override
    public boolean isPrimitive() {
        return getValue().isPrimitive();
    }

    @Override
    public boolean isCallable() {
        return getValue().isCallable();
    }

    @Override
    public boolean strictEquals(JSELValue aInValue) {
        return getValue().strictEquals(aInValue);
    }

    @Override
    public boolean toBoolean() {
        return getValue().toBoolean();
    }

    @Override
    public int toInt32() {
        return getValue().toInt32();
    }

    @Override
    public double toNumber() {
        return getValue().toNumber();
    }

    @Override
    public JSELValue toPrimitive(GetHint aInHint) {
        return getValue().toPrimitive(aInHint);
    }

    @Override
    public JSELObject toObject() {
        return getValue().toObject();
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public long toUInt32() {
        return getValue().toUInt32();
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext) {

        // if they call a function using "obj1.function()", then "aInThis" will
        // be null so this will call function "function" using "obj1" as the
        // "this" object. If they call the same function using
        // "obj1.function.call(obj2)" then "aInThis" will be "obj2", so this
        // will use "obj2" as the "this" object in the invocation.

        return getValue().call(
                aInThis.getType() == Type.NULL
                        || aInThis.getType() == Type.UNDEFINED
                        ? base.toObject()
                        : aInThis,
                aInArguments,
                aInExecutionContext);
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext) {
        return getValue().instantiate(aInArguments, aInExecutionContext);
    }

    @Override
    public boolean isObjectClass(String aInString) {
        return getValue().isObjectClass(aInString);
    }

    @Override
    public MatchResult match(String aInString, int aInIndex) {
        return getValue().match(aInString, aInIndex);
    }

    @Override
    public int toInteger() {
        return getValue().toInteger();
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public JSELValue getValue() {
        if (value == null) {
            value = base.toObject().get(propertyName);
        }
        return value;
    }
}
