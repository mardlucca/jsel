/*
 * File: UTCFunction.java
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
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.util.DateFormat;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class UTCFunction extends JSELFunction {
    public static final String NAME = "UTC";

    public UTCFunction() {
        super(NAME, asList(
                "year", "month", "date", "hours", "minutes", "seconds", "ms"));
    }

    @Override
    public JSELValue call(JSELValue aInThis,
                          List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {

        double lYear = getArgument(aInArguments, 0).toNumber();
        double lMonth = getArgument(aInArguments, 1).toNumber();
        double lDay = aInArguments.size() > 2
                ? getArgument(aInArguments, 2).toNumber()
                : 1;
        double lHour = aInArguments.size() > 3
                ? getArgument(aInArguments, 3).toNumber()
                : 0;
        double lMinute = aInArguments.size() > 4
                ? getArgument(aInArguments, 4).toNumber()
                : 0;
        double lSeconds = aInArguments.size() > 5
                ? getArgument(aInArguments, 5).toNumber()
                : 0;
        double lMillis = aInArguments.size() > 6
                ? getArgument(aInArguments, 6).toNumber()
                : 0;

        Long lTime = JSELDate.getTime(
                lYear, lMonth, lDay, lHour, lMinute, lSeconds, lMillis, true);
        return lTime == null ? JSELNumber.NAN : new JSELNumber(lTime);
    }
}
