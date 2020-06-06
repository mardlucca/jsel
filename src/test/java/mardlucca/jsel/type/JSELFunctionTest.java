/*
 * File: JSELFunctionTest.java
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

import org.junit.Test;

import static mardlucca.jsel.type.JSELFunction.getArgument;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class JSELFunctionTest {
    private JSELFunction function0 = new JSELFunction(emptyList());

    @Test
    public void getType() {
        assertEquals(Type.OBJECT, function0.getType());
    }

    @Test
    public void getObjectClass() {
        assertEquals(JSELFunction.CLASS, function0.getObjectClass());
    }

    @Test
    public void isCallable() {
        assertTrue(function0.isCallable());
    }

    @Test
    public void getArgumentWithoutIndex() {
        assertSame(JSELUndefined.getInstance(), getArgument(emptyList()));
        assertSame(JSELBoolean.TRUE,
                getArgument(singletonList(JSELBoolean.TRUE)));
        assertSame(JSELBoolean.TRUE,
                getArgument(asList(JSELBoolean.TRUE, JSELBoolean.FALSE)));
    }

    @Test
    public void getArgumentWithIndex() {
        assertSame(JSELUndefined.getInstance(), getArgument(emptyList(), 0));
        assertSame(JSELBoolean.TRUE,
                getArgument(singletonList(JSELBoolean.TRUE), 0));
        assertSame(JSELBoolean.TRUE,
                getArgument(asList(JSELBoolean.TRUE, JSELBoolean.FALSE), 0));

        assertSame(JSELUndefined.getInstance(), getArgument(emptyList(), 1));
        assertSame(JSELUndefined.getInstance(), getArgument(emptyList(), 1));
        assertSame(JSELBoolean.FALSE,
                getArgument(asList(JSELBoolean.TRUE, JSELBoolean.FALSE), 1));
    }

}