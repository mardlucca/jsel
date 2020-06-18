/*
 * File: LiteralSupplierExpression.java
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
package mardlucca.jsel.expr;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.type.JSELValue;

import java.util.function.Supplier;

/**
 * Explain nuance why supplier is required (need to instantiate at runtime to
 * pick the right execution context, we can't instantiate at the time we build
 * the parse tree as the object's prototype is still not bound before an
 * execution takes place). Note that this does not apply to number, string,
 * boolean and null literals, as those are not objects so they don't have a
 * prototype, plus, they are immutable.
 */
public class LiteralSupplierExpression implements JSELExpression {
    private Supplier<JSELValue> valueSupplier;

    public LiteralSupplierExpression(
            Supplier<JSELValue> aInValueSupplier) {
        valueSupplier = aInValueSupplier;
    }

    @Override
    public JSELValue execute(ExecutionContext aInContext) {
        return valueSupplier.get();
    }
}
