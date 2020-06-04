/*
 * File: SortFunction.java
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
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.util.DecimalFormat;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;
import mardlucca.jsel.util.DecimalFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mardlucca.jsel.JSELRuntimeException.typeError;
import static java.util.Arrays.asList;

public class SortFunction extends JSELFunction
{
    public static final String SHIFT = "sort";


    public SortFunction()
    {
        super(SHIFT, Collections.singletonList("comparefn"));
    }

    @Override
    public JSELValue call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext)
    {
        JSELObject lThis = aInThisValue.toObject();
        JSELValue lCompareFn = getArgument(aInArguments);

        Map<String, JSELValue> lValuesMap = collectNumberProperties(lThis);
        List<JSELValue> lValues = new ArrayList<>(lValuesMap.values());
        lValues.sort(new JSELValueComparator(lCompareFn, aInExecutionContext));

        // first we go ahead and delete the numeric properties
        for (String lProperty : lValuesMap.keySet())
        {
            lThis.delete(lProperty);
        }

        // at this point array is pretty much empty. now we start adding
        // elements again
        for (int i = 0; i < lValues.size(); i++)
        {
            lThis.put(String.valueOf(i), lValues.get(i));
        }

        // if the length of lValues is less than the size of lValuesMap, it
        // means the array is sparse (i.e. has empty elements)
        return lThis;
    }

    private static Map<String, JSELValue> collectNumberProperties(
            JSELObject aInObject)
    {
        Map<String, JSELValue> lNumberProperties = new HashMap<>();
        for (String lPropertyName : aInObject.getOwnPropertyNames())
        {
            long lNumber = JSELNumber.toInteger(
                    DecimalFormat.parse(lPropertyName));
            if (lPropertyName.equals(DecimalFormat.format(lNumber)))
            {
                //property is an index
                lNumberProperties.put(
                        lPropertyName, aInObject.get(lPropertyName));
            }
        }
        return lNumberProperties;
    }

    static class JSELValueComparator implements Comparator<JSELValue>
    {
        private JSELValue compareFn;

        private ExecutionContext executionContext;

        public JSELValueComparator(
                JSELValue aInCompareFn,
                ExecutionContext aInExecutionContext)
        {
            compareFn = aInCompareFn;
            executionContext = aInExecutionContext;
        }

        @Override
        public int compare(JSELValue aInValue1, JSELValue aInValue2)
        {
            if (aInValue1.getType() == Type.UNDEFINED)
            {
                return aInValue2.getType() == Type.UNDEFINED ? 0 : -1;
            }
            if (aInValue2.getType() == Type.UNDEFINED)
            {
                return aInValue1.getType() == Type.UNDEFINED ? 0 : 1;
            }

            if (compareFn.isCallable())
            {
                return compareFn.call(
                        JSELUndefined.getInstance(),
                        asList(aInValue1, aInValue2),
                        executionContext)
                        .toInt32();
            }

            if (compareFn.getType() == Type.UNDEFINED)
            {
                return aInValue1.toString().compareTo(aInValue2.toString());
            }

            throw JSELRuntimeException.typeError("The comparison function must be either a " +
                    "function or undefined");
        }
    }
}
