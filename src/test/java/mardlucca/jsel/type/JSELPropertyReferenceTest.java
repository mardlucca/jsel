/*
 * File: JSELPropertyReferenceTest.java
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
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static mardlucca.jsel.builtin.object.ToStringFunction.TO_STRING;
import static org.junit.Assert.*;

public class JSELPropertyReferenceTest
{
    private JSELObject base = new JSELObject();
    private JSELValue trueObject;
    private JSELValue falseObject;

    @Before
    public void setUp()
    {
        base.put("true", JSELBoolean.TRUE);
        base.put("false", JSELBoolean.FALSE);

        trueObject = new JSELPropertyReference(base, "true");
        falseObject = new JSELPropertyReference(base, "false");
    }

    @Test
    public void equals()
    {
        assertTrue(trueObject.equals(trueObject));
        assertFalse(trueObject.equals(falseObject));
        assertFalse(falseObject.equals(trueObject));
        assertTrue(falseObject.equals(falseObject));

        assertTrue(falseObject.equals(new JSELNumber(0)));
        assertTrue(trueObject.equals(new JSELNumber(1)));
        assertFalse(trueObject.equals(new JSELNumber(10)));
        assertFalse(falseObject.equals(new JSELNumber(1)));

        assertTrue(falseObject.equals(new JSELString("0")));
        assertTrue(trueObject.equals(new JSELString("1")));
        assertFalse(trueObject.equals(new JSELString("10")));
        assertFalse(falseObject.equals(new JSELString("1")));

        assertFalse(falseObject.equals(new JSELObject()));
        assertFalse(trueObject.equals(new JSELObject()));

        assertFalse(falseObject.equals(JSELNull.getInstance()));
        assertFalse(trueObject.equals(JSELNull.getInstance()));

        assertFalse(falseObject.equals(JSELUndefined.getInstance()));
        assertFalse(trueObject.equals(JSELUndefined.getInstance()));
    }

    @Test
    public void getType()
    {
        assertEquals(Type.BOOLEAN, trueObject.getType());
        assertEquals(Type.BOOLEAN, falseObject.getType());
    }

    @Test
    public void isPrimitive()
    {
        assertTrue(trueObject.isPrimitive());
        assertTrue(falseObject.isPrimitive());
    }

    @Test
    public void isCallable()
    {
        assertFalse(trueObject.isCallable());
        assertFalse(falseObject.isCallable());
    }

    @Test
    public void call()
    {
        JSELValue lBooleanToString =
                new JSELPropertyReference(JSELBoolean.TRUE, TO_STRING);
        assertTrue(lBooleanToString.isCallable());
        assertEquals(
                new JSELString("true"),
                lBooleanToString.call(
                        JSELUndefined.getInstance(),
                        Collections.emptyList(), null));

        assertEquals(
                new JSELString("false"),
                lBooleanToString.call(
                        JSELBoolean.FALSE.toObject(),
                        Collections.emptyList(),
                        null));
    }

    @Test
    public void strictEquals()
    {
        assertTrue(trueObject.strictEquals(trueObject));
        assertFalse(trueObject.strictEquals(falseObject));
        assertFalse(falseObject.strictEquals(trueObject));
        assertTrue(falseObject.strictEquals(falseObject));

        assertFalse(falseObject.strictEquals(new JSELNumber(0)));
        assertFalse(trueObject.strictEquals(new JSELNumber(1)));
        assertFalse(trueObject.strictEquals(new JSELNumber(10)));
        assertFalse(falseObject.strictEquals(new JSELNumber(1)));

        assertFalse(falseObject.strictEquals(new JSELString("0")));
        assertFalse(trueObject.strictEquals(new JSELString("1")));
        assertFalse(trueObject.strictEquals(new JSELString("10")));
        assertFalse(falseObject.strictEquals(new JSELString("1")));

        assertFalse(falseObject.strictEquals(new JSELObject()));
        assertFalse(trueObject.strictEquals(new JSELObject()));

        assertFalse(falseObject.strictEquals(JSELNull.getInstance()));
        assertFalse(trueObject.strictEquals(JSELNull.getInstance()));

        assertFalse(falseObject.strictEquals(
                JSELUndefined.getInstance()));
        assertFalse(trueObject.strictEquals(
                JSELUndefined.getInstance()));
    }

    @Test
    public void toBoolean()
    {
        assertTrue(trueObject.toBoolean());
        assertFalse(falseObject.toBoolean());
    }

    @Test
    public void toInt32()
    {
        assertEquals(1, trueObject.toInt32());
        assertEquals(0, falseObject.toInt32());
    }

    @Test
    public void toNumber()
    {
        assertEquals(1.0, trueObject.toNumber(), 0.0);
        assertEquals(0.0, falseObject.toNumber(), 0.0);
    }

    @Test
    public void toPrimitive()
    {
        assertEquals(trueObject, trueObject.toPrimitive(null));
        assertEquals(trueObject,
                trueObject.toPrimitive(GetHint.NUMBER));
        assertEquals(trueObject,
                trueObject.toPrimitive(GetHint.STRING));

        assertEquals(falseObject, falseObject.toPrimitive(null));
        assertEquals(falseObject,
                falseObject.toPrimitive(GetHint.NUMBER));
        assertEquals(falseObject,
                falseObject.toPrimitive(GetHint.STRING));
    }

    @Test
    public void toObject()
    {
        assertEquals(trueObject,
                trueObject.toObject().toPrimitive(null));
        assertEquals(Type.OBJECT, trueObject.toObject().getType());
        assertEquals(JSELBooleanObject.CLASS,
                trueObject.toObject().getObjectClass());

        assertEquals(falseObject,
                falseObject.toObject().toPrimitive(null));
        assertEquals(Type.OBJECT, falseObject.toObject().getType());
    }

    @Test
    public void testToString()
    {
        assertEquals("true", trueObject.toString());
        assertEquals("false", falseObject.toString());
    }

    @Test
    public void toUInt32()
    {
        assertEquals(1, trueObject.toUInt32());
        assertEquals(0, falseObject.toUInt32());
    }

    @Test
    public void testIsReference()
    {
        assertTrue(trueObject.isReference());
        assertTrue(falseObject.isReference());
    }
}