/*
 * File: FunctionConstructor.java
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
package mardlucca.jsel.builtin.function;

import mardlucca.jsel.*;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.expr.JSELExpression;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;
import mardlucca.jsel.JSELCompilationException;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.expr.JSELExpression;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELObject;
import mardlucca.jsel.type.JSELValue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static mardlucca.jsel.JSELRuntimeException.syntaxError;

public class FunctionConstructor extends JSELFunction {
    public FunctionConstructor() {
        super(JSELFunction.CLASS, Collections.singletonList("body"));

        defineOwnProperty(
                JSELFunction.PROTOTYPE, ExecutionContext.getFunctionPrototype(),
                false, false, false);
    }

    @Override
    public JSELValue call(JSELValue aInThis, List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {
        return instantiate(aInArguments, aInExecutionContext);
    }

    @Override
    public JSELObject instantiate(List<JSELValue> aInArguments,
                                  ExecutionContext aInExecutionContext) {
        try {
            JSELExpression lFunctionExpression =
                    JSELCompiler.getInstance()
                            .compile(getFunctionDefinition(aInArguments));

            return lFunctionExpression.execute(aInExecutionContext).toObject();
        }
        catch (IOException e) {
            // this should never happen
            throw new JSELRuntimeException("Runtime error");
        }
        catch (UnrecognizedCharacterSequenceException e) {
            throw JSELRuntimeException.syntaxError("Unexpected token " + e.getSequence());
        }
        catch (JSELCompilationException e) {
            throw JSELRuntimeException.syntaxError("Unexpected token " + e.getMessage());
        }
    }

    /**
     * @param aInArguments
     * @return
     */
    private static String getFunctionDefinition(
            List<JSELValue> aInArguments) {
        int lNumParams = aInArguments.size() < 2 ? 0 : aInArguments.size() - 1;

        String lParams = aInArguments.stream()
                .limit(lNumParams)
                .map(Object::toString)
                .map(aInS -> aInS.split(","))
                .flatMap(Arrays::stream)
                //TODO Validate that identifier is valid
                .collect(Collectors.joining(","));

        return "(" + lParams + ")=>" + (aInArguments.isEmpty()
                ? "undefined"
                : aInArguments.get(aInArguments.size() - 1));
    }
}
