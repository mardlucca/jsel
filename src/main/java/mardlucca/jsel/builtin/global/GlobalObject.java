/*
 * File: GlobalObject.java
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

import mardlucca.jsel.builtin.array.ArrayPrototype;
import mardlucca.jsel.builtin.bool.BooleanPrototype;
import mardlucca.jsel.builtin.function.FunctionPrototype;
import mardlucca.jsel.builtin.json.JSONObject;
import mardlucca.jsel.builtin.math.MathObject;
import mardlucca.jsel.builtin.number.NumberPrototype;
import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.builtin.regexp.RegExpPrototype;
import mardlucca.jsel.builtin.string.StringPrototype;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELRegExp;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.wrapper.JSELBooleanObject;
import mardlucca.jsel.type.wrapper.JSELNumberObject;
import mardlucca.jsel.type.wrapper.JSELStringObject;

public class GlobalObject extends JSELObject {
    public static final String CLASS = "Global";

    public static final String NAN_PROPERTY = "NaN";
    public static final String INFINITY_PROPERTY = "Infinity";
    public static final String UNDEFINED_PROPERTY = "undefined";

    private ObjectPrototype objectPrototype;
    private FunctionPrototype functionPrototype;
    private BooleanPrototype booleanPrototype;
    private NumberPrototype numberPrototype;
    private StringPrototype stringPrototype;
    private ArrayPrototype arrayPrototype;
    private RegExpPrototype regExpPrototype;

    private boolean initialized = false;

    public GlobalObject() {
        super(new ObjectPrototype());
        objectPrototype = (ObjectPrototype) getPrototype();
        functionPrototype = new FunctionPrototype(objectPrototype);
        booleanPrototype = new BooleanPrototype(objectPrototype);
        numberPrototype = new NumberPrototype(objectPrototype);
        stringPrototype = new StringPrototype(objectPrototype);
        arrayPrototype = new ArrayPrototype(objectPrototype);
        regExpPrototype = new RegExpPrototype(objectPrototype);
    }

    public void initialize() {
        if (!initialized) {
            objectPrototype.initialize();
            functionPrototype.initialize();
            booleanPrototype.initialize();
            numberPrototype.initialize();
            stringPrototype.initialize();
            arrayPrototype.initialize();
            regExpPrototype.initialize();

            defineOwnProperty(NAN_PROPERTY, JSELNumber.NAN,
                    false, false, false);
            defineOwnProperty(INFINITY_PROPERTY, JSELNumber.INFINITY,
                    false, false, false);
            defineOwnProperty(UNDEFINED_PROPERTY, JSELUndefined.getInstance(),
                    false, false, false);

            // Adding functions
            defineOwnProperty(ParseIntFunction.NAME, new ParseIntFunction(),
                    false, true, true);
            defineOwnProperty(ParseFloatFunction.NAME, new ParseFloatFunction(),
                    false, true, true);
            defineOwnProperty(IsNaNFunction.NAME, new IsNaNFunction(),
                    false, true, true);
            defineOwnProperty(IsFiniteFunction.NAME, new IsFiniteFunction(),
                    false, true, true);
            defineOwnProperty(DecodeURIFunction.NAME,
                    new DecodeURIFunction(),
                    false, true, true);
            defineOwnProperty(DecodeURIComponentFunction.NAME,
                    new DecodeURIComponentFunction(),
                    false, true, true);
            defineOwnProperty(EncodeURIFunction.NAME,
                    new EncodeURIFunction(),
                    false, true, true);
            defineOwnProperty(EncodeURIComponentFunction.NAME,
                    new EncodeURIComponentFunction(),
                    false, true, true);

            // Adding built in constructors to global object
            defineOwnProperty(JSELArray.CLASS, arrayPrototype.get(
                    ObjectPrototype.CONSTRUCTOR_PROPERTY),
                    false, true, true);
            defineOwnProperty(JSELBooleanObject.CLASS, booleanPrototype.get(
                    ObjectPrototype.CONSTRUCTOR_PROPERTY),
                    false, true, true);
            defineOwnProperty(JSELFunction.CLASS, functionPrototype.get(
                    ObjectPrototype.CONSTRUCTOR_PROPERTY),
                    false, true, true);
            defineOwnProperty(JSELNumberObject.CLASS, numberPrototype.get(
                    ObjectPrototype.CONSTRUCTOR_PROPERTY),
                    false, true, true);
            defineOwnProperty(JSELObject.CLASS, objectPrototype.get(
                    ObjectPrototype.CONSTRUCTOR_PROPERTY),
                    false, true, true);
            defineOwnProperty(JSELStringObject.CLASS, stringPrototype.get(
                    ObjectPrototype.CONSTRUCTOR_PROPERTY),
                    false, true, true);
            defineOwnProperty(JSELRegExp.CLASS, regExpPrototype.get(
                    ObjectPrototype.CONSTRUCTOR_PROPERTY),
                    false, true, true);

            // Adding additional global objects
            defineOwnProperty(MathObject.CLASS, new MathObject(),
                    false, true, true);
            defineOwnProperty(JSONObject.CLASS, new JSELObject(),
                    false, true, true);

            initialized = true;
        }
    }

    @Override
    public String getObjectClass() {
        return CLASS;
    }

    public JSELObject getObjectPrototype() {
        return objectPrototype;
    }

    public JSELObject getFunctionPrototype() {
        return functionPrototype;
    }

    public JSELObject getBooleanPrototype() {
        return booleanPrototype;
    }

    public JSELObject getNumberPrototype() {
        return numberPrototype;
    }

    public JSELObject getStringPrototype() {
        return stringPrototype;
    }

    public JSELObject getArrayPrototype() {
        return arrayPrototype;
    }

    public JSELObject getRegExpPrototype() {
        return regExpPrototype;
    }
}
