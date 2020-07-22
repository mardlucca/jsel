/*
 * File: DateConstructor.java
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

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

public class DateConstructor extends JSELFunction {
    public DateConstructor() {
        super(JSELDate.CLASS, asList(
                "year", "month", "date", "hours", "minutes", "seconds", "ms"));

        defineOwnProperty(
                JSELFunction.PROTOTYPE, ExecutionContext.getDatePrototype(),
                false, false, false);
        defineOwnProperty(
                UTCFunction.NAME, new UTCFunction(),
                false, true, true);
    }

    @Override
    public JSELValue call(JSELValue aInThis,
                          List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        return new JSELString(DateFormat.format(new Date()));
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
                                  ExecutionContext aInExecutionContext) {
        if (aInArguments.size() == 0) {
            return new JSELDate();
        }
        if (aInArguments.size() == 1) {
            JSELValue lValue = getArgument(aInArguments).toPrimitive(null);
            if (lValue.getType() == Type.STRING) {
                return new JSELDate(DateFormat.parse(lValue.toString()));
            }
            return new JSELDate(new Date((long) lValue.toNumber()));
        }

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

        return new JSELDate(
                lYear, lMonth, lDay, lHour, lMinute, lSeconds, lMillis);
    }
}
