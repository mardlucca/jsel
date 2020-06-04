/*
 * File: ExecutionContext.java
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

import mardlucca.jsel.builtin.global.GlobalObject;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;

import java.util.Stack;

public class ExecutionContext
{
    private static final ThreadLocal<ExecutionContext> contextThreadLocal =
            new ThreadLocal<>();

    private static final ExecutionContext globalContext =
            new ExecutionContext();

    private Stack<EnvironmentRecord> environmentRecords = new Stack<>();
    private Stack<JSELObject> thisBindings = new Stack<>();
    private GlobalObject globalObject = new GlobalObject();

    public ExecutionContext()
    {
        setAsThreadContext();
        try
        {
            globalObject.initialize();
        }
        finally
        {
            clearThreadContext();
        }
        push(new ObjectEnvironmentRecord(globalObject), globalObject);
    }

    public GlobalObject getGlobalObject()
    {
        return globalObject;
    }

    public EnvironmentRecord getEnvironmentRecord()
    {
        return environmentRecords.peek();
    }

    public JSELObject getThisBinding()
    {
        return thisBindings.peek();
    }

    public void push(EnvironmentRecord aInEnvironmentRecord)
    {
        push(aInEnvironmentRecord, getThisBinding());
    }

    public void push(
            EnvironmentRecord aInEnvironmentRecord,
            JSELObject ainThisBinding)
    {
        environmentRecords.push(aInEnvironmentRecord);
        thisBindings.push(ainThisBinding);
    }

    public void pop()
    {
        if (environmentRecords.size() > 1)
        {
            environmentRecords.pop();
            thisBindings.pop();
        }
        // else, we never pop the bottom (global object and initial this bind)
    }

    public void bind(String aInIdentifier, JSELValue aInValue)
    {
        getEnvironmentRecord().bind(aInIdentifier, aInValue);
    }

    public JSELValue resolve(String aInIdentifier)
    {
        return getEnvironmentRecord().resolve(aInIdentifier);
    }

    public static ExecutionContext getGlobalContext()
    {
        return globalContext;
    }

    public static ExecutionContext getThreadContext()
    {
        return contextThreadLocal.get();
    }

    public void setAsThreadContext()
    {
        contextThreadLocal.set(this);
    }

    public static void clearThreadContext()
    {
        contextThreadLocal.remove();
    }

    public static JSELObject getObjectPrototype()
    {
        return getGlobalObjectFromContextOrDefault().getObjectPrototype();
    }

    public static JSELObject getFunctionPrototype()
    {
        return getGlobalObjectFromContextOrDefault().getFunctionPrototype();
    }

    public static JSELObject getBooleanPrototype()
    {
        return getGlobalObjectFromContextOrDefault().getBooleanPrototype();
    }

    public static JSELObject getNumberPrototype()
    {
        return getGlobalObjectFromContextOrDefault().getNumberPrototype();
    }

    public static JSELObject getStringPrototype()
    {
        return getGlobalObjectFromContextOrDefault().getStringPrototype();
    }

    public static JSELObject getArrayPrototype()
    {
        return getGlobalObjectFromContextOrDefault().getArrayPrototype();
    }

    public static JSELObject getRetExpPrototype()
    {
        return getGlobalObjectFromContextOrDefault().getRegExpPrototype();
    }

    private static GlobalObject getGlobalObjectFromContextOrDefault()
    {
        ExecutionContext lExecutionContext =
                ExecutionContext.getThreadContext();
        return lExecutionContext == null
                ? globalContext.getGlobalObject()
                : lExecutionContext.getGlobalObject();
    }
}
