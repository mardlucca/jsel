/*
 * File: DeclarativeEnvironmentRecord.java
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

import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.JSELValue;

import java.util.HashMap;
import java.util.Map;

public class DeclarativeEnvironmentRecord extends EnvironmentRecord {
    private Map<String, JSELValue> bindings = new HashMap<>();

    public DeclarativeEnvironmentRecord() {
    }

    public DeclarativeEnvironmentRecord(
            EnvironmentRecord aInOuter) {
        super(aInOuter);
    }

    @Override
    public void bind(String aInIdentifier, JSELValue aInValue) {
        bindings.put(aInIdentifier, aInValue.getValue());
    }

    @Override
    protected JSELValue resolveOwn(String aInIdentifier) {
        return bindings.get(aInIdentifier);
    }
}
