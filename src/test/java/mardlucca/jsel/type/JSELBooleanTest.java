/*
 * File: JSELBooleanTest.java
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
import mardlucca.jsel.type.JSELValue.GetHint;
import mardlucca.jsel.type.wrapper.JSELBooleanObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSELBooleanTest {
    @Test
    public void equals() {
        assertFalse(JSELBoolean.FALSE.equals(new JSELObject()));

        assertTrue(JSELBoolean.TRUE.equals(JSELBoolean.TRUE));
        assertFalse(JSELBoolean.TRUE.equals(JSELBoolean.FALSE));
        assertFalse(JSELBoolean.FALSE.equals(JSELBoolean.TRUE));
        assertTrue(JSELBoolean.FALSE.equals(JSELBoolean.FALSE));

        assertTrue(JSELBoolean.FALSE.equals(new JSELNumber(0)));
        assertTrue(JSELBoolean.TRUE.equals(new JSELNumber(1)));
        assertFalse(JSELBoolean.TRUE.equals(new JSELNumber(10)));
        assertFalse(JSELBoolean.FALSE.equals(new JSELNumber(1)));

        assertTrue(JSELBoolean.FALSE.equals(new JSELString("0")));
        assertTrue(JSELBoolean.TRUE.equals(new JSELString("1")));
        assertFalse(JSELBoolean.TRUE.equals(new JSELString("10")));
        assertFalse(JSELBoolean.FALSE.equals(new JSELString("1")));

        assertFalse(JSELBoolean.FALSE.equals(new JSELObject()));
        assertFalse(JSELBoolean.TRUE.equals(new JSELObject()));

        assertFalse(JSELBoolean.FALSE.equals(JSELNull.getInstance()));
        assertFalse(JSELBoolean.TRUE.equals(JSELNull.getInstance()));

        assertFalse(JSELBoolean.FALSE.equals(JSELUndefined.getInstance()));
        assertFalse(JSELBoolean.TRUE.equals(JSELUndefined.getInstance()));
    }

    @Test
    public void getType() {
        assertEquals(Type.BOOLEAN, JSELBoolean.TRUE.getType());
        assertEquals(Type.BOOLEAN, JSELBoolean.FALSE.getType());
    }

    @Test
    public void isPrimitive() {
        assertTrue(JSELBoolean.TRUE.isPrimitive());
        assertTrue(JSELBoolean.FALSE.isPrimitive());
    }

    @Test
    public void isCallable() {
        assertFalse(JSELBoolean.TRUE.isCallable());
        assertFalse(JSELBoolean.FALSE.isCallable());
    }

    @Test
    public void call() {
        try {
            JSELBoolean.TRUE.call(JSELBoolean.TRUE.toObject(), null, null);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("cannot invoke object of type boolean",
                    e.getMessage());
        }
    }

    @Test
    public void instantiate() {
        try {
            JSELBoolean.TRUE.instantiate(null, null);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("true is not a constructor", e.getMessage());
        }
    }

    @Test
    public void strictEquals() {
        assertTrue(JSELBoolean.TRUE.strictEquals(JSELBoolean.TRUE));
        assertFalse(JSELBoolean.TRUE.strictEquals(JSELBoolean.FALSE));
        assertFalse(JSELBoolean.FALSE.strictEquals(JSELBoolean.TRUE));
        assertTrue(JSELBoolean.FALSE.strictEquals(JSELBoolean.FALSE));

        assertFalse(JSELBoolean.FALSE.strictEquals(new JSELNumber(0)));
        assertFalse(JSELBoolean.TRUE.strictEquals(new JSELNumber(1)));
        assertFalse(JSELBoolean.TRUE.strictEquals(new JSELNumber(10)));
        assertFalse(JSELBoolean.FALSE.strictEquals(new JSELNumber(1)));

        assertFalse(JSELBoolean.FALSE.strictEquals(new JSELString("0")));
        assertFalse(JSELBoolean.TRUE.strictEquals(new JSELString("1")));
        assertFalse(JSELBoolean.TRUE.strictEquals(new JSELString("10")));
        assertFalse(JSELBoolean.FALSE.strictEquals(new JSELString("1")));

        assertFalse(JSELBoolean.FALSE.strictEquals(new JSELObject()));
        assertFalse(JSELBoolean.TRUE.strictEquals(new JSELObject()));

        assertFalse(JSELBoolean.FALSE.strictEquals(JSELNull.getInstance()));
        assertFalse(JSELBoolean.TRUE.strictEquals(JSELNull.getInstance()));

        assertFalse(JSELBoolean.FALSE.strictEquals(
                JSELUndefined.getInstance()));
        assertFalse(JSELBoolean.TRUE.strictEquals(
                JSELUndefined.getInstance()));
    }

    @Test
    public void toBoolean() {
        assertTrue(JSELBoolean.TRUE.toBoolean());
        assertFalse(JSELBoolean.FALSE.toBoolean());
    }

    @Test
    public void toInt32() {
        assertEquals(1, JSELBoolean.TRUE.toInt32());
        assertEquals(0, JSELBoolean.FALSE.toInt32());
    }

    @Test
    public void toNumber() {
        assertEquals(1.0, JSELBoolean.TRUE.toNumber(), 0.0);
        assertEquals(0.0, JSELBoolean.FALSE.toNumber(), 0.0);
    }

    @Test
    public void toPrimitive() {
        assertSame(JSELBoolean.TRUE, JSELBoolean.TRUE.toPrimitive(null));
        assertSame(JSELBoolean.TRUE,
                JSELBoolean.TRUE.toPrimitive(GetHint.NUMBER));
        assertSame(JSELBoolean.TRUE,
                JSELBoolean.TRUE.toPrimitive(GetHint.STRING));

        assertSame(JSELBoolean.FALSE, JSELBoolean.FALSE.toPrimitive(null));
        assertSame(JSELBoolean.FALSE,
                JSELBoolean.FALSE.toPrimitive(GetHint.NUMBER));
        assertSame(JSELBoolean.FALSE,
                JSELBoolean.FALSE.toPrimitive(GetHint.STRING));
    }

    @Test
    public void toObject() {
        assertSame(JSELBoolean.TRUE,
                JSELBoolean.TRUE.toObject().toPrimitive(null));
        assertEquals(Type.OBJECT, JSELBoolean.TRUE.toObject().getType());
        assertEquals(JSELBooleanObject.CLASS,
                JSELBoolean.TRUE.toObject().getObjectClass());

        assertSame(JSELBoolean.FALSE,
                JSELBoolean.FALSE.toObject().toPrimitive(null));
        assertEquals(Type.OBJECT, JSELBoolean.FALSE.toObject().getType());
    }

    @Test
    public void testToString() {
        assertEquals("true", JSELBoolean.TRUE.toString());
        assertEquals("false", JSELBoolean.FALSE.toString());
    }

    @Test
    public void toUInt32() {
        assertEquals(1, JSELBoolean.TRUE.toUInt32());
        assertEquals(0, JSELBoolean.FALSE.toUInt32());
    }

}