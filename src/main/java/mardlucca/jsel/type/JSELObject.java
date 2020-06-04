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
import mardlucca.jsel.JSELRuntimeException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static mardlucca.jsel.JSELRuntimeException.typeError;
import static mardlucca.jsel.builtin.object.ToStringFunction.TO_STRING;
import static mardlucca.jsel.builtin.object.ValueOfFunction.VALUE_OF;
import static java.lang.Double.isNaN;

public class JSELObject extends JSELValue
{
    public static final String CLASS = "Object";

    private Map<String, PropertyDescriptor> properties = new HashMap<>();

    private JSELObject prototype;
    
    private boolean extensible = true;

    public JSELObject()
    {
        this(ExecutionContext.getObjectPrototype());
    }

    protected JSELObject(JSELObject aInPrototype)
    {
        prototype = aInPrototype;
    }

    public String getObjectClass()
    {
        return CLASS;
    }

    public JSELObject getPrototype()
    {
        return prototype;
    }

    @Override
    public Type getType()
    {
        return Type.OBJECT;
    }

    @Override
    public boolean isPrimitive()
    {
        return false;
    }

    @Override
    public boolean isObjectClass(String aInString)
    {
        return getObjectClass().equals(aInString);
    }

    public JSELValue getPrimitiveValue()
    {
        return null;
    }

    public boolean isExtensible()
    {
        return extensible;
    }

    public void setExtensible(boolean aInExtensible)
    {
        extensible = aInExtensible;
    }

    @Override
    public boolean toBoolean()
    {
        return true;
    }

