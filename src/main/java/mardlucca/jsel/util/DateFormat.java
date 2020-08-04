/*
 * File: DateFormat.java
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
package mardlucca.jsel.util;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateFormat {
    private static ThreadLocal<java.text.DateFormat> dateStringFormat =
            ThreadLocal.withInitial(() ->
                    new SimpleDateFormat("EEE MMM dd yyyy"));
    private static ThreadLocal<java.text.DateFormat> timeStringFormat =
            ThreadLocal.withInitial(() ->
                    new SimpleDateFormat("HH:mm:ss 'GMT'Z (zzzz)"));
    private static ThreadLocal<java.text.DateFormat> dateFormat =
            ThreadLocal.withInitial(() ->
                    new SimpleDateFormat(
                            "EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)"));
    private static ThreadLocal<java.text.DateFormat> utcDateFormat =
            ThreadLocal.withInitial(() -> {
                    SimpleDateFormat lFormat = new SimpleDateFormat(
                            "EEE MMM dd yyyy HH:mm:ss 'GMT'");
                    lFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    return lFormat;
            });
    private static ThreadLocal<java.text.DateFormat> isoDateFormat =
            ThreadLocal.withInitial(() -> {
                SimpleDateFormat lFormat = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                lFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                return lFormat;
            });

    private static final String DATE_PART =
            "(\\d{4})(?:-(\\d\\d)(?:-(\\d\\d))?)?";
    private static final String TIME_PART =
            "T(\\d{2}):(\\d{2})(?::(\\d{2})(?:\\.(\\d{3}))?)?";
    private static final String TIME_ZONE =
            "(Z|[+\\-](?:\\d{4}|\\d{2}:\\d{2}))?";

    private static Pattern datePattern =
            Pattern.compile(DATE_PART +
                    "(?:" + TIME_PART + TIME_ZONE + ")?");

    private DateFormat() {
    }

    public static String format(Date aInDate) {
        return dateFormat.get().format(aInDate);
    }

    public static String formatUTC(Date aInDate) {
        return utcDateFormat.get().format(aInDate);
    }

    public static String formatISO(Date aInDate) {
        return isoDateFormat.get().format(aInDate);
    }

    public static String toTimeString(Date aInDate) {
        return timeStringFormat.get().format(aInDate);
    }


    public static String toDateString(Date aInDate) {
        return dateStringFormat.get().format(aInDate);
    }

    public static Date parse(String aInDateString) {
        Matcher lMatcher = datePattern.matcher(aInDateString);

        if (!lMatcher.matches()) {
            return null;
        }

        TimeZone lTimeZone = lMatcher.group(8) == null
                ? lMatcher.group(4) == null
                        ? TimeZone.getTimeZone("GMT")
                        : TimeZone.getDefault()
                : "Z".equals(lMatcher.group(8))
                        ? TimeZone.getTimeZone("GMT")
                        : TimeZone.getTimeZone(
                                ZoneOffset.of(lMatcher.group(8)));

        GregorianCalendar lCalendar = new GregorianCalendar(lTimeZone);
        lCalendar.set(Calendar.YEAR, Integer.parseInt(lMatcher.group(1)));
        lCalendar.set(Calendar.MONTH, lMatcher.group(2) == null
                ? 0
                : Integer.parseInt(lMatcher.group(2)) - 1);
        lCalendar.set(Calendar.DATE, lMatcher.group(3) == null
                ? 1
                : Integer.parseInt(lMatcher.group(3)));
        lCalendar.set(Calendar.HOUR_OF_DAY, lMatcher.group(4) == null
                ? 0
                : Integer.parseInt(lMatcher.group(4)));
        lCalendar.set(Calendar.MINUTE, lMatcher.group(5) == null
                ? 0
                : Integer.parseInt(lMatcher.group(5)));
        lCalendar.set(Calendar.SECOND, lMatcher.group(6) == null
                ? 0
                : Integer.parseInt(lMatcher.group(6)));
        lCalendar.set(Calendar.MILLISECOND, lMatcher.group(7) == null
                ? 0
                : Integer.parseInt(lMatcher.group(7)));

        return lCalendar.getTime();
    }
}
