/*
 * File: JSELRunner.java
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
package mardlucca.jsel;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.expr.JSELExpression;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;

import java.io.IOException;

public class JSELRunner {
    private static final String VALUE = "value";

    private ExecutionContext executionContext = new ExecutionContext();

    public void bind(JSELExpression aInExpression) {
        JSELValue lValue = execute(aInExpression);
        if (lValue.getType() == Type.OBJECT) {
            JSELObject lObject = lValue.toObject();
            for (String lProperty : lObject.getOwnPropertyNames()) {
                executionContext.bind(lProperty, lObject.getOwn(lProperty));
            }
        } else {
            executionContext.bind(VALUE, lValue);
        }
    }

    public void define(String aInProperty, String aInExpression)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELValue lValue = execute(
                JSELCompiler.getInstance().compile(aInExpression));
        executionContext.bind(aInProperty, lValue);
    }


    public void define(String aInProperty, JSELExpression aInExpression) {
        JSELValue lValue = execute(aInExpression);
        executionContext.bind(aInProperty, lValue);
    }

    public JSELValue execute(JSELExpression aInExpression) {
        executionContext.setAsThreadContext();
        try {
            return aInExpression.execute(executionContext);
        }
        finally {
            ExecutionContext.clearThreadContext();
        }
    }
}
