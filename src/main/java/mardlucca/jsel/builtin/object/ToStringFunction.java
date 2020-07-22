/*
 * File: ToStringFunction.java
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
package mardlucca.jsel.builtin.object;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;

import java.util.List;

public class ToStringFunction extends JSELFunction {
    public static final String NAME = "toString";

    public ToStringFunction() {
        this(NAME);
    }

    protected ToStringFunction(String aInName) {
        super(aInName);
    }

    @Override
    public JSELString call(JSELValue aInThis, List<JSELValue> aInArguments,
                           ExecutionContext aInExecutionContext) {
        String lString;
        switch (aInThis.getType()) {
            case UNDEFINED: lString = "[object Undefined]"; break;
            case NULL: lString = "[object Null]"; break;
            default: lString = "[object "
                    + aInThis.toObject().getObjectClass() + "]";
        }
        return new JSELString(lString);
    }
}
