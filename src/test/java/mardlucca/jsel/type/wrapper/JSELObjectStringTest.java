/*
 * File: JSELObjectStringTest.java
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
import mardlucca.jsel.type.JSELValue.GetHint;
import mardlucca.jsel.type.Type;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSELObjectStringTest {
    private JSELObject string = new JSELStringObject(new JSELString("string"));
    private JSELObject trueString =
            new JSELStringObject(JSELBoolean.TRUE);
    private JSELObject falseString =
            new JSELStringObject(new JSELString("false"));
    private JSELObject zeroString = new JSELStringObject(new JSELString("0"));
    private JSELObject oneString = new JSELStringObject(new JSELString("1"));
    private JSELObject negativeOneString =
            new JSELStringObject(new JSELString("-1"));
    private JSELObject nanString = new JSELStringObject(new JSELString("NaN"));
    private JSELObject infinityString =
            new JSELStringObject(new JSELString("Infinity"));
    private JSELStringObject emptyString =
            new JSELStringObject(JSELString.EMPTY_STRING);

    @Test
    public void equals() {
        assertFalse(trueString.equals(JSELBoolean.TRUE));
        assertFalse(falseString.equals(JSELBoolean.FALSE));
        assertFalse(falseString.equals(JSELBoolean.TRUE));
        assertFalse(trueString.equals(JSELBoolean.FALSE));

        assertTrue(zeroString.equals(new JSELNumber(0)));
        assertTrue(oneString.equals(new JSELNumber(1)));
        assertFalse(nanString.equals(new JSELNumber(Double.NaN)));
        assertTrue(infinityString.equals(
                new JSELNumber(Double.POSITIVE_INFINITY)));
        assertTrue(emptyString.equals(new JSELNumber(0)));

        assertTrue(string.equals(string));
        assertTrue(string.equals(new JSELString("string")));
        assertFalse(string.equals(new JSELString("strin")));
        assertTrue(emptyString.equals(new JSELString("")));


        assertFalse(string.equals(new JSELObject()));
        assertTrue(new JSELString("[object Object]").equals(new JSELObject()));
        assertTrue(string.equals(string.toObject()));
        JSELObject lObject = new JSELObject();
        lObject.put("valueOf", new ConstantFunction(new JSELNumber(10)));
        lObject.put("toString", new ConstantFunction(new JSELString("string")));
        assertFalse(string.equals(lObject));
        assertTrue(string.equals(lObject.toPrimitive(GetHint.STRING)));

        assertFalse(string.equals(JSELNull.getInstance()));
        assertFalse(emptyString.equals(JSELNull.getInstance()));

        assertFalse(string.equals(JSELUndefined.getInstance()));
        assertFalse(emptyString.equals(JSELUndefined.getInstance()));
    }

    @Test
    public void getType() {
        assertEquals(Type.OBJECT, string.getType());
    }

    @Test
    public void isPrimitive() {
        assertFalse(string.isPrimitive());
    }

    @Test
    public void isCallable() {
        assertFalse(string.isCallable());
    }

    @Test
    public void call() {
        try {
            string.call(string.toObject(), null, null);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("cannot invoke object of type object",
                    e.getMessage());
        }
    }

    @Test
    public void strictEquals() {
        assertFalse(trueString.strictEquals(JSELBoolean.TRUE));
        assertFalse(falseString.strictEquals(JSELBoolean.FALSE));
        assertFalse(falseString.strictEquals(JSELBoolean.TRUE));
        assertFalse(trueString.strictEquals(JSELBoolean.FALSE));

        assertFalse(zeroString.strictEquals(new JSELNumber(0)));
        assertFalse(oneString.strictEquals(new JSELNumber(1)));
        assertFalse(nanString.strictEquals(new JSELNumber(Double.NaN)));
        assertFalse(infinityString.strictEquals(
                new JSELNumber(Double.POSITIVE_INFINITY)));
        assertFalse(emptyString.strictEquals(new JSELNumber(0)));

        assertTrue(string.strictEquals(string));
        assertFalse(string.strictEquals(new JSELString("string")));
        assertFalse(string.strictEquals(new JSELString("strin")));
        assertFalse(emptyString.strictEquals(new JSELString("")));

        assertFalse(string.strictEquals(new JSELObject()));
        assertTrue(string.strictEquals(string.toObject()));
        JSELObject lObject = new JSELObject();
        lObject.put("valueOf", new ConstantFunction(new JSELNumber(10)));
        lObject.put("toString", new ConstantFunction(new JSELString("string")));
        assertFalse(string.strictEquals(lObject));

        assertFalse(string.strictEquals(JSELNull.getInstance()));
        assertFalse(emptyString.strictEquals(JSELNull.getInstance()));

        assertFalse(string.strictEquals(JSELUndefined.getInstance()));
        assertFalse(emptyString.strictEquals(JSELUndefined.getInstance()));
    }

    @Test
    public void toBoolean() {
        assertTrue(trueString.toBoolean());
        assertTrue(falseString.toBoolean());
        assertTrue(emptyString.toBoolean());
        assertTrue(pad(emptyString).toBoolean());
    }

    @Test
    public void toInt32() {
        assertEquals(0, zeroString.toInt32());
        assertEquals(1, oneString.toInt32());
        assertEquals(-1, negativeOneString.toInt32());
        assertEquals(0, nanString.toInt32());
        assertEquals(0, infinityString.toInt32());
        assertEquals(0, string.toInt32());
    }

    @Test
    public void toNumber() {
        assertEquals(0, zeroString.toNumber(), 0.0);
        assertEquals(1, oneString.toNumber(), 0.0);
        assertEquals(-1, negativeOneString.toNumber(), 0.0);
        assertEquals(Double.NaN, nanString.toNumber(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, infinityString.toNumber(), 0.0);
        assertEquals(Double.NaN, string.toNumber(), 0.0);
    }

    @Test
    public void toPrimitive() {
        assertEquals(string.toPrimitive(GetHint.STRING),
                string.toPrimitive(null));
        assertEquals(new JSELString("string"), string.toPrimitive(null));
    }

    @Test
    public void toObject() {
        assertSame(string, string.toObject());
        assertEquals(Type.OBJECT, string.toObject().getType());
        assertEquals(JSELStringObject.CLASS,
                string.toObject().getObjectClass());
    }

    @Test
    public void testToString() {
        assertEquals("string", string.toString());
    }

    @Test
    public void toUInt32() {
        assertEquals(0, zeroString.toUInt32());
        assertEquals(1, oneString.toUInt32());
        assertEquals(0xffffffffl, negativeOneString.toUInt32());
        assertEquals(0, nanString.toUInt32());
        assertEquals(0, infinityString.toUInt32());
        assertEquals(0, string.toUInt32());
    }

    private static JSELStringObject pad(JSELStringObject aInString) {
        return new JSELStringObject(new JSELString(
                "\n\t " + aInString.getPrimitiveValue() + "\n\t "));
    }
}