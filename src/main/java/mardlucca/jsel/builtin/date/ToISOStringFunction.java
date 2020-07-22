/*
 * File: ToISOStringFunction.java
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
import mardlucca.jsel.type.JSELDate;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.util.DateFormat;

import java.util.Date;
import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.typeError;

public class ToISOStringFunction extends JSELFunction {
    public static final String NAME = "toUTCString";

    public ToISOStringFunction() {
        super(NAME);
    }

    @Override
    public JSELValue call(JSELValue aInThis,
                          List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (!aInThis.toObject().getObjectClass().equals(
                JSELDate.CLASS)) {
            throw typeError(JSELDate.CLASS + ".prototype." + getName() +
                    " requires that this' be a " + JSELDate.CLASS);
        }

        Date lDate = ((JSELDate) aInThis).getDate();
        return lDate == null
                ? new JSELString(JSELDate.INVALID_DATE)
                : new JSELString(DateFormat.formatUTC(lDate));
    }
}
