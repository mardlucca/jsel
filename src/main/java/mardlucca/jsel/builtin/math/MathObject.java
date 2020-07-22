/*
 * File: MathObject.java
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

package mardlucca.jsel.builtin.math;

import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import sun.rmi.runtime.Log;

import java.nio.channels.AcceptPendingException;

public class MathObject extends JSELObject {
    public static final String CLASS = "Math";
    public static final String E_PROPERTY = "E";
    public static final String LN10_PROPERTY = "LN10";
    public static final String LN2_PROPERTY = "LN2";
    public static final String LOG2E_PROPERTY = "LOG2E";
    public static final String LOG10E_PROPERTY = "LOG10E";
    public static final String PI_PROPERTY = "PI";
    public static final String SQRT1_2 = "SQRT1_2";
    public static final String SQRT2 = "SQRT2";

    @Override
    public String getObjectClass() {
        return CLASS;
    }

    public MathObject() {
        defineOwnProperty(E_PROPERTY, new JSELNumber(2.7182818284590452354D),
                false, false, false);
        defineOwnProperty(LN10_PROPERTY, new JSELNumber(2.302585092994046D),
                false, false, false);
        defineOwnProperty(LN2_PROPERTY, new JSELNumber(0.6931471805599453D),
                false, false, false);
        defineOwnProperty(LOG2E_PROPERTY, new JSELNumber(1.4426950408889634D),
                false, false, false);
        defineOwnProperty(LOG10E_PROPERTY, new JSELNumber(0.4342944819032518D),
                false, false, false);
        defineOwnProperty(PI_PROPERTY, new JSELNumber(3.1415926535897932D),
                false, false, false);
        defineOwnProperty(SQRT1_2, new JSELNumber(0.7071067811865476D),
                false, false, false);
        defineOwnProperty(SQRT2, new JSELNumber(1.4142135623730951D),
                false, false, false);

        // function definitions
        defineOwnProperty(AbsFunction.NAME, new AbsFunction(),
                false, true, true);
        defineOwnProperty(AcosFunction.NAME, new AcosFunction(),
                false, true, true);
        defineOwnProperty(AsinFunction.NAME, new AsinFunction(),
                false, true, true);
        defineOwnProperty(AtanFunction.NAME, new AtanFunction(),
                false, true, true);
        defineOwnProperty(Atan2Function.NAME, new Atan2Function(),
                false, true, true);
        defineOwnProperty(CeilFunction.NAME, new CeilFunction(),
                false, true, true);
        defineOwnProperty(CosFunction.NAME, new CosFunction(),
                false, true, true);
        defineOwnProperty(ExpFunction.NAME, new ExpFunction(),
                false, true, true);
        defineOwnProperty(FloorFunction.NAME, new FloorFunction(),
                false, true, true);
        defineOwnProperty(LogFunction.NAME, new LogFunction(),
                false, true, true);
        defineOwnProperty(MaxFunction.NAME, new MaxFunction(),
                false, true, true);
        defineOwnProperty(MinFunction.NAME, new MinFunction(),
                false, true, true);
        defineOwnProperty(PowFunction.NAME, new PowFunction(),
                false, true, true);
        defineOwnProperty(RandomFunction.NAME, new RandomFunction(),
                false, true, true);
        defineOwnProperty(RoundFunction.NAME, new RoundFunction(),
                false, true, true);
        defineOwnProperty(SinFunction.NAME, new SinFunction(),
                false, true, true);
        defineOwnProperty(SqrtFunction.NAME, new SqrtFunction(),
                false, true, true);
        defineOwnProperty(TanFunction.NAME, new TanFunction(),
                false, true, true);
    }

}
