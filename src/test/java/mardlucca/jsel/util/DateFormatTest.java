/*
 * File: DateFormatTest.java
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

import org.junit.*;

import java.time.ZoneOffset;
import java.util.TimeZone;

import static mardlucca.jsel.util.DateFormat.formatISO;
import static mardlucca.jsel.util.DateFormat.parse;
import static org.junit.Assert.assertEquals;

public class DateFormatTest {
    private static TimeZone defaultTimezone;

    @BeforeClass
    public static void beforeClass() {
        defaultTimezone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.of("-0500")));
    }

    @AfterClass
    public static void tearDown() {
        TimeZone.setDefault(defaultTimezone);
    }

    @Test
    public void testParseYear() {
        assertEquals("2020-01-01T00:00:00.000Z",
                formatISO(parse("2020")));
        assertEquals("2020-01-01T17:23:00.000Z",
                formatISO(parse("2020T12:23")));
        assertEquals("2020-01-02T04:45:30.000Z",
                formatISO(parse("2020T23:45:30")));
        assertEquals("2020-01-02T04:45:30.123Z",
                formatISO(parse("2020T23:45:30.123")));

        assertEquals("2020-01-01T12:23:00.000Z",
                formatISO(parse("2020T12:23Z")));
        assertEquals("2020-01-01T23:45:30.000Z",
                formatISO(parse("2020T23:45:30Z")));
        assertEquals("2020-01-01T23:45:30.123Z",
                formatISO(parse("2020T23:45:30.123Z")));

        assertEquals("2020-01-01T08:23:00.000Z",
                formatISO(parse("2020T12:23+0400")));
        assertEquals("2020-01-02T00:45:30.000Z",
                formatISO(parse("2020T23:45:30-01:00")));
        assertEquals("2020-01-02T03:45:30.123Z",
                formatISO(parse("2020T23:45:30.123-0400")));
    }

    @Test
    public void testParseYearMonth() {
        assertEquals("2020-02-01T00:00:00.000Z",
                formatISO(parse("2020-02")));
        assertEquals("2020-02-01T17:23:00.000Z",
                formatISO(parse("2020-02T12:23")));
        assertEquals("2020-02-02T04:45:30.000Z",
                formatISO(parse("2020-02T23:45:30")));
        assertEquals("2020-02-02T04:45:30.123Z",
                formatISO(parse("2020-02T23:45:30.123")));

        assertEquals("2020-02-01T12:23:00.000Z",
                formatISO(parse("2020-02T12:23Z")));
        assertEquals("2020-02-01T23:45:30.000Z",
                formatISO(parse("2020-02T23:45:30Z")));
        assertEquals("2020-02-01T23:45:30.123Z",
                formatISO(parse("2020-02T23:45:30.123Z")));

        assertEquals("2020-02-01T08:23:00.000Z",
                formatISO(parse("2020-02T12:23+0400")));
        assertEquals("2020-02-02T00:45:30.000Z",
                formatISO(parse("2020-02T23:45:30-01:00")));
        assertEquals("2020-02-02T03:45:30.123Z",
                formatISO(parse("2020-02T23:45:30.123-0400")));
    }

    @Test
    public void testParseYearMonthDay() {
        assertEquals("2020-02-10T00:00:00.000Z",
                formatISO(parse("2020-02-10")));
        assertEquals("2020-02-10T17:23:00.000Z",
                formatISO(parse("2020-02-10T12:23")));
        assertEquals("2020-02-11T04:45:30.000Z",
                formatISO(parse("2020-02-10T23:45:30")));
        assertEquals("2020-02-11T04:45:30.123Z",
                formatISO(parse("2020-02-10T23:45:30.123")));

        assertEquals("2020-02-10T12:23:00.000Z",
                formatISO(parse("2020-02-10T12:23Z")));
        assertEquals("2020-02-10T23:45:30.000Z",
                formatISO(parse("2020-02-10T23:45:30Z")));
        assertEquals("2020-02-10T23:45:30.123Z",
                formatISO(parse("2020-02-10T23:45:30.123Z")));

        assertEquals("2020-02-10T08:23:00.000Z",
                formatISO(parse("2020-02-10T12:23+0400")));
        assertEquals("2020-02-11T00:45:30.000Z",
                formatISO(parse("2020-02-10T23:45:30-01:00")));
        assertEquals("2020-02-11T03:45:30.123Z",
                formatISO(parse("2020-02-10T23:45:30.123-0400")));
    }

    @Test
    public void testParseInvalid() {
        assertEquals("2020-01-01T00:00:00.000Z",
                formatISO(parse("2020Z")));
        assertEquals("2019-12-31T20:00:00.000Z",
                formatISO(parse("2020+04:00")));
    }
}