/*
 * File: JSELUndefinedTest.java
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
import mardlucca.jsel.type.JSELValue.MatchResult;
import mardlucca.jsel.type.wrapper.JSELStringObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSELUndefinedTest {
    private JSELUndefined undefined = JSELUndefined.getInstance();

    @Test
    public void equals() {
        assertFalse(undefined.equals(JSELBoolean.FALSE));

        assertFalse(undefined.equals(new JSELNumber(0)));

        assertFalse(undefined.equals(new JSELString("0")));
        assertFalse(undefined.equals(new JSELString("false")));
        assertFalse(undefined.equals(new JSELString("null")));

        assertFalse(undefined.equals(new JSELObject()));

        assertTrue(undefined.equals(JSELNull.getInstance()));

        assertTrue(undefined.equals(JSELUndefined.getInstance()));
    }

    @Test
    public void getType() {
        assertEquals(Type.UNDEFINED, undefined.getType());
    }

    @Test
    public void isPrimitive() {
        assertTrue(undefined.isPrimitive());
    }

    @Test
    public void isCallable() {
        assertFalse(undefined.isCallable());
    }

    @Test
    public void call() {
        try {
            undefined.call(new JSELStringObject(null), null, null);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("cannot invoke object of type undefined",
                    e.getMessage());
        }
    }

    @Test
    public void instantiate() {
        try {
            undefined.instantiate(null, null);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("undefined is not a constructor", e.getMessage());
        }
    }

    @Test
    public void strictEquals() {
        assertFalse(undefined.strictEquals(JSELBoolean.FALSE));

        assertFalse(undefined.strictEquals(new JSELNumber(0)));

        assertFalse(undefined.strictEquals(new JSELString("0")));
        assertFalse(undefined.strictEquals(new JSELString("false")));
        assertFalse(undefined.strictEquals(new JSELString("null")));

        assertFalse(undefined.strictEquals(new JSELObject()));

        assertFalse(undefined.strictEquals(JSELNull.getInstance()));

        assertTrue(undefined.strictEquals(JSELUndefined.getInstance()));
    }

    @Test
    public void toBoolean() {
        assertFalse(undefined.toBoolean());
    }

    @Test
    public void toInt32() {
        assertEquals(0, undefined.toInt32());
    }

    @Test
    public void toNumber() {
        assertEquals(Double.NaN, undefined.toNumber(), 0.0);
    }

    @Test
    public void toPrimitive() {
        assertSame(undefined, undefined.toPrimitive(null));
    }

    @Test
    public void toObject() {
        try {
            undefined.toObject();
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("undefined cannot be converted to object",
                    e.getMessage());
        }
    }

    @Test
    public void testToString() {
        assertEquals("undefined", undefined.toString());
    }

    @Test
    public void toUInt32() {
        assertEquals(0, undefined.toUInt32());
    }

    @Test
    public void match() {
        MatchResult lResult = undefined.match("undefined", 0);
        assertNotNull(lResult);
        assertEquals(0, lResult.getStart());
        assertEquals(undefined.toString().length(), lResult.getEnd());
        assertEquals(1, lResult.getCaptures().length);
        assertEquals(undefined.toString(), lResult.getCaptures()[0]);

        MatchResult lResult2 = undefined.match("defined", 0);
        assertNotNull(lResult2);
        assertEquals(2, lResult2.getStart());
        assertEquals(9, lResult2.getEnd());
        assertEquals(1, lResult2.getCaptures().length);
        assertEquals("defined", lResult2.getCaptures()[0]);
    }

}