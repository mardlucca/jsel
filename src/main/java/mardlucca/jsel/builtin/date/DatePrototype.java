/*
 * File: DatePrototype.java
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

import mardlucca.jsel.builtin.DefaultToLocaleStringFunction;
import mardlucca.jsel.builtin.DefaultToStringFunction;
import mardlucca.jsel.builtin.DefaultValueOfFunction;
import mardlucca.jsel.builtin.bool.BooleanConstructor;
import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.builtin.object.ToLocaleStringFunction;
import mardlucca.jsel.builtin.object.ToStringFunction;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELDate;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.wrapper.JSELBooleanObject;

import java.util.Calendar;

public class DatePrototype extends JSELDate {
    public DatePrototype(JSELObject aInPrototype) {
        super(aInPrototype);
    }

    public void initialize() {
        defineOwnProperty(ObjectPrototype.CONSTRUCTOR_PROPERTY,
                new DateConstructor(),
                false, true, true);

        defineOwnProperty(ToStringFunction.NAME,
                new DefaultToStringFunction(JSELDate.CLASS),
                false, true, true);
        defineOwnProperty(ToDateStringFunction.NAME,
                new ToDateStringFunction(),
                false, true, true);
        defineOwnProperty(ToTimeStringFunction.NAME,
                new ToTimeStringFunction(),
                false, true, true);
        defineOwnProperty(ToLocaleStringFunction.NAME,
                new DefaultToLocaleStringFunction(JSELDate.CLASS),
                false, true, true);
        defineOwnProperty(ToLocaleDateStringFunction.NAME,
                new ToLocaleDateStringFunction(),
                false, true, true);
        defineOwnProperty(ToLocaleTimeStringFunction.NAME,
                new ToLocaleTimeStringFunction(),
                false, true, true);
        defineOwnProperty(ValueOfFunction.NAME,
                new ValueOfFunction(),
                false, true, true);
        defineOwnProperty(GetTimeFunction.NAME,
                new GetTimeFunction(),
                false, true, true);

        JSELFunction lFunction =
                new GetCalendarFieldFunction("FullYear", Calendar.YEAR);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetUTCCalendarFieldFunction("FullYear", Calendar.YEAR);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetCalendarFieldFunction("Month", Calendar.MONTH);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetUTCCalendarFieldFunction("Month", Calendar.MONTH);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetCalendarFieldFunction("Date", Calendar.DATE);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetUTCCalendarFieldFunction("Date", Calendar.DATE);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetCalendarFieldFunction("Day", Calendar.DAY_OF_WEEK);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetUTCCalendarFieldFunction("Day", Calendar.DAY_OF_WEEK);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetCalendarFieldFunction("Hours", Calendar.HOUR_OF_DAY);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetUTCCalendarFieldFunction("Hours", Calendar.HOUR_OF_DAY);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetCalendarFieldFunction("Minutes", Calendar.MINUTE);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetUTCCalendarFieldFunction("Minutes", Calendar.MINUTE);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetCalendarFieldFunction("Seconds", Calendar.SECOND);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetUTCCalendarFieldFunction("Seconds", Calendar.SECOND);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetCalendarFieldFunction("Milliseconds", Calendar.MILLISECOND);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);
        lFunction = new GetUTCCalendarFieldFunction("Milliseconds", Calendar.MILLISECOND);
        defineOwnProperty(lFunction.getName(), lFunction, false, true, true);

        defineOwnProperty(GetTimezoneOffsetFunction.NAME,
                new GetTimezoneOffsetFunction(),
                false, true, true);
        defineOwnProperty(SetTimeFunction.NAME,
                new SetTimeFunction(),
                false, true, true);
        defineOwnProperty(SetHoursFunction.NAME,
                new SetHoursFunction(),
                false, true, true);
        defineOwnProperty(SetUTCHoursFunction.NAME,
                new SetUTCHoursFunction(),
                false, true, true);
        defineOwnProperty(SetMinutesFunction.NAME,
                new SetMinutesFunction(),
                false, true, true);
        defineOwnProperty(SetUTCMinutesFunction.NAME,
                new SetUTCMinutesFunction(),
                false, true, true);
        defineOwnProperty(SetSecondsFunction.NAME,
                new SetSecondsFunction(),
                false, true, true);
        defineOwnProperty(SetUTCSecondsFunction.NAME,
                new SetUTCSecondsFunction(),
                false, true, true);
        defineOwnProperty(SetMillisecondsFunction.NAME,
                new SetMillisecondsFunction(),
                false, true, true);
        defineOwnProperty(SetUTCMillisecondsFunction.NAME,
                new SetUTCMillisecondsFunction(),
                false, true, true);

        defineOwnProperty(SetDateFunction.NAME,
                new SetDateFunction(),
                false, true, true);
        defineOwnProperty(SetUTCDateFunction.NAME,
                new SetUTCDateFunction(),
                false, true, true);
        defineOwnProperty(SetMonthFunction.NAME,
                new SetMonthFunction(),
                false, true, true);
        defineOwnProperty(SetUTCMonthFunction.NAME,
                new SetUTCMonthFunction(),
                false, true, true);
        defineOwnProperty(SetFullYearFunction.NAME,
                new SetFullYearFunction(),
                false, true, true);
        defineOwnProperty(SetUTCFullYearFunction.NAME,
                new SetUTCFullYearFunction(),
                false, true, true);

        defineOwnProperty(ToUTCStringFunction.NAME,
                new ToUTCStringFunction(),
                false, true, true);
        defineOwnProperty(ToISOStringFunction.NAME,
                new ToISOStringFunction(),
                false, true, true);
        defineOwnProperty(ToJSONFunction.NAME,
                new ToJSONFunction(),
                false, true, true);
    }
}
