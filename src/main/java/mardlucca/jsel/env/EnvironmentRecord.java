/*
 * File: EnvironmentRecord.java
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

import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;

public abstract class EnvironmentRecord
{
    public EnvironmentRecord()
    {
        this(null);
    }

    public EnvironmentRecord(
            EnvironmentRecord aInOuter)
    {
        outer = aInOuter;
    }

    private EnvironmentRecord outer;

    public abstract void bind(String aInIdentifier, JSELValue aInValue);

    protected abstract JSELValue resolveOwn(String aInIdentifier);

    public JSELValue resolve(String aInIdentifier)
    {
        EnvironmentRecord lContext = this;
        do
        {
            JSELValue lValue = lContext.resolveOwn(aInIdentifier);
            if (lValue != null)
            {
                return lValue;
            }

            lContext = lContext.outer;
        }
        while (lContext != null);

        return JSELUndefined.getInstance();
    }

    public void setOuter(EnvironmentRecord aInOuter)
    {
        outer = aInOuter;
    }
}
