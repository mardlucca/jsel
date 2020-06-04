/*
 * File: DecimalFormat.java
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

public class DecimalFormat
{
    private static ThreadLocal<java.text.DecimalFormat> formatter =
            ThreadLocal.withInitial(() -> {
        java.text.DecimalFormat lFormat = new java.text.DecimalFormat();
        lFormat.setMaximumFractionDigits(17);
        lFormat.setGroupingUsed(false);
        return lFormat;
    });

    private DecimalFormat()
    {
    }

    public static String format(double aInDouble)
    {
        if (Double.isNaN(aInDouble) || Double.isInfinite(aInDouble))
        {
            return String.valueOf(aInDouble);
        }
        if (aInDouble == 0.0)
        {
            return "0";
        }
        return formatter.get().format(aInDouble);
    }

    public static double parse(String aInDouble)
    {
        aInDouble = aInDouble.trim();
        if (aInDouble.isEmpty())
        {
            return 0;
        }

        try
        {
            return Double.parseDouble(aInDouble);
        }
        catch (NumberFormatException ignore)
        {
            return Double.NaN;
        }
    }
}
