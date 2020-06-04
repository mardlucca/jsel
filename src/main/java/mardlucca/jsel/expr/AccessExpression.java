/*
 * File: AccessExpression.java
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
import mardlucca.jsel.type.JSELNull;
import mardlucca.jsel.type.JSELPropertyReference;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.type.JSELPropertyReference;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;

public class AccessExpression implements JSELExpression
{
    private JSELExpression scopeExpression;
    private JSELExpression keyExpression;
    private String key;

    public AccessExpression(
            JSELExpression aInKeyExpression,
            JSELExpression aInScopeExpression)
    {
        keyExpression = aInKeyExpression;
        scopeExpression = aInScopeExpression;
    }

    public AccessExpression(
            String aInKey, JSELExpression aInScopeExpression)
    {
        scopeExpression = aInScopeExpression;
        key = aInKey;
    }

    @Override
    public JSELValue execute(ExecutionContext aInContext)
    {
        JSELValue lScope = scopeExpression.execute(aInContext);

        String lKey = key == null
                ? keyExpression.execute(aInContext).toString()
                : key;

        JSELPropertyReference lReference =
                new JSELPropertyReference(lScope, lKey);

        return lReference.getType() == Type.FUNCTION
                ? lReference
                : lReference.getValue();
    }
}
