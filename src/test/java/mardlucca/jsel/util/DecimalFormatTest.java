/*
 * File: DecimalFormatTest.java
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

import java.text.ParseException;

import static org.junit.Assert.*;

public class DecimalFormatTest
{
    @Test
    public void format()
    {
        assertEquals("0", DecimalFormat.format(0));
        assertEquals("0", DecimalFormat.format(-0.0));
        assertEquals("0.12345678901234568",
                DecimalFormat.format(0.12345678901234567890));
    }

    @Test
    public void parse() throws ParseException
    {
        assertEquals(0, DecimalFormat.parse("0"), 0.0);
        assertEquals(1.2e10, DecimalFormat.parse("1.2e10"), 0.0);
        assertEquals(-1.2e10, DecimalFormat.parse("-1.2e10"), 0.0);
        assertEquals(Double.NaN, DecimalFormat.parse("a"), 0.0);
        assertEquals(Double.POSITIVE_INFINITY,
                DecimalFormat.parse("Infinity"), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY,
                DecimalFormat.parse("-Infinity"), 0.0);
    }

}