/*
 * File: IsPrototypeOfFunction.java
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
import mardlucca.jsel.type.*;

import java.util.Collections;
import java.util.List;

public class IsPrototypeOfFunction extends JSELFunction {
    public static final String NAME = "isPrototypeOf";

    public IsPrototypeOfFunction() {
        super(NAME, Collections.singletonList("value"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInContext) {
        JSELValue lArgument = getArgument(aInArguments);
        if (lArgument.getType() != Type.OBJECT) { return JSELBoolean.FALSE; }

        return aInThis.toObject() == lArgument.toObject().getPrototype()
                ? JSELBoolean.TRUE
                : JSELBoolean.FALSE;
    }
}
