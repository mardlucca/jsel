/*
 * File: JSELStringTest.java
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
import mardlucca.jsel.builtin.object.ToStringFunction;
import mardlucca.jsel.type.JSELValue.MatchResult;
import mardlucca.jsel.type.wrapper.JSELNumberObject;
import mardlucca.jsel.type.wrapper.JSELStringObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSELStringTest {
    private JSELString string = new JSELString("string");
    private JSELString trueString = new JSELString("true");
    private JSELString falseString = new JSELString("false");
    private JSELString zeroString = new JSELString("0");
    private JSELString oneString = new JSELString("1");
    private JSELString negativeOneString = new JSELString("-1");
    private JSELString nanString = new JSELString("NaN");
    private JSELString infinityString = new JSELString("Infinity");
    private JSELString emptyString = JSELString.EMPTY_STRING;

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
        assertTrue(string.equals(lObject));

        assertFalse(string.equals(JSELNull.getInstance()));
        assertFalse(emptyString.equals(JSELNull.getInstance()));

        assertFalse(string.equals(JSELUndefined.getInstance()));
        assertFalse(emptyString.equals(JSELUndefined.getInstance()));
    }

    @Test
    public void getType() {
        assertEquals(Type.STRING, string.getType());
    }

    @Test
    public void isPrimitive() {
        assertTrue(string.isPrimitive());
    }

    @Test
    public void isCallable() {
        assertFalse(string.isCallable());
    }

    @Test
    public void isObjectClass() {
        assertFalse(string.isObjectClass(JSELStringObject.CLASS));
    }

    @Test
    public void isReference() {
        assertFalse(string.isReference());
    }

    @Test
    public void call() {
        try {
            string.call(string.toObject(), null, null);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("cannot invoke object of type string",
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
        assertTrue(string.strictEquals(new JSELString("string")));
        assertFalse(string.strictEquals(new JSELString("strin")));
        assertTrue(emptyString.strictEquals(new JSELString("")));


        assertFalse(string.strictEquals(new JSELObject()));
        assertFalse(string.strictEquals(string.toObject()));
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
        assertFalse(emptyString.toBoolean());
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
    public void toInteger() {
        assertEquals(0, zeroString.toInteger());
        assertEquals(1, oneString.toInteger());
        assertEquals(-1, negativeOneString.toInteger());
        assertEquals(0, nanString.toInteger());
        assertEquals(Integer.MAX_VALUE, infinityString.toInteger());
        assertEquals(0, string.toInteger());
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
        assertSame(string, string.toPrimitive(null));
    }

    @Test
    public void toObject() {
        assertSame(string, string.toObject().toPrimitive(null));
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

    @Test
    public void match() {
        MatchResult lResult = string.match("string", 0);
        assertNotNull(lResult);
        assertEquals(0, lResult.getStart());
        assertEquals(string.toString().length(), lResult.getEnd());
        assertEquals(1, lResult.getCaptures().length);
        assertEquals("string", lResult.getCaptures()[0]);

        MatchResult lResult2 = string.match("rin", 2);
        assertNotNull(lResult2);
        assertEquals(2, lResult2.getStart());
        assertEquals(5, lResult2.getEnd());
        assertEquals(1, lResult2.getCaptures().length);
        assertEquals("rin", lResult2.getCaptures()[0]);
    }

    @Test
    public void notMatch() {
        assertNull(string.match("string", 1));
        assertNull(string.match("rin", 3));
    }

    private static JSELString pad(JSELString aInString) {
        return new JSELString("\n\t " + aInString + "\n\t ");
    }
}