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

/**
 * This represents an function object type in JSEL.
 */
public class JSELFunction extends JSELObject {
    /**
     * Constant used for the internal [[Class]] property for objects of this
     * type.
     */
    public static final String CLASS = "Function";

    /**
     * Constant for the name of property "length"
     */
    public static final String LENGTH = "length";

    /**
     * Constant for the name of property "prototype"
     */
    public static final String PROTOTYPE = "prototype";

    /**
     * Name of the function. This is mostly used in messages.
     */
    protected String name;

    /**
     * Parameter list. This is mostly used in messages.
     */
    protected List<String> parameters;

    /**
     * Creates a new JSELFunction with default name "f".
     * <p>
     * This uses the {@link ExecutionContext#getFunctionPrototype() function
     * prototype} associated to this thread's {@link ExecutionContext}.
     * </p>
     */
    public JSELFunction() {
        this("f", null);
    }

    /**
     * Creates a new JSELFunction
     * <p>
     * This uses the {@link ExecutionContext#getFunctionPrototype() function
     * prototype} associated to this thread's {@link ExecutionContext}.
     * </p>
     * @param aInName the name of the function
     */
    public JSELFunction(String aInName) {
        this(aInName, null);
    }

    /**
     * Creates a new JSELFunction with default name "f" and with the given
     * parameters.
     * <p>
     * This uses the {@link ExecutionContext#getFunctionPrototype() function
     * prototype} associated to this thread's {@link ExecutionContext}.
     * </p>
     * @param aInParameters the function parameters.
     */
    public JSELFunction(List<String> aInParameters) {
        this("f", aInParameters);
    }

    /**
     * Creates a new JSELFunction with the given name and with the given
     * parameters.
     * <p>
     * This uses the {@link ExecutionContext#getFunctionPrototype() function
     * prototype} associated to this thread's {@link ExecutionContext}.
     * </p>
     * @param aInName the function name.
     * @param aInParameters the function parameters.
     */
    public JSELFunction(String aInName, List<String> aInParameters) {
        this(ExecutionContext.getFunctionPrototype(), aInName, aInParameters);
    }

    /**
     * Creates a new JSELFunction with the given name and with the given
     * parameters, using the given prototype object.
     * @param aInPrototype the Function prototype for this function
     * @param aInName the function name.
     * @param aInParameters the function parameters.
     */
    public JSELFunction(JSELObject aInPrototype,
            String aInName, List<String> aInParameters) {
        super(aInPrototype);
        name = aInName == null ? "f" : aInName;
        parameters = aInParameters == null
                ? Collections.emptyList()
                : aInParameters;

        defineOwnProperty(LENGTH, new JSELNumber(parameters.size()),
                false, false, false);
    }

    @Override
    public String toString() {
        return "f" + (getName() == null ? "" : " " + getName())
                + "() { " + getSourceCode() + " }";
    }

    @Override
    public String getObjectClass() {
        return CLASS;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    /**
     * Gets this function's name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the source code for this function.
     * @return the source code for this function.
     */
    protected String getSourceCode() {
        return "[native code]";
    }

    /**
     * Gets this function's list of parameters.
     * @return the list of parameters
     */
    public List<String> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    /**
     * This is a utility method to returns and indexed argument for the
     * function.
     *
     * @param aInArguments the list of arguments
     * @param aInIndex the index we're interested in
     * @return the argument or {@link JSELUndefined#getInstance()} if the index
     * is out of bounds.
     */
    public static JSELValue getArgument(List<JSELValue> aInArguments,
            int aInIndex) {
        return aInIndex < aInArguments.size()
                ? aInArguments.get(aInIndex)
                : JSELUndefined.getInstance();
    }

    /**
     * Utility to get the first argument in a list of arguments or "undefined"
     * if the list is empty.
     *
     * @param aInArguments the list of arguments.
     * @return the argument or {@link JSELUndefined#getInstance()} if the list
     * is empty.
     */
    public static JSELValue getArgument(List<JSELValue> aInArguments) {
        return aInArguments.isEmpty()
                ? JSELUndefined.getInstance()
                : aInArguments.get(0);
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
                                  ExecutionContext aInExecutionContext) {
        throw JSELRuntimeException.typeError(
                getName() + " is not a constructor");
    }

    @Override
    public JSELBoolean hasInstance(JSELValue aInValue) {
        if (aInValue.getType() != Type.OBJECT) { return JSELBoolean.FALSE; }

        JSELValue lThisPrototype = getOwn(PROTOTYPE);
        if (lThisPrototype.getType() != Type.OBJECT) {
            throw typeError("'typeof " + getName() +
                    ".prototype' is not an object");
        }

        JSELObject lObject = aInValue.toObject();
        do {
            lObject = lObject.getPrototype();
            if (lObject == lThisPrototype) { return JSELBoolean.TRUE; }
        } while (lObject != null);
        return null;
    }
}
