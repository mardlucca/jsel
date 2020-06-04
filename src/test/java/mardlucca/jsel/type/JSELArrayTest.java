/*
 * File: JSELArrayTest.java
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
import org.junit.Test;

import static mardlucca.jsel.type.JSELArray.LENGTH;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class JSELArrayTest
{
    @Test
    public void testPutAllTypes()
    {
        JSELArray lArray = new JSELArray();
        assertEquals(new JSELNumber(0), lArray.get(LENGTH));

        lArray.put(0, new JSELString("Zero"));
        assertEquals(new JSELNumber(1), lArray.get(LENGTH));
        assertEquals(new JSELString("Zero"), lArray.get("0"));

        lArray.put("2", new JSELString("Two"));
        assertEquals(new JSELNumber(3), lArray.get(LENGTH));
        assertEquals(new JSELString("Two"), lArray.get("2"));

        assertEquals(JSELUndefined.getInstance(), lArray.get("1"));
        assertEquals(JSELUndefined.getInstance(), lArray.get("3"));

        lArray.put(new JSELString("1"), new JSELString("One"));
        assertEquals(new JSELNumber(3), lArray.get(LENGTH));
        assertEquals(new JSELString("One"), lArray.get(1));

        lArray.put(new JSELNumber(3), new JSELString("Three"));
        assertEquals(new JSELNumber(4), lArray.get(LENGTH));
        assertEquals(new JSELString("Three"), lArray.get(3));
    }

    @Test
    public void testPutFloatingPoint()
    {
        JSELArray lArray = new JSELArray();
        assertEquals(new JSELNumber(0), lArray.get(LENGTH));

        lArray.put(new JSELNumber(1.2), JSELBoolean.TRUE);
        assertEquals(new JSELNumber(0), lArray.get(LENGTH));
        assertEquals(JSELBoolean.TRUE, lArray.get(new JSELNumber(1.2)));
    }

    @Test
    public void getObjectClass()
    {
        JSELArray lArray = new JSELArray();
        assertEquals(JSELArray.CLASS, lArray.getObjectClass());
        assertTrue(lArray.isObjectClass(JSELArray.CLASS));
    }

    @Test
    public void testChangeLength()
    {
        JSELArray lArray = new JSELArray(
                asList(new JSELNumber(10),
                        new JSELNumber(11),
                        new JSELNumber(12)));

        assertEquals(new JSELNumber(3), lArray.get(LENGTH));
        assertEquals(new JSELNumber(10), lArray.get(0));
        assertEquals(new JSELNumber(11), lArray.get(1));
        assertEquals(new JSELNumber(12), lArray.get(2));

        lArray.put(LENGTH, new JSELString("10"));
        assertEquals(new JSELNumber(10), lArray.get(LENGTH));
        assertEquals(new JSELNumber(10), lArray.get(0));
        assertEquals(new JSELNumber(11), lArray.get(1));
        assertEquals(new JSELNumber(12), lArray.get(2));
        for (int i = 3; i < 10; i++)
        {
            assertFalse(lArray.hasProperty(String.valueOf(i)));
        }

        lArray.put(LENGTH, new JSELString("1"));
        assertEquals(new JSELNumber(1), lArray.get(LENGTH));
        assertEquals(new JSELNumber(10), lArray.get(0));
        assertEquals(JSELUndefined.getInstance(), lArray.get(1));
        assertEquals(JSELUndefined.getInstance(), lArray.get(2));
        for (int i = 1; i < 10; i++)
        {
            assertFalse(lArray.hasProperty(String.valueOf(i)));
        }
    }

    @Test
    public void testChangeReadonlyLength()
    {
        JSELArray lArray = new JSELArray(
                asList(new JSELNumber(10),
                        new JSELNumber(11),
                        new JSELNumber(12)));
        // make length readonly
        lArray.defineOwnProperty(LENGTH, null, null, false, null, false);

        lArray.put(LENGTH, new JSELString("5"), false);
        assertEquals(new JSELNumber(3), lArray.get(LENGTH));

        try
        {
            lArray.defineOwnProperty(LENGTH, new JSELString("1"),
                    null, null, null, true);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot redefine property length",
                    e.getMessage());
        }
    }

    @Test
    public void testInvalidLength()
    {
        JSELArray lArray = new JSELArray(
                asList(new JSELNumber(10),
                        new JSELNumber(11),
                        new JSELNumber(12)));

        try
        {
            lArray.put(LENGTH, new JSELNumber(1.2));
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("RangeError: Invalid array length", e.getMessage());
        }
    }

    @Test
    public void testSetLengthNotConfigurable()
    {
        JSELArray lArray = new JSELArray(
                asList(new JSELNumber(10),
                        new JSELNumber(11),
                        new JSELNumber(12)));
        // make length not configurable
        lArray.defineOwnProperty(LENGTH, null, null, null, false, false);

        lArray.put(LENGTH, new JSELString("5"), false);
        assertEquals(new JSELNumber(5), lArray.get(LENGTH));

        // make it configurable but don't throw
        assertFalse(lArray.defineOwnProperty(LENGTH, new JSELString("1"),
                null, null, true, false));
        try
        {
            // make it configurable and throw
            lArray.defineOwnProperty(LENGTH, new JSELString("1"),
                    null, null, true, true);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot redefine property length",
                    e.getMessage());
        }
    }

    @Test
    public void testArrayItemReadOnly()
    {
        JSELArray lArray = new JSELArray(
                asList(new JSELNumber(10),
                        new JSELNumber(11),
                        new JSELNumber(12)));

        lArray.defineOwnProperty("1", null, null, false, null);
        assertEquals(new JSELNumber(11), lArray.get(1));
        lArray.put("1", new JSELNumber(111), false);
        assertEquals(new JSELNumber(11), lArray.get(1));

        try
        {
            lArray.put("1", new JSELNumber(111));
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot add or change property 1", e.getMessage());
        }
    }

    @Test
    public void testChangeLengthWithItemNotConfigurable()
    {
        JSELArray lArray = new JSELArray(
                asList(new JSELNumber(10),
                        new JSELNumber(11),
                        new JSELNumber(12)));

        lArray.defineOwnProperty("1", null, null, null, false);

        try
        {
            lArray.put(LENGTH, new JSELString("1"));
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot redefine property length", e.getMessage());
        }

        assertEquals(new JSELNumber(2), lArray.get(LENGTH));
        assertEquals(JSELUndefined.getInstance(), lArray.get("2"));
    }

    @Test
    public void testRedefineItemNotConfigurable()
    {
        JSELArray lArray = new JSELArray(
                asList(new JSELNumber(10),
                        new JSELNumber(11),
                        new JSELNumber(12)));

        lArray.defineOwnProperty("1", null, null, null, false);

        try
        {
            lArray.defineOwnProperty("1", null, false, null, false);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot redefine property 1", e.getMessage());
        }
    }

    @Test
    public void testGrowArrayReadonlyLength()
    {
        JSELArray lArray = new JSELArray(
                asList(new JSELNumber(10),
                        new JSELNumber(11),
                        new JSELNumber(12)));
        // make length readonly
        lArray.defineOwnProperty(LENGTH, null, null, false, null, false);

        lArray.put(new JSELString("5"), new JSELNumber(15), false);
        assertEquals(new JSELNumber(3), lArray.get(LENGTH));
        assertEquals(JSELUndefined.getInstance(), lArray.get(5));

        try
        {
            lArray.put(new JSELString("5"), new JSELNumber(15), true);
            fail();
        }
        catch (JSELRuntimeException e)
        {
            assertEquals("Cannot redefine property length",
                    e.getMessage());
        }
    }

}