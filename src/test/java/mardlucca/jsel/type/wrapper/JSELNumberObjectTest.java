/*
 * File: JSELNumberObjectTest.java
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

import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.builtin.ConstantFunction;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELNull;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.type.wrapper.JSELNumberObject;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSELNumberObjectTest {
    private JSELObject number0 = new JSELNumberObject(new JSELNumber(0));
    private JSELObject number1 = new JSELNumberObject(new JSELString("1"));
    private JSELObject numberNegative1 =
            new JSELNumberObject(new JSELNumber(-1));
    private JSELObject numberNaN =
            new JSELNumberObject(new JSELNumber(Double.NaN));
    private JSELObject numberInfinity =
            new JSELNumberObject(new JSELNumber(Double.POSITIVE_INFINITY));

    @Test
    public void equals() {
        assertTrue(number1.equals(JSELBoolean.TRUE));
        assertTrue(number0.equals(JSELBoolean.FALSE));
        assertFalse(number0.equals(JSELBoolean.TRUE));
        assertFalse(number1.equals(JSELBoolean.FALSE));

        assertFalse(number0.equals(number0.toPrimitive(null).toObject()));
        assertTrue(number0.equals(number0));
        assertTrue(number1.equals(number1));
        assertTrue(number1.equals(new JSELNumber(1)));
        assertTrue(numberNegative1.equals(new JSELNumber(-1)));
        assertTrue(numberNaN.equals(numberNaN));
        assertTrue(numberInfinity.equals(numberInfinity));
        assertFalse(number0.equals(number1));
        assertFalse(number0.equals(numberNaN));
        assertFalse(number0.equals(numberInfinity));

        assertTrue(number0.equals(new JSELString("0")));
        assertTrue(number1.equals(new JSELString("1")));
        assertFalse(numberNaN.equals(new JSELString("NaN")));
        assertTrue(numberInfinity.equals(new JSELString("Infinity")));
        assertFalse(number0.equals(new JSELString("10")));
        assertFalse(number0.equals(new JSELString("1")));

        assertFalse(number0.equals(new JSELObject()));
        assertTrue(number0.equals(number0.toObject()));
        JSELObject lObject = new JSELObject();
        lObject.put("valueOf", new ConstantFunction(new JSELNumber(1)));
        lObject.put("toString", new ConstantFunction(new JSELString("str")));
        assertFalse(number1.equals(lObject));
        assertTrue(number1.equals(lObject.toPrimitive(null)));

        assertFalse(number0.equals(JSELNull.getInstance()));
        assertFalse(number1.equals(JSELNull.getInstance()));
        assertFalse(numberNaN.equals(JSELNull.getInstance()));

        assertFalse(number0.equals(JSELUndefined.getInstance()));
        assertFalse(number1.equals(JSELUndefined.getInstance()));
        assertFalse(numberNaN.equals(JSELUndefined.getInstance()));
    }

    @Test
    public void getType() {
        Assert.assertEquals(Type.OBJECT, number0.getType());
    }

    @Test
    public void isPrimitive() {
        assertFalse(number0.isPrimitive());
    }

    @Test
    public void isCallable() {
        assertFalse(number0.isCallable());
    }

    @Test
    public void call() {
        try {
            number0.call(number0.toObject(), null, null);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("cannot invoke object of type object",
                    e.getMessage());
        }
    }

    @Test
    public void strictEquals() {
        assertFalse(number1.strictEquals(JSELBoolean.TRUE));
        assertFalse(number0.strictEquals(JSELBoolean.FALSE));
        assertFalse(number0.strictEquals(JSELBoolean.TRUE));
        assertFalse(number1.strictEquals(JSELBoolean.FALSE));

        assertTrue(number0.strictEquals(number0));
        assertTrue(number1.strictEquals(number1));
        assertFalse(number1.strictEquals(new JSELNumber(1)));
        assertTrue(numberNaN.strictEquals(numberNaN));
        assertTrue(numberInfinity.strictEquals(numberInfinity));
        assertFalse(number0.strictEquals(number1));
        assertFalse(number0.strictEquals(numberNaN));
        assertFalse(number0.strictEquals(numberInfinity));

        assertFalse(number0.strictEquals(new JSELString("0")));
        assertFalse(number1.strictEquals(new JSELString("1")));
        assertFalse(numberNaN.strictEquals(new JSELString("NaN")));
        assertFalse(numberInfinity.strictEquals(new JSELString("Infinity")));
        assertFalse(number0.strictEquals(new JSELString("10")));
        assertFalse(number0.strictEquals(new JSELString("1")));

        assertFalse(number0.strictEquals(new JSELObject()));
        assertTrue(number0.strictEquals(number0.toObject()));
        JSELObject lObject = new JSELObject();
        lObject.put("valueOf", new ConstantFunction(new JSELNumber(1)));
        assertFalse(number1.strictEquals(lObject));

        assertFalse(number0.strictEquals(JSELNull.getInstance()));
        assertFalse(number1.strictEquals(JSELNull.getInstance()));
        assertFalse(numberNaN.strictEquals(JSELNull.getInstance()));

        assertFalse(number0.strictEquals(JSELUndefined.getInstance()));
        assertFalse(number1.strictEquals(JSELUndefined.getInstance()));
        assertFalse(numberNaN.strictEquals(JSELUndefined.getInstance()));
    }

    @Test
    public void toBoolean() {
        assertTrue(number1.toBoolean());
        // number0 is an object, which is always regarded as true
        assertTrue(number0.toBoolean());
        // numberNaN is an object, which is always regarded as true
        assertTrue(numberNaN.toBoolean());
        assertTrue(numberInfinity.toBoolean());
    }

    @Test
    public void toInt32() {
        assertEquals(0, number0.toInt32());
        assertEquals(1, number1.toInt32());
        assertEquals(-1, numberNegative1.toInt32());
        assertEquals(0xffffffffl, numberNegative1.toUInt32());
        assertEquals(0, numberNaN.toInt32());
        assertEquals(0, numberInfinity.toInt32());
    }

    @Test
    public void toNumber() {
        assertEquals(0.0, number0.toNumber(), 0.0);
        assertEquals(1.0, number1.toNumber(), 0.0);
        assertEquals(Double.NaN, numberNaN.toNumber(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, numberInfinity.toNumber(), 0.0);
    }

    @Test
    public void toPrimitive() {
        assertEquals(number0, number0.toPrimitive(null));
        assertEquals(number1, number1.toPrimitive(null));
        assertTrue(Double.isNaN(numberNaN.toNumber()));
        assertTrue(Double.isNaN(numberNaN.toPrimitive(null).toNumber()));
        assertEquals(Type.NUMBER, number1.toPrimitive(null).getType());
        assertEquals(numberInfinity, numberInfinity.toPrimitive(null));
    }

    @Test
    public void toObject() {
        assertSame(number0, number0.toObject());
        assertEquals(number0, number0.toObject().toPrimitive(null));
        assertEquals(Type.OBJECT, number0.toObject().getType());
        assertEquals(JSELNumberObject.CLASS,
                number0.toObject().getObjectClass());
    }

    @Test
    public void testToString() {
        assertEquals("0", number0.toString());
        assertEquals("1", number1.toString());
        assertEquals("NaN", numberNaN.toString());
        assertEquals("Infinity", numberInfinity.toString());
    }

    @Test
    public void toUInt32() {
        assertEquals(0, number0.toUInt32());
        assertEquals(1, number1.toUInt32());
        assertEquals(0, numberNaN.toUInt32());
        assertEquals(0, numberInfinity.toUInt32());
    }
}