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

public class JSELUserFunction extends JSELFunction
{
    private JSELExpression expression;
    private EnvironmentRecord scope;

    public JSELUserFunction(List<String> aInParameters,
            JSELExpression aInExpression,
            EnvironmentRecord aInScope)
    {
        this(null, aInParameters, aInExpression, aInScope);
    }

    public JSELUserFunction(String aInName,
            List<String> aInParameters,
            JSELExpression aInExpression,
            EnvironmentRecord aInScope)
    {
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
            ExecutionContext aInExecutionContext)
    {
        if (aInThis.getType() == Type.UNDEFINED ||
                aInThis.getType() == Type.NULL)
        {
            aInThis = aInExecutionContext.getGlobalObject();
        }
        aInExecutionContext.push(
                new DeclarativeEnvironmentRecord(scope), aInThis.toObject());
        try
        {
            instantiateDeclarationBinding(aInArguments, aInExecutionContext);
            // function calls never return references
            return expression.execute(aInExecutionContext).getValue();
        }
        finally
        {
            aInExecutionContext.pop();
        }
    }

    private void instantiateDeclarationBinding(List<JSELValue> aInArguments,
            ExecutionContext aInOutContext)
    {
        // bind arguments (arguments cannot be references, by the way, that's
        // why we do getValue
        for (int i = 0; i < parameters.size(); i++)
        {
            JSELValue lArgument = i < aInArguments.size()
                    ? aInArguments.get(i).getValue()
                    : JSELUndefined.getInstance();
            aInOutContext.bind(parameters.get(i), lArgument);
        }

        // TODO: Create and bind "arguments" object
    }

    @Override
    protected String getSourceCode()
    {
        return "source code";
    }

    public JSELExpression getExpression()
    {
        return expression;
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
            ExecutionContext aInExecutionContext)
    {
        JSELObject lThis = new JSELObject(getFunctionPrototype());
        JSELValue lReturn = call(lThis, aInArguments, aInExecutionContext);

        return lReturn.getType() == Type.OBJECT ? lReturn.toObject() : lThis;
    }

    protected JSELObject getFunctionPrototype()
    {
        JSELValue lValue = get(PROTOTYPE);
        if (lValue.getType() == Type.OBJECT)
        {
            return lValue.toObject();
        }
        return ExecutionContext.getObjectPrototype();
    }
}
