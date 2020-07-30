/*
 * File: ParseFunction.java
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

package mardlucca.jsel.builtin.json;

import mardlucca.jsel.JSELCompilationException;
import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.JSONCompiler;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.expr.JSELExpression;
import mardlucca.jsel.type.*;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static java.util.Arrays.asList;
import static mardlucca.jsel.JSELRuntimeException.syntaxError;

public class ParseFunction extends JSELFunction {
    public static final String NAME = "parse";

    public ParseFunction() {
        super(NAME, asList("text", "reviver"));
    }

    @Override
    public JSELValue call(JSELValue aInThis,
                          List<JSELValue> aInArguments,
                          ExecutionContext aInExecutionContext) {

        String lText = getArgument(aInArguments).toString();
        JSELExpression lExpression;
        try {
            lExpression = JSONCompiler.getInstance().compile(
                    new StringReader(lText));
        } catch (IOException e) {
            // this will never happen;
            throw new JSELRuntimeException(e.getMessage());
        } catch (JSELCompilationException
                | UnrecognizedCharacterSequenceException e) {
            throw syntaxError(e.getMessage());
        }

        JSELValue lValue = lExpression.execute(aInExecutionContext);
        JSELValue lReviver = getArgument(aInArguments, 1);
        if (!lReviver.isCallable()) {
            return lValue;
        }

        JSELObject lHolder = new JSELObject();
        lHolder.defineOwnProperty("", lValue, true, true, true, false);
        return walk(lHolder, JSELString.EMPTY_STRING, lReviver,
                aInExecutionContext);
    }

    private JSELValue walk(JSELObject aInHolder,
                           JSELString aInName,
                           JSELValue aInReviver,
                           ExecutionContext aInExecutionContext) {

        JSELValue lValue = aInHolder.get(aInName);

        if (lValue.isObjectClass(JSELArray.CLASS)) {
            JSELArray lArray = (JSELArray) lValue;
            int lLength = lArray.get(JSELArray.LENGTH).toInteger();

            for (int i = 0; i < lLength; i++) {
                String lStringIndex = String.valueOf(i);
                JSELValue lNewElement = walk(lArray,
                        new JSELString(lStringIndex),
                        aInReviver,
                        aInExecutionContext);
                if (lNewElement.getType() == Type.UNDEFINED) {
                    lArray.delete(lStringIndex, false);
                } else {
                    lArray.defineOwnProperty(lStringIndex, lNewElement,
                            true, true, true, false);
                }
            }
        } else if (lValue.getType() == Type.OBJECT) {
            JSELObject lObject = (JSELObject) lValue;
            for (String lName : lObject.getOwnPropertyNames()) {
                if (!lObject.getOwnProperty(lName).isEnumerable()) { continue; }

                JSELValue lNewElement = walk(lObject,
                        new JSELString(lName),
                        aInReviver,
                        aInExecutionContext);
                if (lNewElement.getType() == Type.UNDEFINED) {
                    lObject.delete(lName, false);
                } else {
                    lObject.defineOwnProperty(lName, lNewElement,
                            true, true, true, false);
                }
            }
        }

        return aInReviver.call(aInHolder, asList(aInName, lValue),
                aInExecutionContext);
    }
}
