/*
 * File: GetOwnPropertyDescriptorFunction.java
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

import java.util.List;

import static java.util.Arrays.asList;
import static mardlucca.jsel.JSELRuntimeException.typeError;

public class GetOwnPropertyDescriptorFunction extends JSELFunction {
    public static final String NAME =
            "getOwnPropertyDescriptor";
    public static final String CONFIGURABLE = "configurable";
    public static final String ENUMERABLE = "enumerable";
    public static final String VALUE= "value";
    public static final String WRITABLE = "writable";

    public GetOwnPropertyDescriptorFunction() {
        super(NAME, asList("o", "propertyKey"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        JSELValue lArgument = getArgument(aInArguments);
        if (lArgument.getType() != Type.OBJECT) {
            throw typeError("Argument is not an object");
        }
        String lProperty = getArgument(aInArguments, 1).toString();
        PropertyDescriptor lDescriptor =
                lArgument.toObject().getOwnProperty(lProperty);
        if (lDescriptor == null) {
            return JSELUndefined.getInstance();
        }

        JSELObject lObject = new JSELObject();
        lObject.defineOwnProperty(VALUE, lDescriptor.getValue(),
                true, true, true, false);
        lObject.defineOwnProperty(CONFIGURABLE,
                new JSELBoolean(lDescriptor.isConfigurable()),
                true, true, true, false);
        lObject.defineOwnProperty(ENUMERABLE,
                new JSELBoolean(lDescriptor.isEnumerable()),
                true, true, true, false);
        lObject.defineOwnProperty(WRITABLE,
                new JSELBoolean(lDescriptor.isWritable()),
                true, true, true, false);


        return lObject;
    }
}
