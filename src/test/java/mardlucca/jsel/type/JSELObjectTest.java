/*
 * File: JSELObjectTest.java
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

import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.builtin.ConstantFunction;
import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.builtin.object.ToStringFunction;
import mardlucca.jsel.type.JSELObject.PropertyDescriptor;
import mardlucca.jsel.type.JSELValue.GetHint;
import mardlucca.jsel.type.wrapper.JSELStringObject;
import org.junit.Before;
import org.junit.Test;

import static mardlucca.jsel.type.JSELObject.sameValue;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class JSELObjectTest
{
    private JSELObject emptyObject = new JSELObject();
    private JSELObject objectWithNoValueOf = new JSELObject();
    private JSELObject objectWithNoToString = new JSELObject();
    private JSELObject objectWithValueOf0 = new JSELObject();
    private JSELObject objectWithValueOf1 = new JSELObject();
    private JSELObject objectWithValueOf2 = new JSELObject();
    private JSELObject objectWithValueOfNeg1 = new JSELObject();

    @Before
    public void setUp()
    {
        objectWithNoValueOf.put("valueOf", JSELNull.getInstance());
        objectWithNoValueOf.put("toString",
                new ConstantFunction(new JSELString("Value=null")));

        objectWithNoToString.put("valueOf",
                new ConstantFunction(new JSELNumber(10)));
        objectWithNoToString.put("toString", JSELNull.getInstance());

        objectWithValueOf0.put("valueOf",
                new ConstantFunction(new JSELNumber(0)));
        objectWithValueOf0.put("toString",
                new ConstantFunction(new JSELString("Value=0")));
        objectWithValueOf1.put("valueOf",
                new ConstantFunction(new JSELNumber(1)));
        objectWithValueOf1.put("toString",
                new ConstantFunction(new JSELString("Value=1")));
        objectWithValueOf2.put("valueOf",
                new ConstantFunction(new JSELNumber(2)));
        objectWithValueOf2.put("toString",
                new ConstantFunction(new JSELString("Value=2")));
        objectWithValueOfNeg1.put("valueOf",
                new ConstantFunction(new JSELNumber(-1)));
        objectWithValueOfNeg1.put("toString",
                new ConstantFunction(new JSELString("Value=-1")));
    }

    @Test
    public void equals()
    {
        assertFalse(emptyObject.equals(JSELBoolean.TRUE));
        assertFalse(emptyObject.equals(JSELBoolean.FALSE));
        assertTrue(objectWithValueOf0.equals(JSELBoolean.FALSE));
        assertFalse(objectWithValueOf0.equals(JSELBoolean.TRUE));
        assertFalse(objectWithValueOf1.equals(JSELBoolean.FALSE));
        assertTrue(objectWithValueOf1.equals(JSELBoolean.TRUE));
        assertFalse(objectWithValueOf2.equals(JSELBoolean.FALSE));
        assertFalse(objectWithValueOf2.equals(JSELBoolean.TRUE));

        assertFalse(emptyObject.equals(new JSELNumber(0)));
        assertTrue(objectWithValueOf0.equals(new JSELNumber(0)));
        assertFalse(objectWithValueOf0.equals(new JSELNumber(10)));
        assertTrue(objectWithValueOf1.equals(new JSELNumber(1)));
        assertTrue(objectWithValueOf2.equals(new JSELNumber(2)));

        assertFalse(emptyObject.equals(new JSELString("Value=0")));
        assertTrue(emptyObject.equals(new JSELString("[object Object]")));

        // false because "valueOf", which returns 0, takes precedence
        assertFalse(objectWithValueOf0.equals(new JSELString("Value=0")));

        assertTrue(emptyObject.equals(emptyObject));
        assertFalse(emptyObject.equals(new JSELObject()));
        assertFalse(new JSELObject().equals(new JSELObject()));
        assertTrue(emptyObject.equals(emptyObject.toObject()));

        JSELObject lSameValueOf = new JSELObject();
        lSameValueOf.put("valueOf", new ConstantFunction(new JSELNumber(0)));
        // false because two objects are always different in javascript
        assertFalse(objectWithValueOf0.equals(lSameValueOf));

        assertFalse(emptyObject.equals(JSELNull.getInstance()));

        assertFalse(emptyObject.equals(JSELUndefined.getInstance()));
    }

    @Test
    public void getType()
    {
        assertEquals(Type.OBJECT, emptyObject.getType());
    }

    @Test
    public void isPrimitive()
    {
        assertFalse(emptyObject.isPrimitive());
    }

    @Test
    public void getPrimitiveValue()
    {
        assertNull(emptyObject.getPrimitiveValue());
        assertNull(objectWithNoValueOf.getPrimitiveValue());
    }

    @Test
    public void isCallable()
    {
        assertFalse(emptyObject.isCallable());
    }

    @Test
    public void call()
    {
        try
        {
            emptyObject.call(emptyObject, null, null);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("cannot invoke object of type object",
                    e.getMessage());
        }
    }

    @Test
    public void strictEquals()
    {
        assertFalse(emptyObject.strictEquals(JSELBoolean.TRUE));
        assertFalse(emptyObject.strictEquals(JSELBoolean.FALSE));
        assertFalse(objectWithValueOf0.strictEquals(JSELBoolean.FALSE));
        assertFalse(objectWithValueOf0.strictEquals(JSELBoolean.TRUE));
        assertFalse(objectWithValueOf1.strictEquals(JSELBoolean.FALSE));
        assertFalse(objectWithValueOf1.strictEquals(JSELBoolean.TRUE));
        assertFalse(objectWithValueOf2.strictEquals(JSELBoolean.FALSE));
        assertFalse(objectWithValueOf2.strictEquals(JSELBoolean.TRUE));

        assertFalse(emptyObject.strictEquals(new JSELNumber(0)));
        assertFalse(objectWithValueOf0.strictEquals(new JSELNumber(0)));
        assertFalse(objectWithValueOf0.strictEquals(new JSELNumber(10)));
        assertFalse(objectWithValueOf1.strictEquals(new JSELNumber(1)));
        assertFalse(objectWithValueOf2.strictEquals(new JSELNumber(2)));

        assertFalse(emptyObject.strictEquals(new JSELString("Value=0")));
        assertFalse(emptyObject.strictEquals(
                new JSELString("[object Object]")));

        assertFalse(objectWithValueOf0.strictEquals(new JSELString("Value=0")));

        assertTrue(emptyObject.strictEquals(emptyObject));
        assertFalse(emptyObject.strictEquals(new JSELObject()));
        assertFalse(new JSELObject().strictEquals(new JSELObject()));
        assertTrue(emptyObject.strictEquals(emptyObject.toObject()));

        JSELObject lSameValueOf = new JSELObject();
        lSameValueOf.put("valueOf", new ConstantFunction(new JSELNumber(0)));

        assertFalse(objectWithValueOf0.strictEquals(lSameValueOf));

        assertFalse(emptyObject.strictEquals(JSELNull.getInstance()));

        assertFalse(emptyObject.strictEquals(JSELUndefined.getInstance()));
    }

    @Test
    public void toBoolean()
    {
        assertTrue(emptyObject.toBoolean());
    }

    @Test
    public void toInt32()
    {
        assertEquals(0, objectWithValueOf0.toInt32());
        assertEquals(1, objectWithValueOf1.toInt32());
        assertEquals(2, objectWithValueOf2.toInt32());
        assertEquals(0, emptyObject.toInt32());
    }

    @Test
    public void toNumber()
    {
        assertEquals(0, objectWithValueOf0.toNumber(), 0.0);
        assertEquals(1, objectWithValueOf1.toNumber(), 0.0);
        assertEquals(2, objectWithValueOf2.toNumber(), 0.0);
        assertEquals(Double.NaN, emptyObject.toNumber(), 0.0);
    }

    @Test
    public void toPrimitive()
    {
        assertEquals(objectWithValueOf2.toNumber(),
                objectWithValueOf2.toPrimitive(null).toNumber(),
                0.0);
        assertEquals(2,
                objectWithValueOf2.toPrimitive(GetHint.NUMBER).toNumber(),
                0.0);
        assertEquals("Value=2",
                objectWithValueOf2.toPrimitive(GetHint.STRING).toString());

        assertEquals("Value=null",
                objectWithNoValueOf.toPrimitive(null).toString());
        assertEquals("Value=null",
                objectWithNoValueOf.toPrimitive(GetHint.NUMBER).toString());

        assertEquals(10,
                objectWithNoToString.toPrimitive(null).toNumber(),
                0.0);
        assertEquals(10,
                objectWithNoToString.toPrimitive(GetHint.STRING).toNumber(),
                0.0);
    }

    @Test
    public void toPrimitiveFailure()
    {
        JSELObject lObject = new JSELObject();
        // changing toString to a number, which can't be called
        lObject.put("toString", new JSELNumber(2));
        lObject.put("valueOf", new JSELNumber(2));

        try
        {
            lObject.toPrimitive(null);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot convert object to primitive value",
                    e.getMessage());
        }
    }

    @Test
    public void toObject()
    {
        assertSame(emptyObject, emptyObject.toObject());
    }

    @Test
    public void testToString()
    {
        assertEquals("[object Object]", emptyObject.toString());
    }

    @Test
    public void toUInt32()
    {
        assertEquals(0, objectWithValueOf0.toUInt32());
        assertEquals(1, objectWithValueOf1.toUInt32());
        assertEquals(2, objectWithValueOf2.toUInt32());
        assertEquals(0xffffffffl, objectWithValueOfNeg1.toUInt32());
        assertEquals(0, emptyObject.toUInt32());
    }

    @Test
    public void getObjectClass()
    {
        assertEquals(JSELObject.CLASS, emptyObject.getObjectClass());
    }

    @Test
    public void propertiesTest()
    {
        JSELValue lKey = new JSELNumber(1);
        JSELValue lValue = new JSELString("value");

        assertFalse(emptyObject.hasProperty(lKey));
        emptyObject.put(lKey, lValue, true);
        assertSame(lValue, emptyObject.get(lKey));
        assertEquals(JSELUndefined.getInstance(),
                emptyObject.getPrototype().get(lKey));
        assertTrue(emptyObject.hasProperty(lKey));
        assertFalse(emptyObject.getPrototype().hasProperty(lKey.toString()));

        assertFalse(emptyObject.hasOwnProperty("toString"));
        assertEquals(JSELUndefined.getInstance(),
                emptyObject.getOwn("toString"));
        assertTrue(emptyObject.hasProperty("toString"));
        assertEquals(Type.FUNCTION, emptyObject.get("toString").getType());
    }

    @Test
    public void objectEqualsTest()
    {
        assertEquals(JSELString.EMPTY_STRING, new JSELString(""));
        assertEquals(JSELString.EMPTY_STRING, JSELString.EMPTY_STRING);
        assertNotEquals(JSELString.EMPTY_STRING, "");
    }

    @Test
    public void defineOwnPropertyAndDelete()
    {
        JSELObject lObject = new JSELObject();
        assertTrue(lObject.isExtensible());
        assertFalse(lObject.hasProperty("p1"));
        assertFalse(lObject.hasOwnProperty("p1"));
        lObject.defineOwnProperty("p1", JSELBoolean.TRUE, true, true, true);
        assertTrue(lObject.hasProperty("p1"));
        assertTrue(lObject.hasOwnProperty("p1"));
        assertEquals(JSELBoolean.TRUE, lObject.get("p1"));

        // redefining, not changing configuration
        lObject.defineOwnProperty("p1", JSELBoolean.FALSE, true, true, true);
        assertTrue(lObject.hasProperty("p1"));
        assertTrue(lObject.hasOwnProperty("p1"));
        assertEquals(JSELBoolean.FALSE, lObject.get("p1"));

        lObject.defineOwnProperty("p2", JSELNull.getInstance(),
                true, true, true);
        assertTrue(lObject.hasOwnProperty("p2"));
        assertTrue(lObject.getOwnPropertyNames()
                .containsAll(asList("p1", "p2")));

        assertTrue(lObject.delete("p1"));
        assertFalse(lObject.hasProperty("p1"));
        assertFalse(lObject.hasOwnProperty("p1"));
        assertEquals(1, lObject.getOwnPropertyNames().size());
    }

    @Test
    public void testWritable()
    {
        JSELObject lObject = new JSELObject();
        assertFalse(lObject.hasProperty("p1"));
        assertFalse(lObject.hasOwnProperty("p1"));
        lObject.defineOwnProperty("p1", JSELBoolean.TRUE, true, false, true);
        assertEquals(JSELBoolean.TRUE, lObject.get("p1"));
        lObject.put("p1", JSELBoolean.FALSE, false);
        assertEquals(JSELBoolean.TRUE, lObject.get("p1"));

        try
        {
            // putting a different value is a problem
            lObject.put("p1", JSELBoolean.FALSE);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot add or change property p1", e.getMessage());
        }

        // can't put but can redefine with different value
        lObject.defineOwnProperty("p1", JSELBoolean.FALSE, true, false, true);
        assertEquals(JSELBoolean.FALSE, lObject.get("p1"));

        assertTrue(lObject.delete("p1"));
        assertFalse(lObject.hasProperty("p1"));
        assertFalse(lObject.hasOwnProperty("p1"));
    }

    @Test
    public void testConfigurable()
    {
        JSELObject lObject = new JSELObject();
        assertFalse(lObject.hasProperty("p1"));
        assertFalse(lObject.hasOwnProperty("p1"));
        lObject.defineOwnProperty("p1", JSELBoolean.TRUE, true, true, false);
        // no change in configuration and property is writable. All good.
        lObject.defineOwnProperty("p1", JSELBoolean.FALSE, true, true, false);
        assertEquals(JSELBoolean.FALSE, lObject.get("p1"));

        lObject.defineOwnProperty("p2", JSELBoolean.TRUE, true, false, false);
        // object not writable or configurable, but value is the same,
        // so all good
        lObject.defineOwnProperty("p2", JSELBoolean.TRUE, true, false, false);

        try
        {
            // now we change the value, that's no good
            lObject.defineOwnProperty("p2",
                    JSELBoolean.FALSE, true, false, false);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot redefine property p2", e.getMessage());
        }

        lObject.defineOwnProperty("p3", JSELBoolean.TRUE, true, true, false);
        try
        {
            // trying to change configuration on a non-configurable object.
            lObject.defineOwnProperty("p3",
                    JSELBoolean.TRUE, false, true, false);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot redefine property p3", e.getMessage());
        }


        assertFalse(lObject.delete("p3", false));
        try
        {
            // trying to remove non-configurable object.
            lObject.delete("p3");
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot remove property p3", e.getMessage());
        }
    }

    @Test
    public void testDefineOwnPropretyWithDefaults()
    {
        JSELObject lObject = new ObjectPrototype();
        lObject.defineOwnProperty("p1", null, null, null, null, false);
        PropertyDescriptor lDescriptor = lObject.getOwnProperty("p1");
        assertSame(JSELUndefined.getInstance(), lDescriptor.getValue());
        assertFalse(lDescriptor.isConfigurable());
        assertFalse(lDescriptor.isEnumerable());
        assertFalse(lDescriptor.isWritable());

        lObject.put("p2", null);
        lDescriptor = lObject.getOwnProperty("p2");
        assertSame(JSELUndefined.getInstance(), lDescriptor.getValue());
        assertTrue(lDescriptor.isConfigurable());
        assertTrue(lDescriptor.isEnumerable());
        assertTrue(lDescriptor.isWritable());
    }


    @Test
    public void testDefinePropertyInSealedObjectPrototype()
    {
        JSELObject lObject = new ObjectPrototype();
        lObject.setExtensible(false);

        try
        {
            // trying to remove non-configurable object.
            lObject.put("p1", JSELBoolean.TRUE);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot add or change property p1", e.getMessage());
        }

        try
        {
            // trying to remove non-configurable object.
            lObject.defineOwnProperty("p1", null, false, false, false, true);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot define property p1, object is not extensible",
                    e.getMessage());
        }

    }

    @Test
    public void testDeleteProperty()
    {
        JSELObject lPrototype = new JSELObject();
        JSELObject lObject = new JSELObject(lPrototype);

        lPrototype.defineOwnProperty("p1", JSELBoolean.TRUE, true, true, false);
        assertEquals(JSELBoolean.TRUE, lPrototype.getOwn("p1"));
        assertTrue(lObject.hasProperty("p1"));
        assertFalse(lObject.hasOwnProperty("p1"));
        assertEquals(JSELUndefined.getInstance(), lObject.getOwn("p1"));
        assertEquals(JSELBoolean.TRUE, lObject.get("p1"));

        // even if not configurable in prototype you can still change
        // configuration in own
        lObject.defineOwnProperty("p1", JSELBoolean.FALSE, false, false, true);
        assertTrue(lObject.hasProperty("p1"));
        assertTrue(lObject.hasOwnProperty("p1"));
        assertEquals(JSELBoolean.FALSE, lObject.get("p1"));
        assertTrue(lObject.delete("p1"));
        assertTrue(lObject.hasProperty("p1"));
        assertFalse(lObject.hasOwnProperty("p1"));

        lPrototype.defineOwnProperty("p1", JSELBoolean.TRUE, true, true, false);
        assertTrue(lObject.delete("blah", true));
        assertTrue(lObject.delete("blah", false));
    }

    @Test
    public void testPutAndOverwriteValue()
    {
        JSELObject lObject = new JSELObject();
        lObject.put("p1", new JSELNumber(1));
        assertEquals(new JSELNumber(1), lObject.getOwn("p1"));
        lObject.put("p1", new JSELString("string"));
        assertEquals(new JSELString("string"), lObject.getOwn("p1"));
    }

    @Test
    public void testSameValue()
    {
        assertTrue(sameValue(JSELBoolean.FALSE, JSELBoolean.FALSE));
        assertTrue(sameValue(new JSELNumber(0), new JSELNumber(0)));
        assertTrue(sameValue(new JSELString("str"), new JSELString("str")));
        assertTrue(sameValue(JSELNull.getInstance(), JSELNull.getInstance()));
        assertTrue(sameValue(JSELUndefined.getInstance(),
                JSELUndefined.getInstance()));
        JSELObject lObject = new JSELObject();
        assertTrue(sameValue(lObject, lObject));
        assertTrue(sameValue(JSELNumber.NAN, new JSELNumber(Double.NaN)));
        assertTrue(sameValue(JSELUndefined.getInstance(),
                new JSELPropertyReference(lObject, "blah")));

        assertFalse(sameValue(JSELBoolean.TRUE, JSELBoolean.FALSE));
        assertFalse(sameValue(new JSELNumber(1), new JSELNumber(0)));
        assertFalse(sameValue(new JSELString("string"), new JSELString("str")));
        assertFalse(sameValue(lObject, new JSELObject()));
        assertFalse(sameValue(JSELNull.getInstance(),
                JSELUndefined.getInstance()));
        assertFalse(sameValue(JSELUndefined.getInstance(),
                JSELNull.getInstance()));
        assertFalse(sameValue(new JSELNumber(-0.0d), new JSELNumber(0.0d)));
        assertFalse(sameValue(new JSELNumber(0.0), new JSELNumber(-0.0)));
    }
}