/*
 * File: SetMonthFunction.java
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static java.util.Arrays.asList;
import static mardlucca.jsel.JSELRuntimeException.typeError;

public class SetMonthFunction extends JSELFunction {
    public static final String NAME = "setMonth";

    public SetMonthFunction() {
        this(NAME);
    }

    protected SetMonthFunction(String aInName) {
        super(aInName, asList("month", "date"));
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

        JSELDate lDate = (JSELDate) aInThis;

        GregorianCalendar lCalendar = new GregorianCalendar(getTimeZone());

        lCalendar.setTime(lDate.getDate());
        lCalendar.set(Calendar.MONTH,
                (int) getArgument(aInArguments).toNumber());
        if (aInArguments.size() > 1) {
            lCalendar.set(Calendar.DAY_OF_MONTH,
                    (int) getArgument(aInArguments, 1).toNumber());
        }

        lDate.setTime(lCalendar.getTimeInMillis());

        // date may have been clipped, so we check first
        return lDate.getDate() == null
                ? JSELNumber.NAN
                : new JSELNumber(lDate.getDate().getTime());
    }

    protected TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }
}
