/*
 * File: DefaultToStringFunction.java
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
package mardlucca.jsel.builtin;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;

import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.typeError;
import static mardlucca.jsel.builtin.object.ToStringFunction.TO_STRING;

public class DefaultToStringFunction extends JSELFunction
{
    private String objectClass;

    public DefaultToStringFunction(String aInObjectClass)
    {
        super(TO_STRING, null);
        objectClass = aInObjectClass;
    }

    @Override
    public JSELString call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                           ExecutionContext aInExecutionContext)
    {
        if (!aInThisValue.isObjectCoercible()
                || !aInThisValue.toObject().getObjectClass().equals(
                        objectClass))
        {
            throw JSELRuntimeException.typeError(objectClass + ".prototype.toString requires that " +
                    "'this' be a " + objectClass);
        }
        return aInThisValue.isPrimitive()
                ? new JSELString(aInThisValue.toString())
                : new JSELString(
                        aInThisValue.toObject().getPrimitiveValue().toString());
    }
}
