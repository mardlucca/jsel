/*
 * File: MapFunction.java
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
import mardlucca.jsel.type.*;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static mardlucca.jsel.JSELRuntimeException.typeError;

public class MapFunction extends JSELFunction {
    public static final String NAME = "map";

    public MapFunction() {
        super(NAME, Collections.singletonList("callbackfn"));
    }

    @Override
    public JSELValue call(JSELValue aInThis,
                          List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        JSELValue lCallbackFn = getArgument(aInArguments);
        if (!lCallbackFn.isCallable()) {
            throw typeError("'" + lCallbackFn + "' is not a function");
        }
        JSELObject lObject = aInThis.toObject();
        long lLength = lObject.get(JSELArray.LENGTH).toUInt32();
        JSELValue lThisArg = getArgument(aInArguments, 1);

        JSELArray lJSELArray = new JSELArray((int) lLength);

        for (int i = 0; i < lLength; i++) {
            String lStringIndex = String.valueOf(i);
            if (!lObject.hasProperty(lStringIndex)) { continue; }

            JSELValue lMapped = lCallbackFn.call(
                    lThisArg,
                    asList(lObject.get(lStringIndex),
                            new JSELNumber(i),
                            lObject),
                    aInExecutionContext);

            lJSELArray.defineOwnProperty(lStringIndex, lMapped,
                    true, true, true, false);
        }
        return lJSELArray;
    }
}
