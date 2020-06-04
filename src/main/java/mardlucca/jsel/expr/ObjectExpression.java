/*
 * File: ObjectExpression.java
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
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class ObjectExpression implements JSELExpression
{
    private Map<String, JSELExpression> propertyExpressions = new HashMap<>();

    @Override
    public JSELValue execute(ExecutionContext aInContext)
    {
        JSELObject lNewObject = new JSELObject();
        for (Map.Entry<String, JSELExpression> lEntry :
                propertyExpressions.entrySet())
        {
            lNewObject.put(
                    lEntry.getKey(),
                    lEntry.getValue().execute(aInContext));
        }
        return lNewObject;
    }

    public Object add(Pair<String, JSELExpression> aInValue)
    {
        propertyExpressions.put(aInValue.getKey(), aInValue.getValue());
        return this;
    }
}
