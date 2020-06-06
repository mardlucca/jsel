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
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.*;

import java.util.List;

import static java.util.Arrays.asList;

public class GetOwnPropertyDescriptorFunction extends JSELFunction {
    public static final String GET_OWN_PROPERTY_DESCRIPTOR =
            "getOwnPropertyDescriptor";
    public static final String CONFIGURABLE = "configurable";
    public static final String ENUMERABLE = "enumerable";
    public static final String WRITABLE = "writable";

    public GetOwnPropertyDescriptorFunction() {
        super(GET_OWN_PROPERTY_DESCRIPTOR, asList("o", "propertyKey"));
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        PropertyDescriptor lDescriptor =
                getArgument(aInArguments).toObject().getOwnProperty(
                        getArgument(aInArguments, 1).toString());
        if (lDescriptor == null) {
            return JSELUndefined.getInstance();
        }

        JSELObject lObject = new JSELObject();
        lObject.put(CONFIGURABLE,
                new JSELBoolean(lDescriptor.isConfigurable()));
        lObject.put(ENUMERABLE, new JSELBoolean(lDescriptor.isEnumerable()));
        lObject.put(WRITABLE, new JSELBoolean(lDescriptor.isWritable()));

        return lObject;
    }
}
