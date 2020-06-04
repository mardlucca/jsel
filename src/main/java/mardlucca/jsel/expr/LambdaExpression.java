/*
 * File: LambdaExpression.java
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
package mardlucca.jsel.expr;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELUserFunction;
import mardlucca.jsel.type.JSELUserFunction;

import java.util.Collections;
import java.util.List;

public class LambdaExpression implements JSELExpression
{
    private List<String> parameters;
    private JSELExpression bodyExpression;

    public LambdaExpression(String aInParameter,
            JSELExpression aInBodyExpression)
    {
        this(Collections.singletonList(aInParameter), aInBodyExpression);
    }

    public LambdaExpression(List<String> aInParameters,
            JSELExpression aInBodyExpression)
    {
        parameters = aInParameters;
        bodyExpression = aInBodyExpression;
    }

    @Override
    public JSELUserFunction execute(ExecutionContext aInContext)
    {
        return new JSELUserFunction(
                parameters,
                bodyExpression,
                aInContext.getEnvironmentRecord());
    }
}
