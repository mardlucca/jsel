/*
 * File: JSELFunction.java
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

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.JSELRuntimeException;

import java.util.Collections;
import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.typeError;

public class JSELFunction extends JSELObject
{
    public static final String CLASS = "Function";

    public static final String LENGTH = "length";

    public static final String PROTOTYPE = "prototype";

    private String name;

    protected List<String> parameters;

    public JSELFunction()
    {
        this("f", null);
    }

    public JSELFunction(String aInName)
    {
        this(aInName, null);
    }

    public JSELFunction(List<String> aInParameters)
    {
        this("f", aInParameters);
    }

    public JSELFunction(String aInName, List<String> aInParameters)
    {
        this(ExecutionContext.getFunctionPrototype(), aInName, aInParameters);
    }

    public JSELFunction(JSELObject aInPrototype,
            String aInName, List<String> aInParameters)
    {
        super(aInPrototype);
        name = aInName == null ? "f" : aInName;
        parameters = aInParameters == null
                ? Collections.emptyList()
                : aInParameters;

        defineOwnProperty(LENGTH, new JSELNumber(parameters.size()),
                false, false, false);
    }

    @Override
    public String toString()
    {
        return "f" + (getName() == null ? "" : " " + getName())
                + "() { [" + getSourceCode() + "] }";
    }

    @Override
    public Type getType()
    {
        return Type.FUNCTION;
    }

    @Override
    public String getObjectClass()
    {
        return CLASS;
    }

    @Override
    public boolean isCallable()
    {
        return true;
    }

    protected String getName()
    {
        return name;
    }

    protected String getSourceCode()
    {
        return "native code";
    }

    public List<String> getParameters()
    {
        return Collections.unmodifiableList(parameters);
    }

    public static JSELValue getArgument(List<JSELValue> aInArguments,
            int aInIndex)
    {
        return aInIndex < aInArguments.size()
                ? aInArguments.get(aInIndex)
                : JSELUndefined.getInstance();
    }

    public static JSELValue getArgument(List<JSELValue> aInArguments)
    {
        return aInArguments.isEmpty()
                ? JSELUndefined.getInstance()
                : aInArguments.get(0);
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext)
    {
        throw JSELRuntimeException.typeError(name + " is not a constructor");
    }
}
