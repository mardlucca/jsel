/*
 * File: CharCodeAtFunction.java
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
package mardlucca.jsel.builtin.string;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;

import java.util.List;

import static mardlucca.jsel.JSELRuntimeException.typeError;

public class CharCodeAtFunction extends JSELFunction {
    public static final String CHAR_CODE_AT = "charCodeAt";

    public CharCodeAtFunction() {
        super(CHAR_CODE_AT);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        if (aInThis.getType() == Type.NULL
                || aInThis.getType() == Type.UNDEFINED) {
            throw JSELRuntimeException.typeError(
                    "String.prototype.charCodeAt called on null or undefined");
        }
        
        String lString = aInThis.toString();
        int lPos = getArgument(aInArguments).toInteger();
        if (lPos >= lString.length()) {
            return JSELNumber.NAN;
        }
        
        return new JSELNumber(lString.charAt(lPos));
    }
}
