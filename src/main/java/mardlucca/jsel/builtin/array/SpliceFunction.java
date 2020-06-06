/*
 * File: SpliceFunction.java
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
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.util.Arrays.asList;

public class SpliceFunction extends JSELFunction {
    public static final String SPLICE = "splice";

    public SpliceFunction() {
        super(SPLICE, asList("start", "deleteCount"));
    }

    @Override
    public JSELArray call(JSELValue aInThisValue, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        JSELObject lThis = aInThisValue.toObject();
        int lLength = lThis.get(JSELArray.LENGTH).toInteger();

        int lStartArgument = getArgument(aInArguments, 0).toInteger();
        int lStart = lStartArgument < 0
                ? max(lLength + lStartArgument, 0)
                : min (lStartArgument, lLength);

        int lDeleteCount = min(
                max(getArgument(aInArguments, 1).toInteger(), 0),
                lLength - lStart);

        // collect elements to return
        List<JSELValue> lArrayValues = new ArrayList<>();
        for (int i = 0; i < lDeleteCount; i++) {
            String lFrom = String.valueOf(lStart + i);
            if (lThis.hasProperty(lFrom)) {
                lArrayValues.add(lThis.get(lFrom));
            }
        }

        int lItemCount = aInArguments.size() - 2;
        // position the final items in the array to their final location,
        // leaving a gap for the new items.

        if (lItemCount < lDeleteCount) {
            // we're deleting more than we are inserting
            for (int i = lStart; i < lLength - lDeleteCount; i++) {
                String lFrom = String.valueOf(i + lDeleteCount);
                String lTo = String.valueOf(i + lItemCount);
                if (lThis.hasProperty(lFrom)) {
                    lThis.put(lTo, lThis.get(lFrom));
                }
                else {
                    lThis.delete(lTo);
                }
            }

            // remove tail items, as array is now smaller than it was originally
            for (int i = lLength - 1;
                    i >= lLength - lDeleteCount + lItemCount;
                    i++) {
                lThis.delete(String.valueOf(i));
            }
        }
        else if (lItemCount > lDeleteCount) {
            // number of new items is larger than original. We move the tail
            // out a little to leave enough space for the new items.
            for (int i = lLength - lDeleteCount; i > lStart; i--) {
                String lFrom = String.valueOf(i + lDeleteCount - 1);
                String lTo = String.valueOf(i + lItemCount -1 );
                if (lThis.hasProperty(lFrom)) {
                    lThis.put(lTo, lThis.get(lFrom));
                }
                else {
                    lThis.delete(lTo);
                }
            }

        }

        // finally, copy new items in
        for (int i = 0; i < lItemCount; i++) {
            lThis.put(String.valueOf(lStart + i),
                    getArgument(aInArguments, 2 + i));
        }

        lThis.put(LENGTH,
                new JSELNumber(lLength - lDeleteCount + lItemCount));

        return new JSELArray(lArrayValues);
    }
}
