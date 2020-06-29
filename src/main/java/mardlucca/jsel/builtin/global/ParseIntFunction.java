/*
 * File: ParseIntFunction.java
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

package mardlucca.jsel.builtin.global;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.List;

import static java.util.Arrays.asList;
import static mardlucca.jsel.JSELRuntimeException.notImplemented;

public class ParseIntFunction extends JSELFunction {
    public static final String NAME = "parseInt";

    public ParseIntFunction() {
        super(NAME, asList("string", "radix"));
    }

    @Override
    public JSELNumber call(JSELValue aInThis, List<JSELValue> aInArguments,
                            ExecutionContext aInExecutionContext) {
        String lString = getArgument(aInArguments).toString().trim();
        JSELValue lRadix = getArgument(aInArguments, 1);

        int lIntRadix;
        if (lRadix == JSELUndefined.getInstance()) {
            lIntRadix = 10;
        } else {
            lIntRadix = lRadix.toInt32();
        }

        if (lIntRadix == 0) {
            lIntRadix = 10;
        } else if (lIntRadix < 2 || lIntRadix > 36) {
            return JSELNumber.NAN;
        }

        if (lIntRadix == 16
                && (lString.startsWith("0x") || lString.startsWith("0X"))) {
            lString = lString.substring(2);
        }

        int lValidIndex = 0;
        while (lValidIndex < lString.length()
                && isValid(lString.charAt(lValidIndex), lIntRadix)) {
            lValidIndex++;
        }
        lString = lString.substring(0, lValidIndex);

        if (lString.isEmpty()) { return JSELNumber.NAN; }

        try {
            // Here we know for sure string container only valid characters
            return new JSELNumber(Long.parseLong(lString, lIntRadix));
        } catch (NumberFormatException nfe) {
            // A number format exception indicates an overflow. We will simply
            // return the largest long. This is not in the spec, but should
            // suffice for JSEL.
            return lString.charAt(0) == '-'
                    ? new JSELNumber(Long.MIN_VALUE)
                    : new JSELNumber(Long.MAX_VALUE);
        }
    }

    private static boolean isValid(char aInChar, int aInIntRadix) {
        if (aInChar >= '0' && aInChar <= '9') {
            return (aInChar - '0') < aInIntRadix;
        }
        if (aInChar >= 'A' && aInChar <= 'Z') {
            return (aInChar - 'A' + 10) < aInIntRadix;
        }
        if (aInChar >= 'a' && aInChar <= 'z') {
            return (aInChar - 'a' + 10) < aInIntRadix;
        }
        return false;
    }
}
