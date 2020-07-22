/*
 * File: ToJSONFunction.java
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

package mardlucca.jsel.builtin.date;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;
import mardlucca.jsel.util.DateFormat;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;
import static mardlucca.jsel.JSELRuntimeException.typeError;

public class ToJSONFunction extends JSELFunction {
    public static final String NAME = "toJSON";

    public ToJSONFunction() {
        super(NAME, Collections.singletonList("key"));
    }

    @Override
    public JSELValue call(JSELValue aInThis,
                          List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        JSELObject lObject = aInThis.toObject();
        JSELValue lValue = lObject.toPrimitive(GetHint.NUMBER);

        if (lValue.getType() == Type.NUMBER
                && Double.isInfinite(lValue.toNumber())) {
            return JSELNull.getInstance();
        }

        JSELValue lToISO = lObject.get(ToISOStringFunction.NAME);
        return lToISO.call(lObject, emptyList(), aInExecutionContext);
    }
}
