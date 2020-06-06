/*
 * File: ObjectEnvironmentRecord.java
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
package mardlucca.jsel.env;

import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELObject.PropertyDescriptor;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELObject.PropertyDescriptor;
import mardlucca.jsel.type.JSELValue;

public class ObjectEnvironmentRecord extends EnvironmentRecord {
    private JSELObject bindingObject;

    public ObjectEnvironmentRecord(
            JSELObject aInBindingObject) {
        this(null, aInBindingObject);
    }

    public ObjectEnvironmentRecord(
            EnvironmentRecord aInOuter, JSELObject aInBindingObject) {
        super(aInOuter);
        bindingObject = aInBindingObject;
    }

    @Override
    public void bind(String aInIdentifier, JSELValue aInValue) {
        bindingObject.put(aInIdentifier, aInValue.getValue());
    }

    @Override
    protected JSELValue resolveOwn(String aInIdentifier) {
        PropertyDescriptor lDescriptor =
                bindingObject.getProperty(aInIdentifier);
        return lDescriptor == null ? null : lDescriptor.getValue();
    }
}
