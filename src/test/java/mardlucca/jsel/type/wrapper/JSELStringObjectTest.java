/*
 * File: JSELStringObjectTest.java
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

import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELUndefined;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSELStringObjectTest {
    @Test
    public void testGetOwnDescriptor() {
        JSELString lString = new JSELString("test");
        assertEquals("t", lString.toObject().get("0").toString());
        assertEquals("e", lString.toObject().get("1").toString());
        assertEquals("s", lString.toObject().get("2").toString());
        assertEquals("t", lString.toObject().get("3").toString());
        assertSame(JSELUndefined.getInstance(), lString.toObject().get("4"));
        assertSame(JSELUndefined.getInstance(), lString.toObject().get("-1"));
    }
}