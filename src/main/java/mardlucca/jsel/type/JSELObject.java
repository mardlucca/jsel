/*
 * File: JSELObject.java
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
import mardlucca.jsel.type.JSELValue.GetHint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static mardlucca.jsel.JSELRuntimeException.typeError;
import static mardlucca.jsel.builtin.object.ToStringFunction.TO_STRING;
import static mardlucca.jsel.builtin.object.ValueOfFunction.VALUE_OF;
import static java.lang.Double.isNaN;

/**
 * This represents an object in JSEL. All object types in JSEL extend from this.
 * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-4.2.1">
 * ECMA-262, 5.1, Section 4.2.1"</a>
 * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.6.2">
 * ECMA-262, 5.1, Section 8.6.2 - Object Internal Objects and Methods"</a>
 */
public class JSELObject extends JSELValue {
    /**
     * The value for internal property [[Class]] of type Object
     */
    public static final String CLASS = "Object";

    /**
     * The list of "own" properties contained in this object.
     */
    private Map<String, PropertyDescriptor> properties = new HashMap<>();

    /**
     * This object's [[Prototype]] internal
     */
    private JSELObject prototype;

    /**
     * Whether or not this object is extensible.
     */
    private boolean extensible = true;

    /**
     * Constructor.
     * Creates a new JSELObject using the {@link
     * mardlucca.jsel.builtin.object.ObjectPrototype} associated to this
     * thread's execution context.
     */
    public JSELObject() {
        this(ExecutionContext.getObjectPrototype());
    }

    /**
     * Constructor.
     * Creates a new JSELObject using the given prototype object passed in as an
     * argument.
     * @param aInPrototype a prototype object
     */
    protected JSELObject(JSELObject aInPrototype) {
        prototype = aInPrototype;
    }

    /**
     * Returns this object's object class (referred to as internal property
     * "[[Class]]" in the spec).
     * @return This object's object class.
     */
    public String getObjectClass() {
        return CLASS;
    }

    /**
     * Returns this object's prototype object (referred to as internal property
     * [[Prototype]] in the spec). Both in JSEL and ECMAScript 5.1 the only
     * object in the language to not have an internal [[Prototype]] property is
     * {@link mardlucca.jsel.builtin.object.ObjectPrototype Object.prototype}
     *
     * @return this Object's prototype.
     */
    public JSELObject getPrototype() {
        return prototype;
    }

