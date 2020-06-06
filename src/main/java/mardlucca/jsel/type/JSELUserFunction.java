/*
 * File: JSELUserFunction.java
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

import mardlucca.jsel.env.DeclarativeEnvironmentRecord;
import mardlucca.jsel.env.EnvironmentRecord;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.expr.JSELExpression;

import java.util.List;

import static mardlucca.jsel.builtin.object.ObjectPrototype.CONSTRUCTOR_PROPERTY;

/**
 * This class represents a user defined function. This function takes a {@link
 * JSELExpression} with the expression that needs to be evaluated as long as an
 * EnvironmentRecord from which identifiers are read.
 */
public class JSELUserFunction extends JSELFunction {
    private JSELExpression expression;
    private EnvironmentRecord scope;

    /**
     * Creates a user defined function.
     * @param aInParameters the function parameters
     * @param aInExpression the expression
     * @param aInScope the environment record containing the identifiers and
     *                 arguments for to be used by the function.
     */
    public JSELUserFunction(List<String> aInParameters,
            JSELExpression aInExpression,
            EnvironmentRecord aInScope) {
        this(null, aInParameters, aInExpression, aInScope);
    }

    /**
     * Creates a user defined function.
     * @param aInName the name of the function
     * @param aInParameters the function parameters
     * @param aInExpression the expression
     * @param aInScope the current environment record used for resolving
     *                 identifiers external to the function (i.e. the so called
     *                 "closures").
     */
    public JSELUserFunction(String aInName,
            List<String> aInParameters,
            JSELExpression aInExpression,
            EnvironmentRecord aInScope) {
        super(aInName, aInParameters);
        expression = aInExpression;
        scope = aInScope;

        JSELObject lPrototype = new JSELObject();
        lPrototype.defineOwnProperty(CONSTRUCTOR_PROPERTY,
                this, false, true, true);
        defineOwnProperty(PROTOTYPE, lPrototype, false, true, false);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext) {
        if (aInThis.getType() == Type.UNDEFINED ||
                aInThis.getType() == Type.NULL) {
            aInThis = aInExecutionContext.getGlobalObject();
        }
        aInExecutionContext.push(
                new DeclarativeEnvironmentRecord(scope), aInThis.toObject());
        try {
            bindArgumentsToParameters(aInArguments, aInExecutionContext);
            // function calls never return references
            return expression.execute(aInExecutionContext).getValue();
        }
        finally {
            aInExecutionContext.pop();
        }
    }

    /**
     * Binds argument values to the function's parameters in the execution
     * context.
     * @param aInArguments the argument values to bind.
     * @param aInOutContext the execution context.
     */
    private void bindArgumentsToParameters(List<JSELValue> aInArguments,
                                           ExecutionContext aInOutContext) {
        // bind arguments (arguments cannot be references, by the way, that's
        // why we do getValue
        for (int i = 0; i < parameters.size(); i++) {
            JSELValue lArgument = i < aInArguments.size()
                    ? aInArguments.get(i).getValue()
                    : JSELUndefined.getInstance();
            aInOutContext.bind(parameters.get(i), lArgument);
        }

        // TODO: Create and bind "arguments" object
    }

    @Override
    protected String getSourceCode() {
        return "source code";
    }

    /**
     * Instantiates a new object using this function as a constructor. This
     * implements instantiation as specified for internal method "[[Construct]]"
     * @param aInArguments the constructor arguments
     * @param aInExecutionContext the execution context
     * @return the newly created object.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-13.2.2">
     * ECMA-262, 5.1, Section 13.2.2"</a>
     */
    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext) {
        JSELObject lThis = new JSELObject(getFunctionPrototype());
        JSELValue lReturn = call(lThis, aInArguments, aInExecutionContext);

        return lReturn.getType() == Type.OBJECT ? lReturn.toObject() : lThis;
    }

    /**
     * Gets the function prototype object. If the function's "prototype"
     * property is not a JSELObject, the default Object.prototype value is
     * used.
     * @return the function prototype object.
     */
    private JSELObject getFunctionPrototype() {
        JSELValue lValue = get(PROTOTYPE);
        if (lValue.getType() == Type.OBJECT) {
            // return de-referenced object.
            return lValue.toObject();
        }
        return ExecutionContext.getObjectPrototype();
    }
}
