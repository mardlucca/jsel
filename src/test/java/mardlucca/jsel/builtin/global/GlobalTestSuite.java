/*
 * File: GlobalTestSuite.java
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

package mardlucca.jsel.builtin.global;

import mardlucca.jsel.AbstractJSELExpressionTest;
import mardlucca.jsel.JSELCompilationException;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;
import mardlucca.jsel.AbstractJSELExpressionTest;
import org.junit.Test;

import java.io.IOException;

public class GlobalTestSuite extends AbstractJSELExpressionTest {
    @Test
    public void testConstructorCalledAsAFunction()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testString("String()", "");
        testString("String(1)", "1");
        testString("String(true)", "true");
        testString("String({})", "[object Object]");
        testString("typeof String()", "string");

        // calling on the wrong type
        testString("String.call(1)", "");
    }

    @Test
    public void testInstantiate()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("new String() == ''", true);
        testString("typeof new String()", "object");
        testBoolean("new String(1) == '1'", true);
        testBoolean("new String(true) == 'true'", true);
        testBoolean("new String({}) == '[object Object]'", true);
        testBoolean("new String(1) === '1'", false);
        testBoolean("new String(true) === 'true'", false);
        testBoolean("new String({}) === '[object Object]'", false);
    }

    @Test
    public void testPropertiesAndPrototype()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("String.prototype.constructor == String", true);
        testFunction("String.prototype.constructor", "value");
        testNumber("String.prototype.constructor.length", 1);
        testString("String.prototype.toString()", "");
        testObject("Object.getOwnPropertyDescriptor(String, 'prototype')",
                propertyVerifier("configurable", booleanVerifier(false)),
                propertyVerifier("enumerable", booleanVerifier(false)),
                propertyVerifier("writable", booleanVerifier(false)));
        testString("Object.prototype.toString.call(new String())",
                "[object String]");

        // test properties
        testNumber("''.length", 0);
        testNumber("'1'.length", 1);
        testNumber("'12'.length", 2);
        testString("'test'[2]", "s");
        testObject("Object.getOwnPropertyDescriptor(String, 'length')",
                propertyVerifier("configurable", booleanVerifier(false)),
                propertyVerifier("enumerable", booleanVerifier(false)),
                propertyVerifier("writable", booleanVerifier(false)));
    }
}