    @Override
    public Type getType() {
        return Type.OBJECT;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isObjectClass(String aInString) {
        return getObjectClass().equals(aInString);
    }

    /**
     * Returns true if this object is extensible. See internal property
     * [[Extensible]] in the spec.
     *
     * @return true if this object is extensible.
     */
    public boolean isExtensible() {
        return extensible;
    }

    public void setExtensible(boolean aInExtensible) {
        extensible = aInExtensible;
    }

    @Override
    public boolean toBoolean() {
        return true;
    }

    @Override
    public JSELObject toObject() {
        return this;
    }

    @Override
    public double toNumber() {
        return toPrimitive(GetHint.NUMBER).toNumber();
    }

    @Override
    public String toString() {
        return toPrimitive(GetHint.STRING).toString();
    }

    /**
     * This implements the [[DefaultValue]] internal method, which is used to
     * convert objects to primitives.
     * @param aInHint the hint to use in the conversion. Defaults to
     *                GetHint.NUMBER, as per spec.
     * @return the converted value
     * @throws mardlucca.jsel.JSELRuntimeException a TypeError if this object
     * does not have a default value (i.e. cannot be converted to a primitive).
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.12.8">
     * ECMA-262, 5.1, Section 8.12.8"</a>
     */
    protected JSELValue defaultValue(GetHint aInHint) {
        JSELValue lCandidate;

        if (aInHint == GetHint.STRING) {
            // we try string conversion first
            lCandidate = defaultStringValue();
            if (lCandidate != null && lCandidate.isPrimitive()) {
                return lCandidate;
            }
            // we try number conversion next
            lCandidate = defaultNumberValue();
            if (lCandidate != null && lCandidate.isPrimitive()) {
                return lCandidate;
            }
        }
        else {
            // we try number conversion next
            lCandidate = defaultNumberValue();
            if (lCandidate != null && lCandidate.isPrimitive()) {
                return lCandidate;
            }
            // we try string conversion next
            lCandidate = defaultStringValue();
            if (lCandidate != null && lCandidate.isPrimitive()) {
                return lCandidate;
            }
        }

        throw typeError("Cannot convert object to primitive value");
    }

    /**
     * Retrieves a String representation of this value, if one exists. This uses
     * the "toString" method in this object, if it exists and can be invoked.
     * @return the string representation of this value or null if it does not
     * exist.
     */
    private JSELValue defaultStringValue() {
        JSELValue lToStringFnObject = get(TO_STRING);
        if (!lToStringFnObject.isCallable()) {
            return null;
        }
        JSELFunction lToStringFn = (JSELFunction) lToStringFnObject;
        return lToStringFn.call(this, Collections.emptyList(),
                ExecutionContext.getThreadContext());
    }

    /**
     * Retrieves a Number representation of this value, if one exists. This uses
     * the "valueOf" method in this object, if it exists and can be invoked.
     * @return the Number representation of this value or null if it does not
     * exit
     */
    private JSELValue defaultNumberValue() {
        JSELValue lValueOfFnObject = get(VALUE_OF);
        if (!lValueOfFnObject.isCallable()) {
            return null;
        }
        JSELFunction lValueOfFn = (JSELFunction) lValueOfFnObject;
        return lValueOfFn.call(this, Collections.emptyList(),
                ExecutionContext.getThreadContext());
    }

    @Override
    public JSELValue toPrimitive(GetHint aInHint) {
        return defaultValue(aInHint);
    }

    /**
     * Compares this object to another JSELValue.
     * @param aInObject the JSELValue to compare this to.
     * @return true if the two values are equal.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-11.9.4">
     * ECMA-262, 5.1, Section 11.9.3, sub-sections 8 and 9"</a>
     */
    @Override
    public boolean equals(JSELValue aInObject) {
        aInObject = aInObject.getValue();
        if (aInObject == this) {
            return true;
        }

        if (aInObject.getType() == Type.BOOLEAN) {
            return equals(new JSELNumber(aInObject.toNumber()));
        }

        if (aInObject.getType() == Type.STRING
                || aInObject.getType() == Type.NUMBER) {
            return toPrimitive(null).equals(aInObject);
        }

        // all other types of objects are different, we don't allow them to
        // provide an implementation
        return false;
    }

    /**
     * Returns a property owned by this object (i.e. this does not go down the
     * prototype chain)
     * @param aInProperty the property to look for
     * @return the {@link PropertyDescriptor} of the property or null if does
     * not exist.
     */
    public PropertyDescriptor getOwnProperty(String aInProperty) {
        return properties.get(aInProperty);
    }

    /**
     * Returns a property from this object. If the property does not exist
     * locally, this will go down the prototype chain to search for it.
     *
     * @param aInProperty the property to look for
     * @return the {@link PropertyDescriptor} of the property or null if does
     * not exist.
     */
    public PropertyDescriptor getProperty(String aInProperty) {
        JSELObject lObject = this;
        do {
            PropertyDescriptor lProperty = lObject.getOwnProperty(aInProperty);
            if (lProperty != null) {
                return lProperty;
            }

            lObject = lObject.prototype;
        }
        while (lObject != null);

        return null;
    }

    /**
     * This is the same as {@link #getOwnProperty(String)}, but it will return
     * the value rather than the descriptor.
     * @param aInProperty the property to look for
     * @return the value for the property or {@link JSELUndefined#getInstance()}
     * if the property does not exist.
     */
    public JSELValue getOwn(String aInProperty) {
        PropertyDescriptor lPropertyDescriptor = getOwnProperty(aInProperty);
        return lPropertyDescriptor == null 
                ? JSELUndefined.getInstance()
                : lPropertyDescriptor.getValue();
    }

    /**
     * Returns the names of all the properties in this object, regardless of
     * whether the property is enumerable or not.
     * @return the property names.
     */
    public Set<String> getOwnPropertyNames() {
        return properties.keySet();
    }

    /**
     * This is the same as {@link #getProperty(String)}, but it will return
     * the value rather than the descriptor.
     * @param aInProperty the property to look for
     * @return the value for the property or {@link JSELUndefined#getInstance()}
     * if the property does not exist.
     */
    public JSELValue get(String aInProperty) {
        PropertyDescriptor lPropertyDescriptor = getProperty(aInProperty);
        return lPropertyDescriptor == null
                ? JSELUndefined.getInstance()
                : lPropertyDescriptor.getValue();
    }

    /**
     * This is the same as {@link #getProperty(String)}, but it will return
     * the value rather than the descriptor.
     * @param aInProperty the property to look for
     * @return the value for the property or {@link JSELUndefined#getInstance()}
     * if the property does not exist.
     */
    public JSELValue get(JSELValue aInProperty) {
        return get(aInProperty.toString());
    }

    /**
     * Deletes a property like {@link #delete(String, boolean)}, using "throws"
     * behavior.
     * @param aInProperty the property to delete.
     * @return true if the property was deleted or did not exist.
     * @throws mardlucca.jsel.JSELRuntimeException if the property exists but
     * cannot be deleted.
     */
    public boolean delete(String aInProperty) {
        return delete(aInProperty, true);
    }

    /**
     * Deletes a property from the object, if possible.
     * @param aInProperty property to delete.
     * @param aInThrow if true, this will raise a "TypeError" if the property
     *                 cannot be deleted. If false, this method will return
     *                 "false" rather than throw.
     * @return true if the property can be deleted or if it does not exist,
     * which is also considered "ok". This will return "false" if the property
     * does exist but cannot be removed and parameter aInThrow is false.
     * @throws mardlucca.jsel.JSELRuntimeException a "TypeError" if the property
     * exists but cannot be removed and "aInThrow" is true.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.12.7">
     * ECMA-262, 5.1, Section 8.12.7"</a>
     */
    public boolean delete(String aInProperty, boolean aInThrow) {
        PropertyDescriptor lDescriptor = getOwnProperty(aInProperty);
        if (lDescriptor == null) {
            // this is as per spec
            return true;
        }

        if (lDescriptor.isConfigurable()) {
            properties.remove(aInProperty);
            return true;
        }

        return reject(aInThrow, "Cannot remove property " + aInProperty);
    }

    /**
     * Puts a property in this object.
     * @param aInProperty the property to put.
     * @param aInValue the value.
     * @throws mardlucca.jsel.JSELRuntimeException a TypeError if the property
     * cannot be added to this object.
     */
    public void put(JSELValue aInProperty, JSELValue aInValue) {
        put(aInProperty.toString(), aInValue, true);
    }

    /**
     * Puts a property in this object.
     * @param aInProperty the property to put.
     * @param aInValue the value.
     * @throws mardlucca.jsel.JSELRuntimeException a TypeError if the property
     * cannot be added to this object.
     */
    public void put(String aInProperty, JSELValue aInValue) {
        put(aInProperty, aInValue, true);
    }

    /**
     * Puts a property in this object.
     * @param aInProperty the property to put.
     * @param aInValue the value.
     * @param aInThrow whether to throw an exception or return false in case a
     *                 property cannot be put into this object.
     * @throws mardlucca.jsel.JSELRuntimeException a TypeError if the property
     * cannot be added to this object and aInThrow is true
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.12.5">
     * ECMA-262, 5.1, Section 8.12.5"</a>
     */
    public void put(JSELValue aInProperty, JSELValue aInValue, boolean aInThrow) {
        put(aInProperty.toString(), aInValue, aInThrow);
    }

    /**
     * Puts a property in this object.
     * @param aInProperty the property to put.
     * @param aInValue the value.
     * @param aInThrow whether to throw an exception or return false in case a
     *                 property cannot be put into this object.
     * @throws mardlucca.jsel.JSELRuntimeException a TypeError if the property
     * cannot be added to this object and aInThrow is true
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.12.5">
     * ECMA-262, 5.1, Section 8.12.5"</a>
     */
    public void put(String aInProperty, JSELValue aInValue, boolean aInThrow) {
        if (!canPut(aInProperty)) {
            reject(aInThrow, "Cannot add or change property " + aInProperty);
            return;
        }

        PropertyDescriptor lOwnProperty = getOwnProperty(aInProperty);
        if (lOwnProperty != null) {
            // property already there and we can put, so we just replace value
            defineOwnProperty(aInProperty, aInValue,
                    null, null, null, aInThrow);
            return;
        }

        // no own property yet so we create a new one here.
        defineOwnProperty(aInProperty, aInValue, true, true, true, aInThrow);
    }

    /**
     * Checks to see if a property with a given name can be put into this
     * Object.
     * @param aInProperty the property to put.
     * @return true if it can be put.
     */
    private boolean canPut(String aInProperty) {
        PropertyDescriptor lDescriptor = getOwnProperty(aInProperty);
        if (lDescriptor != null) {
            return lDescriptor.isWritable();
        }

        if (prototype == null) {
            // ObjectPrototype is the only object to ever not have a prototype
            // the spec allows users to "seal" objects, this is why this is
            // checked
            return extensible;
        }

        PropertyDescriptor lInherited = prototype.getProperty(aInProperty);
        if (lInherited == null) {
            return extensible;
        }

        return extensible && lInherited.writable;
    }

    /**
     * Checks to see if this object contains a given property, without going
     * down the prototype chain.
     * @param aInProperty the property to check.
     * @return true if the object contains the property
     */
    public boolean hasOwnProperty(String aInProperty) {
        return properties.containsKey(aInProperty);
    }

    /**
     * Checks to see if this object contains a given property. This will go down
     * the prototype chain if the local object does not contain the property.
     * @param aInProperty the property to check.
     * @return true if the object contains the property
     */
    public boolean hasProperty(String aInProperty) {
        JSELObject lObject = this;
        do {
            if (lObject.hasOwnProperty(aInProperty)) {
                return true;
            }

            lObject = lObject.prototype;
        }
        while (lObject != null);

        return false;
    }

    /**
     * Checks to see if this object contains a given property. This will go down
     * the prototype chain if the local object does not contain the property.
     * @param aInProperty the property to check.
     * @return true if the object contains the property
     */
    public boolean hasProperty(JSELValue aInProperty) {
        return hasProperty(aInProperty.toString());
    }

    /**
     * Defines or updates a property value in an object.
     * @param aInProperty the property to add
     * @param aInValue the value
     * @param aInEnumerable whether or not the property is enumerable. If null
     *                      then the property is not updated.
     * @param aInWritable whether or not the property is writable. If null then
     *                    the property is not updated.
     * @param aInConfigurable whether or not the property is configurable. If
     *                        null then the property is not updated.
     * @throws mardlucca.jsel.JSELRuntimeException a TypeError if the property
     * cannot be defined.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.12.9">
     * ECMA-262, 5.1, Section 8.12.9"</a>
     */
    public void defineOwnProperty(
            String aInProperty,
            JSELValue aInValue,
            Boolean aInEnumerable,
            Boolean aInWritable,
            Boolean aInConfigurable) {
        defineOwnProperty(aInProperty, aInValue, aInEnumerable,
                aInWritable, aInConfigurable, true);
    }

    /**
     * Defines or updates a property value in an object.
     * @param aInProperty the property to add
     * @param aInValue the value
     * @param aInEnumerable whether or not the property is enumerable. If null
     *                      then the property is not updated or will default to
     *                      false on creation.
     * @param aInWritable whether or not the property is writable. If null then
     *                    the property is not updated or will default to
     *      *                      false on creation.
     * @param aInConfigurable whether or not the property is configurable. If
     *                        null then the property is not updated or will
     *                        default to false on creation.
     * @param aInThrow if true this will cause a TypeError to be raised when the
     *                 property cannot be added. If this is false, this method
     *                 will return false in this case.
     * @return true if the property can be defined and false if it cannot be
     * defined and aInThrow is false
     * @throws mardlucca.jsel.JSELRuntimeException a TypeError if the property
     * cannot be defined and aInThrow is true.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.12.9">
     * ECMA-262, 5.1, Section 8.12.9"</a>
     */
    public boolean defineOwnProperty(
            String aInProperty,
            JSELValue aInValue,
            Boolean aInEnumerable,
            Boolean aInWritable,
            Boolean aInConfigurable,
            boolean aInThrow) {
        PropertyDescriptor lCurrent = getOwnProperty(aInProperty);
        if (lCurrent == null && !extensible) {
            return reject(aInThrow, "Cannot define property " + aInProperty
                    +", object is not extensible");
        }
        else if (lCurrent == null) {
            // object is extensible, so we're good. Javascript does not compare
            // against property definitions at the prototype level here.
            properties.put(aInProperty, new PropertyDescriptor(
                    aInValue == null ?
                            JSELUndefined.getInstance() :
                            aInValue.getValue(),        // possibly de-reference
                    aInEnumerable == null ? false : aInEnumerable,
                    aInWritable == null ? false : aInWritable,
                    aInConfigurable == null ? false : aInConfigurable));
            return true;
        }

        if (!lCurrent.isConfigurable()) {
            if ((aInConfigurable != null && aInConfigurable)
                    || aInEnumerable != null
                            && (aInEnumerable != lCurrent.isEnumerable())
                    || (aInWritable != null && aInWritable
                            && !lCurrent.isWritable())) {
                // trying to change a configuration value (i.e. configurable,
                // enumerable and/or writable) in an object that is not
                // configurable. Note that while it is possible to make a
                // writable property not writable, the opposite is not valid.
                return reject(aInThrow, "Cannot redefine property "
                        + aInProperty);
            }

            if (aInValue != null && !lCurrent.isWritable() && !sameValue(
                    lCurrent.getValue(), aInValue)) {
                // trying to write a different value on an object that is not
                //   configurable and not writable.
                return reject(aInThrow, "Cannot redefine property "
                        + aInProperty);
            }
        }
        // else configurable is true so defining own property is allowed even
        //   if lCurrent.writable is false (because they can achieve the same
        //   result by setting writable to true first, changing the value and
        //   then setting writable back to false).

        if (aInValue != null) {
            lCurrent.value = aInValue;
        }
        if (aInEnumerable != null) {
            lCurrent.enumerable = aInEnumerable;
        }
        if (aInWritable != null) {
            lCurrent.writable = aInWritable;
        }
        if (aInConfigurable != null) {
            lCurrent.configurable = aInConfigurable;
        }

        return true;
    }

    /**
     * Utility method to reject an operatoin. This will return false if aInThrow
     * is false or will raise a TypeError with the given error message if
     * aInThrow is true.
     * @param aInThrow true to throw an exception, false to return false.
     * @param aInMessage the message to go along with the exception
     * @return false if aInThrow is false
     * @throws mardlucca.jsel.JSELRuntimeException a TypeError with the given
     * message if "aInThrow" is true.
     */
    static boolean reject(boolean aInThrow, String aInMessage) {
        if (aInThrow) {
            throw typeError(aInMessage);
        }
        return false;
    }

    /**
     * This implements the [[SameValue]] internal method of type object. Note
     * that this is slightly different than the '===' (string equals) operator.
     * @param aInValue1 the first value to compare
     * @param aInValue2 the second value to compare
     * @return true if the two values are considered the same.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-9.12">
     * ECMA-262, 5.1, Section 9.12"</a>
     */
    static boolean sameValue(JSELValue aInValue1, JSELValue aInValue2) {
        if (aInValue1 == aInValue2) {
            return true;
        }

        Type lType1 = aInValue1.getType();
        if (lType1 != aInValue2.getType()) {
            return false;
        }
        if (lType1 == Type.NULL || lType1 == Type.UNDEFINED) {
            return true;
        }
        if (lType1 == Type.NUMBER) {
            double lN1 = aInValue1.toNumber();
            double lN2 = aInValue2.toNumber();
            return (isNaN(lN1) && isNaN(lN1)) ||
                    new Double(lN1).equals(new Double(lN2));
            // the spec differentiates 0 from -0. Using Double.equals instead
            // of the == operator produces the desired effect.
        }
        if (lType1 == Type.STRING) {
            return aInValue1.toString().equals(aInValue2.toString());
        }
        if (lType1 == Type.BOOLEAN) {
            return aInValue1.toBoolean() == aInValue2.toBoolean();
        }
        return false;
    }

    /**
     * Property descriptor for properties owned by objects
     */
    public static class PropertyDescriptor {
        private JSELValue value;
        private boolean enumerable;
        private boolean writable;
        private boolean configurable;

        public PropertyDescriptor(
                JSELValue aInValue,
                boolean aInEnumerable,
                boolean aInWritable,
                boolean aInConfigurable) {
            value = aInValue;
            enumerable = aInEnumerable;
            writable = aInWritable;
            configurable = aInConfigurable;
        }

        public JSELValue getValue() {
            return value;
        }

        public void setValue(JSELValue aInValue) {
            value = aInValue;
        }

        public boolean isEnumerable() {
            return enumerable;
        }

        public boolean isWritable() {
            return writable;
        }

        public boolean isConfigurable() {
            return configurable;
        }
    }
}
