/*
 * File: JSELDate.java
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
package mardlucca.jsel.type;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.util.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.lang.Double.isNaN;

/**
 * This represents the Date object type in JSEL.
 */
public class JSELDate extends JSELObject {
    public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");
    public static final String INVALID_DATE = "Invalid Date";
    private static final long MAX_TIME = 8640000000000000L;

    private Date date;

    public JSELDate() {
        this(new Date());
    }

    public JSELDate(Date aInDate) {
        super(ExecutionContext.getDatePrototype());
        date = aInDate;
    }

    public JSELDate(
            double aInYear, double aInMonth, double aInDay,
            double aInHour, double aInMinute, double aInSecond,
            double aInMillisecond) {
        Long lTime = getTime(aInYear, aInMonth, aInDay, aInHour, aInMinute,
                aInSecond, aInMillisecond, false);
        if (lTime != null) {
            date = new Date(lTime);
        }
    }

    protected JSELDate(JSELObject aInPrototype) {
        super(aInPrototype);
    }

    /**
     * Constant used for the internal [[Class]] property for objects of this
     * type.
     */
    public static final String CLASS = "Date";

    /**
     * Converts this Date value to a primitive.
     * @param aInHint a hint to use in the conversion. If not specified,
     *                GetHint.STRING is used.
     * @return the default/primitive value.
     * @see <a href="https://www.ecma-international.org/ecma-262/5.1/#sec-8.12.8">
     * ECMA-262, 5.1, Section 8.12.8"</a>
     */
    @Override
    protected JSELValue defaultValue(GetHint aInHint) {
        return aInHint == null ?
                defaultValue(GetHint.STRING) :
                super.defaultValue(aInHint);
    }

    @Override
    public String getObjectClass() {
        return CLASS;
    }

    @Override
    public String toString() {
        return date == null ? INVALID_DATE : DateFormat.format(date);
    }

    public static Long getTime(
            double aInYear, double aInMonth, double aInDay,
            double aInHour, double aInMinute, double aInSecond,
            double aInMillisecond, boolean aInUTC) {
        if (isNaN(aInYear) || isNaN(aInMonth) || isNaN(aInDay)
                || isNaN(aInHour) || isNaN(aInMinute) || isNaN(aInSecond)
                || isNaN(aInMillisecond)) {
            return null;
        }

        if (aInYear >= 0 && aInYear <= 99) { aInYear += 1900; }

        GregorianCalendar lCalendar = new GregorianCalendar(
                (int) aInYear, (int) aInMonth, (int) aInDay,
                (int) aInHour, (int) aInMinute, (int) aInSecond);
        lCalendar.set(Calendar.MILLISECOND, (int) aInMillisecond);
        if (aInUTC) {
            lCalendar.setTimeZone(UTC_TIMEZONE);
        }

        // perform type clipping, as specified in
        // http://www.ecma-international.org/ecma-262/5.1/#sec-15.9.1.14

        long lTimeMillis = lCalendar.getTimeInMillis();
        if (Math.abs(lTimeMillis) <= MAX_TIME) {
            return lTimeMillis;
        }

        // else we leave date as null (i.e. we "clip" the time)
        return null;
    }

    public Date getDate() {
        return date;
    }

    public void setTime(long aInTime) {
        date = Math.abs(aInTime) <= MAX_TIME ? new Date(aInTime) : null;
    }
}
