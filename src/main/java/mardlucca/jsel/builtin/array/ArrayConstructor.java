/*
 * File: ArrayConstructor.java
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
package mardlucca.jsel.builtin.array;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.Collections;
import java.util.List;

import static jdk.nashorn.internal.runtime.ECMAErrors.rangeError;

public class ArrayConstructor extends JSELFunction
{
    public ArrayConstructor()
    {
        super(JSELArray.CLASS, Collections.singletonList("len"));

        defineOwnProperty(
                JSELFunction.PROTOTYPE, ExecutionContext.getArrayPrototype(),
                false, false, false);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext)
    {
        return instantiate(aInArguments, aInExecutionContext);
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
                                  ExecutionContext aInExecutionContext)
    {
        if (aInArguments.size() != 1)
        {
            return new JSELArray(aInArguments);
        }

        JSELValue lLength = getArgument(aInArguments);
        if (lLength.getType() != Type.NUMBER)
        {
            return new JSELArray(aInArguments);
        }

        int lIndex = lLength.toInteger();
        if (lIndex < 0)
        {
            throw rangeError(lLength.toString());
        }

        return new JSELArray(lIndex);
    }
}
