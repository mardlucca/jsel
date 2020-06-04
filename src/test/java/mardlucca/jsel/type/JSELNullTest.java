/*
 * File: JSELNullTest.java
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
import mardlucca.jsel.type.wrapper.JSELNumberObject;
import mardlucca.jsel.type.wrapper.JSELStringObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSELNullTest
{
    private JSELNull nullObject = JSELNull.getInstance();

    @Test
    public void equals()
    {
        assertFalse(nullObject.equals(JSELBoolean.FALSE));

        assertFalse(nullObject.equals(new JSELNumber(0)));

        assertFalse(nullObject.equals(new JSELString("0")));
        assertFalse(nullObject.equals(new JSELString("false")));
        assertFalse(nullObject.equals(new JSELString("null")));

        assertFalse(nullObject.equals(new JSELObject()));

        assertTrue(nullObject.equals(nullObject));

        assertTrue(nullObject.equals(JSELUndefined.getInstance()));
    }

    @Test
    public void getType()
    {
        assertEquals(Type.NULL, nullObject.getType());
    }

    @Test
    public void isPrimitive()
    {
        assertTrue(nullObject.isPrimitive());
    }

    @Test
    public void isCallable()
    {
        assertFalse(nullObject.isCallable());
    }

    @Test
    public void call()
    {
        try
        {
            nullObject.call(new JSELStringObject(null), null, null);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("cannot invoke object of type null",
                    e.getMessage());
        }
    }

    @Test
    public void instantiate()
    {
        try
        {
            nullObject.instantiate(null, null);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("null is not a constructor", e.getMessage());
        }
    }

    @Test
    public void strictEquals()
    {
        assertFalse(nullObject.strictEquals(JSELBoolean.FALSE));

        assertFalse(nullObject.strictEquals(new JSELNumber(0)));

        assertFalse(nullObject.strictEquals(new JSELString("0")));
        assertFalse(nullObject.strictEquals(new JSELString("false")));
        assertFalse(nullObject.strictEquals(new JSELString("null")));

        assertFalse(nullObject.strictEquals(new JSELObject()));

        assertTrue(nullObject.strictEquals(nullObject));

        assertFalse(nullObject.strictEquals(JSELUndefined.getInstance()));
    }

    @Test
    public void toBoolean()
    {
        assertFalse(nullObject.toBoolean());
    }

    @Test
    public void toInt32()
    {
        assertEquals(0, nullObject.toInt32());
    }

    @Test
    public void toNumber()
    {
        assertEquals(0.0, nullObject.toNumber(), 0.0);
    }

    @Test
    public void toPrimitive()
    {
        assertSame(nullObject, nullObject.toPrimitive(null));
    }

    @Test
    public void toObject()
    {
        try
        {
            nullObject.toObject();
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("null cannot be converted to object",
                    e.getMessage());
        }
    }

    @Test
    public void testToString()
    {
        assertEquals("null", nullObject.toString());
    }

    @Test
    public void toUInt32()
    {
        assertEquals(0, nullObject.toUInt32());
    }
}