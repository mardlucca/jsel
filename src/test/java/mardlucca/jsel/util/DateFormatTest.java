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

import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateFormatTest {

    @Test
    public void testToDateString() {
        System.out.println(DateFormat.toDateString(new Date()));
    }

    @Test
    public void testToTimeString() {
        System.out.println(DateFormat.toTimeString(new Date()));
    }

    @Test
    public void testFormat() {
        Date d = new Date();
        System.out.println(DateFormat.format(d));
        System.out.println(DateFormat.formatUTC(d));
        System.out.println(DateFormat.formatISO(d));
    }

    @Test
    public void name() {
        System.out.println(DateFormat.parse("1290-08-01T13:34:56.789-0500"));
        System.out.println(TimeZone.getDefault().getOffset(System.currentTimeMillis()) / (60*1000));
    }
}