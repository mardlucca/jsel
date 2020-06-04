/*
 * File: ExecutionContextTest.java
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

package mardlucca.jsel.env;

import mardlucca.jsel.builtin.DefaultToStringFunction;
import mardlucca.jsel.builtin.global.GlobalObject;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELRegExp;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionContextTest
{
    private GlobalObject globalObject;
    private ExecutionContext executionContext;

    @Before
    public void setUp()
    {
        executionContext = new ExecutionContext();
        globalObject = executionContext.getGlobalObject();
        globalObject.put("string", new JSELString("global"));
        globalObject.put("number", new JSELNumber(10));
    }

    @Test
    public void testThreadContext()
    {
        assertNull(ExecutionContext.getThreadContext());
        executionContext.setAsThreadContext();
        assertSame(executionContext, ExecutionContext.getThreadContext());
        ExecutionContext.clearThreadContext();
        assertNull(ExecutionContext.getThreadContext());
    }

    @Test
    public void testResolve()
    {
        assertEquals("global", executionContext.resolve("string").toString());
        assertSame(globalObject, executionContext.getGlobalObject());
        executionContext.push(new DeclarativeEnvironmentRecord(
                executionContext.getEnvironmentRecord()));
        assertEquals("global", executionContext.resolve("string").toString());
        executionContext.bind("string", new JSELString("local"));
        assertEquals("local", executionContext.resolve("string").toString());
        assertEquals("global", globalObject.get("string").toString());

        executionContext.push(new ObjectEnvironmentRecord(
                executionContext.getEnvironmentRecord(),
                new JSELObject()));
        assertEquals("local", executionContext.resolve("string").toString());
        executionContext.bind("string", new JSELString("local2"));
        assertEquals("local2", executionContext.resolve("string").toString());
        assertSame(globalObject, executionContext.getGlobalObject());

        executionContext.pop();
        assertEquals("local", executionContext.resolve("string").toString());
        assertEquals(
                new JSELNumber(10),
                executionContext.resolve("number"));
        assertSame(
                JSELUndefined.getInstance(),
                executionContext.resolve("blah"));

        executionContext.pop();
        executionContext.pop();
        executionContext.pop();
        executionContext.pop();
        executionContext.pop();
        assertEquals("global", executionContext.resolve("string").toString());
        assertSame(globalObject, executionContext.getGlobalObject());

        // setting a value as undefined in a new env record
        executionContext.push(new DeclarativeEnvironmentRecord(
                executionContext.getEnvironmentRecord()));
        executionContext.bind("string", JSELUndefined.getInstance());
        assertSame(JSELUndefined.getInstance(),
                executionContext.resolve("string"));
        executionContext.pop();
        assertEquals("global", executionContext.resolve("string").toString());
    }

    @Test
    public void testGetThisBind()
    {
        assertSame(globalObject, executionContext.getThisBinding());
        JSELObject lNewThis = new JSELObject();
        executionContext.push(new DeclarativeEnvironmentRecord(
                executionContext.getEnvironmentRecord()), lNewThis);
        assertSame(lNewThis, executionContext.getThisBinding());
        executionContext.push(new DeclarativeEnvironmentRecord(
                executionContext.getEnvironmentRecord()));
        assertSame(lNewThis, executionContext.getThisBinding());
        executionContext.pop();
        assertSame(lNewThis, executionContext.getThisBinding());
        executionContext.pop();
        assertSame(globalObject, executionContext.getThisBinding());
        executionContext.pop();
        executionContext.pop();
        executionContext.pop();
        executionContext.pop();
        assertSame(globalObject, executionContext.getThisBinding());
    }

    @Test
    public void testEmptyContext()
    {
        ExecutionContext lExecutionContext = new ExecutionContext();
        assertNotNull(lExecutionContext.getGlobalObject());
        assertSame(lExecutionContext.getGlobalObject(),
                lExecutionContext.getThisBinding());
    }

    @Test
    public void testBindNewValueToGlobal()
    {
        executionContext.bind("string", new JSELString("str"));
        executionContext.bind("string2", new JSELString("str2"));
        assertEquals("str", executionContext.resolve("string").toString());
        assertEquals("str2", executionContext.resolve("string2").toString());
        assertEquals("str", globalObject.get("string").toString());
        assertEquals("str2", globalObject.get("string2").toString());
    }

    @Test
    public void testNumberPrototypes()
    {
        JSELValue lNumber = new JSELNumber(1);
        assertSame(getDefaultGlobalObject().getNumberPrototype(),
                lNumber.toObject().getPrototype());
        assertNotSame(globalObject.getNumberPrototype(),
                lNumber.toObject().getPrototype());
        executionContext.setAsThreadContext();

        assertNotSame(getDefaultGlobalObject().getNumberPrototype(),
                lNumber.toObject().getPrototype());
        assertSame(globalObject.getNumberPrototype(),
                lNumber.toObject().getPrototype());

        ExecutionContext.clearThreadContext();
        assertSame(getDefaultGlobalObject().getNumberPrototype(),
                lNumber.toObject().getPrototype());
        assertNotSame(globalObject.getNumberPrototype(),
                lNumber.toObject().getPrototype());
    }

    @Test
    public void testStringPrototypes()
    {
        JSELValue lString = new JSELString("1");
        assertSame(getDefaultGlobalObject().getStringPrototype(),
                lString.toObject().getPrototype());
        assertNotSame(globalObject.getStringPrototype(),
                lString.toObject().getPrototype());
        executionContext.setAsThreadContext();

        assertNotSame(getDefaultGlobalObject().getStringPrototype(),
                lString.toObject().getPrototype());
        assertSame(globalObject.getStringPrototype(),
                lString.toObject().getPrototype());

        ExecutionContext.clearThreadContext();
        assertSame(getDefaultGlobalObject().getStringPrototype(),
                lString.toObject().getPrototype());
        assertNotSame(globalObject.getStringPrototype(),
                lString.toObject().getPrototype());
    }

    @Test
    public void testBooleanPrototypes()
    {
        JSELValue lBoolean = JSELBoolean.TRUE;
        assertSame(getDefaultGlobalObject().getBooleanPrototype(),
                lBoolean.toObject().getPrototype());
        assertNotSame(globalObject.getBooleanPrototype(),
                lBoolean.toObject().getPrototype());
        executionContext.setAsThreadContext();

        assertNotSame(getDefaultGlobalObject().getBooleanPrototype(),
                lBoolean.toObject().getPrototype());
        assertSame(globalObject.getBooleanPrototype(),
                lBoolean.toObject().getPrototype());

        ExecutionContext.clearThreadContext();
        assertSame(getDefaultGlobalObject().getBooleanPrototype(),
                lBoolean.toObject().getPrototype());
        assertNotSame(globalObject.getBooleanPrototype(),
                lBoolean.toObject().getPrototype());
    }

    @Test
    public void testObjectPrototypes()
    {
        JSELValue lObject = new JSELObject();
        assertSame(getDefaultGlobalObject().getObjectPrototype(),
                lObject.toObject().getPrototype());
        assertNotSame(globalObject.getObjectPrototype(),
                lObject.toObject().getPrototype());
        executionContext.setAsThreadContext();

        lObject = new JSELObject();
        assertNotSame(getDefaultGlobalObject().getObjectPrototype(),
                lObject.toObject().getPrototype());
        assertSame(globalObject.getObjectPrototype(),
                lObject.toObject().getPrototype());

        ExecutionContext.clearThreadContext();

        lObject = new JSELObject();
        assertSame(getDefaultGlobalObject().getObjectPrototype(),
                lObject.toObject().getPrototype());
        assertNotSame(globalObject.getObjectPrototype(),
                lObject.toObject().getPrototype());
    }


    @Test
    public void testArrayPrototypes()
    {
        JSELValue lArray = new JSELArray();
        assertSame(getDefaultGlobalObject().getArrayPrototype(),
                lArray.toObject().getPrototype());
        assertNotSame(globalObject.getArrayPrototype(),
                lArray.toObject().getPrototype());
        executionContext.setAsThreadContext();

        lArray = new JSELArray();
        assertNotSame(getDefaultGlobalObject().getArrayPrototype(),
                lArray.toObject().getPrototype());
        assertSame(globalObject.getArrayPrototype(),
                lArray.toObject().getPrototype());

        ExecutionContext.clearThreadContext();

        lArray = new JSELArray();
        assertSame(getDefaultGlobalObject().getArrayPrototype(),
                lArray.toObject().getPrototype());
        assertNotSame(globalObject.getArrayPrototype(),
                lArray.toObject().getPrototype());
    }

    @Test
    public void testFunctionPrototypes()
    {
        JSELValue lFunction = new DefaultToStringFunction(JSELObject.CLASS);
        assertSame(getDefaultGlobalObject().getFunctionPrototype(),
                lFunction.toObject().getPrototype());
        assertNotSame(globalObject.getFunctionPrototype(),
                lFunction.toObject().getPrototype());
        executionContext.setAsThreadContext();

        lFunction = new DefaultToStringFunction(JSELObject.CLASS);
        assertNotSame(getDefaultGlobalObject().getFunctionPrototype(),
                lFunction.toObject().getPrototype());
        assertSame(globalObject.getFunctionPrototype(),
                lFunction.toObject().getPrototype());

        ExecutionContext.clearThreadContext();

        lFunction = new DefaultToStringFunction(JSELObject.CLASS);
        assertSame(getDefaultGlobalObject().getFunctionPrototype(),
                lFunction.toObject().getPrototype());
        assertNotSame(globalObject.getFunctionPrototype(),
                lFunction.toObject().getPrototype());
    }

    @Test
    public void testRegExpPrototypes()
    {
        JSELValue lRegExp = new JSELRegExp("a");
        assertSame(getDefaultGlobalObject().getRegExpPrototype(),
                lRegExp.toObject().getPrototype());
        assertNotSame(globalObject.getRegExpPrototype(),
                lRegExp.toObject().getPrototype());
        executionContext.setAsThreadContext();

        lRegExp = new JSELRegExp("a");
        assertNotSame(getDefaultGlobalObject().getRegExpPrototype(),
                lRegExp.toObject().getPrototype());
        assertSame(globalObject.getRegExpPrototype(),
                lRegExp.toObject().getPrototype());

        ExecutionContext.clearThreadContext();

        lRegExp = new JSELRegExp("a");
        assertSame(getDefaultGlobalObject().getRegExpPrototype(),
                lRegExp.toObject().getPrototype());
        assertNotSame(globalObject.getRegExpPrototype(),
                lRegExp.toObject().getPrototype());
    }

    private GlobalObject getDefaultGlobalObject()
    {
        return ExecutionContext.getGlobalContext().getGlobalObject();
    }
}