    @Override
    public JSELObject toObject()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return toPrimitive(GetHint.STRING).toString();
    }

    protected JSELValue defaultValue(GetHint aInHint)
    {
        JSELValue lCandidate;

        if (aInHint == GetHint.STRING)
        {
            // we try string conversion first
            lCandidate = defaultStringValue();
            if (lCandidate != null && lCandidate.isPrimitive())
            {
                return lCandidate;
            }
            lCandidate = defaultNumberValue();
            if (lCandidate != null && lCandidate.isPrimitive())
            {
                return lCandidate;
            }
        }

        // we try valueOf conversion first
        lCandidate = defaultNumberValue();
        if (lCandidate != null && lCandidate.isPrimitive())
        {
            return lCandidate;
        }
        lCandidate = defaultStringValue();
        if (lCandidate != null && lCandidate.isPrimitive())
        {
            return lCandidate;
        }

        throw JSELRuntimeException.typeError("Cannot convert object to primitive value");
    }

    private JSELValue defaultStringValue()
    {
        JSELValue lToStringFnObject = get(TO_STRING);
        if (!lToStringFnObject.isCallable())
        {
            return null;
        }
        JSELFunction lToStringFn = (JSELFunction) lToStringFnObject;
        return lToStringFn.call(this, Collections.emptyList(),
                ExecutionContext.getThreadContext());
    }

    private JSELValue defaultNumberValue()
    {
        JSELValue lValueOfFnObject = get(VALUE_OF);
        if (!lValueOfFnObject.isCallable())
        {
            return null;
        }
        JSELFunction lValueOfFn = (JSELFunction) lValueOfFnObject;
        return lValueOfFn.call(this, Collections.emptyList(),
                ExecutionContext.getThreadContext());
    }

    @Override
    public JSELValue toPrimitive(JSELValue.GetHint aInHint)
    {
        return defaultValue(aInHint);
    }

    @Override
    public boolean equals(JSELValue aInObject)
    {
        aInObject = aInObject.getValue();
        if (aInObject == this)
        {
            return true;
        }

        if (aInObject.getType() == Type.BOOLEAN)
        {
            return equals(new JSELNumber(aInObject.toNumber()));
        }

        if (aInObject.getType() == Type.STRING
                || aInObject.getType() == Type.NUMBER)
        {
            return toPrimitive(null).equals(aInObject);
        }

        // all other types of objects are different, we don't allow them to
        // provide an implementation
        return false;
    }

    public PropertyDescriptor getOwnProperty(String aInProperty)
    {
        return properties.get(aInProperty);
    }

    public PropertyDescriptor getProperty(String aInProperty)
    {
        JSELObject lObject = this;
        do
        {
            PropertyDescriptor lProperty = lObject.getOwnProperty(aInProperty);
            if (lProperty != null)
            {
                return lProperty;
            }

            lObject = lObject.prototype;
        }
        while (lObject != null);

        return null;
    }
    
    public JSELValue getOwn(String aInProperty)
    {
        PropertyDescriptor lPropertyDescriptor = getOwnProperty(aInProperty);
        return lPropertyDescriptor == null 
                ? JSELUndefined.getInstance()
                : lPropertyDescriptor.getValue();
    }

    public Set<String> getOwnPropertyNames()
    {
        return properties.keySet();
    }

    public JSELValue get(String aInProperty)
    {
        PropertyDescriptor lPropertyDescriptor = getProperty(aInProperty);
        return lPropertyDescriptor == null
                ? JSELUndefined.getInstance()
                : lPropertyDescriptor.getValue();
    }

    public JSELValue get(JSELValue aInProperty)
    {
        return get(aInProperty.toString());
    }

    public boolean delete(String aInProperty)
    {
        return delete(aInProperty, true);
    }

    public boolean delete(String aInProperty, boolean aInThrow)
    {
        PropertyDescriptor lDescriptor = getOwnProperty(aInProperty);
        if (lDescriptor == null)
        {
            return true;
        }

        if (lDescriptor.isConfigurable())
        {
            properties.remove(aInProperty);
            return true;
        }

        return reject(aInThrow, "Cannot remove property " + aInProperty);
    }

    public void put(JSELValue aInProperty, JSELValue aInValue)
    {
        put(aInProperty.toString(), aInValue, true);
    }

    public void put(String aInProperty, JSELValue aInValue)
    {
        put(aInProperty, aInValue, true);
    }

    public void put(JSELValue aInProperty, JSELValue aInValue, boolean aInThrow)
    {
        put(aInProperty.toString(), aInValue, aInThrow);
    }

    public void put(String aInProperty, JSELValue aInValue, boolean aInThrow)
    {
        if (!canPut(aInProperty))
        {
            reject(aInThrow, "Cannot add or change property " + aInProperty);
            return;
        }

        PropertyDescriptor lOwnProperty = getOwnProperty(aInProperty);
        if (lOwnProperty != null)
        {
            // property already there and we can put, so we just replace value
            defineOwnProperty(aInProperty, aInValue,
                    null, null, null, aInThrow);
            return;
        }

        // no own property yet so we create a new one here.
        defineOwnProperty(aInProperty, aInValue, true, true, true, aInThrow);
    }

    protected boolean canPut(String aInProperty)
    {
        PropertyDescriptor lDescriptor = getOwnProperty(aInProperty);
        if (lDescriptor != null)
        {
            return lDescriptor.isWritable();
        }

        if (prototype == null)
        {
            // ObjectPrototype is the only object to ever not have a prototype
            // the spec allows users to "seal" objects, this is why this is
            // checked
            return extensible;
        }

        PropertyDescriptor lInherited = prototype.getProperty(aInProperty);
        if (lInherited == null)
        {
            return extensible;
        }

        return extensible && lInherited.writable;
    }

    public boolean hasOwnProperty(String aInProperty)
    {
        return properties.containsKey(aInProperty);
    }

    public boolean hasProperty(String aInProperty)
    {
        JSELObject lObject = this;
        do
        {
            if (lObject.hasOwnProperty(aInProperty))
            {
                return true;
            }

            lObject = lObject.prototype;
        }
        while (lObject != null);

        return false;
    }

    public boolean hasProperty(JSELValue aInProperty)
    {
        return hasProperty(aInProperty.toString());
    }

    public boolean defineOwnProperty(
            String aInProperty,
            JSELValue aInValue,
            Boolean aInEnumerable,
            Boolean aInWritable,
            Boolean aInConfigurable)
    {
        return defineOwnProperty(aInProperty, aInValue, aInEnumerable,
                aInWritable, aInConfigurable, true);
    }

    public boolean defineOwnProperty(
            String aInProperty,
            JSELValue aInValue,
            Boolean aInEnumerable,
            Boolean aInWritable,
            Boolean aInConfigurable,
            boolean aInThrow)
    {
        PropertyDescriptor lCurrent = getOwnProperty(aInProperty);
        if (lCurrent == null && !extensible)
        {
            return reject(aInThrow, "Cannot define property " + aInProperty
                    +", object is not extensible");
        }
        else if (lCurrent == null)
        {
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

        if (!lCurrent.isConfigurable())
        {
            if ((aInConfigurable != null && aInConfigurable)
                    || aInEnumerable != null
                            && (aInEnumerable != lCurrent.isEnumerable())
                    || (aInWritable != null && aInWritable
                            && !lCurrent.isWritable()))
            {
                // trying to configure an object that is not configurable
                return reject(aInThrow, "Cannot redefine property "
                        + aInProperty);
            }

            if (aInValue != null && !lCurrent.isWritable() && !sameValue(
                    lCurrent.getValue(), aInValue))
            {
                // trying to write a different value on an object that is not
                //   configurable and not writable.
                return reject(aInThrow, "Cannot redefine property "
                        + aInProperty);
            }
        }
        // else configurable is true so defining own property is allowed even
        //   if lCurrent.writable is false (because they can achieve the same
        //   result by setting writable to true first, changing the value and
        //   then set writable back to false).

        if (aInValue != null)
        {
            lCurrent.value = aInValue;
        }
        if (aInEnumerable != null)
        {
            lCurrent.enumerable = aInEnumerable;
        }
        if (aInWritable != null)
        {
            lCurrent.writable = aInWritable;
        }
        if (aInConfigurable != null)
        {
            lCurrent.configurable = aInConfigurable;
        }

        return true;
    }

    static boolean reject(boolean aInThrow, String aInMessage)
    {
        if (aInThrow)
        {
            throw JSELRuntimeException.typeError(aInMessage);
        }
        return false;
    }

    static boolean sameValue(JSELValue aInValue1, JSELValue aInValue2)
    {
        if (aInValue1 == aInValue2)
        {
            return true;
        }

        Type lType1 = aInValue1.getType();
        if (lType1 != aInValue2.getType())
        {
            return false;
        }
        if (lType1 == Type.NULL || lType1 == Type.UNDEFINED)
        {
            return true;
        }
        if (lType1 == Type.NUMBER)
        {
            double lN1 = aInValue1.toNumber();
            double lN2 = aInValue2.toNumber();
            return (isNaN(lN1) && isNaN(lN1)) ||
                    new Double(lN1).equals(new Double(lN2));
            // the spec differentiantes 0 from -0. Using Double.equals instead
            // of the == operator produces the desired effect.
        }
        if (lType1 == Type.STRING)
        {
            return aInValue1.toString().equals(aInValue2.toString());
        }
        if (lType1 == Type.BOOLEAN)
        {
            return aInValue1.toBoolean() == aInValue2.toBoolean();
        }
        return false;
    }

    public static class PropertyDescriptor
    {
        private JSELValue value;
        private boolean enumerable;
        private boolean writable;
        private boolean configurable;

        public PropertyDescriptor(
                JSELValue aInValue,
                boolean aInEnumerable,
                boolean aInWritable,
                boolean aInConfigurable)
        {
            value = aInValue;
            enumerable = aInEnumerable;
            writable = aInWritable;
            configurable = aInConfigurable;
        }

        public JSELValue getValue()
        {
            return value;
        }

        public void setValue(JSELValue aInValue)
        {
            value = aInValue;
        }

        public boolean isEnumerable()
        {
            return enumerable;
        }

        public boolean isWritable()
        {
            return writable;
        }

        public boolean isConfigurable()
        {
            return configurable;
        }
    }
}
