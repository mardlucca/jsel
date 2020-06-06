/*
 * File: StringTestSuite.java
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

package mardlucca.jsel.builtin.string;

import mardlucca.jsel.AbstractJSELExpressionTest;
import mardlucca.jsel.JSELCompilationException;
import mardlucca.jsel.expr.JSELExpression;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;
import mardlucca.jsel.AbstractJSELExpressionTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class StringTestSuite extends AbstractJSELExpressionTest {
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

    @Test
    public void testFromCharCode()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testString("String.fromCharCode(97)", "a");
        testString("String.fromCharCode(97, '98', {valueOf:x=>99})", "abc");
        testString("String.fromCharCode(10)", "\n");
        testString("String.fromCharCode()", "");
    }

    @Test
    public void testFromCharCodeProperties()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testFunction("String.fromCharCode", "chars");
        testUndefined("String.fromCharCode.prototype");
        testIfThrows("new String.fromCharCode()",
                "fromCharCode is not a constructor");
        testObject("Object.getOwnPropertyDescriptor(String, 'fromCharCode')",
                propertyVerifier("enumerable", booleanVerifier(false)),
                propertyVerifier("writable", booleanVerifier(true)),
                propertyVerifier("configurable", booleanVerifier(true)));
    }

    @Test
    public void testToString()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testString("'1'.toString()", "1");
        runner.define("number", "1");
        runner.define("ignore", "'ignore me'");
        testString("number.toString(ignore)", "1");

        testString("1.0.toString()", "1");
        testString("true.toString()", "true");
        testString("String.prototype.toString.call('1')", "1");

    }

    @Test
    public void testToStringProperties()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testFunction("String.prototype.toString");
        testUndefined("String.prototype.toString.prototype");
        testIfThrows("new String.prototype.toString()",
                "toString is not a constructor");
        testObject("Object.getOwnPropertyDescriptor(" +
                        "String.prototype, 'toString')",
                propertyVerifier("enumerable", booleanVerifier(false)),
                propertyVerifier("writable", booleanVerifier(true)),
                propertyVerifier("configurable", booleanVerifier(true)));
    }


    @Test
    public void testCallToStringOnWrongType()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testIfThrows("String.prototype.toString.call(1)",
                "String.prototype.toString requires that 'this' be a String");
        testIfThrows("String.prototype.toString.call(undefined)",
                "String.prototype.toString requires that 'this' be a String");
    }

    @Test
    public void testValueOf()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testString("'1'.valueOf()", "1");
        testString("new String('1').valueOf()", "1");
    }

    @Test
    public void testValueOfProperties()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testFunction("String.prototype.valueOf");
        testUndefined("String.prototype.valueOf.prototype");
        testIfThrows("new String.prototype.valueOf()",
                "valueOf is not a constructor");
        testObject("Object.getOwnPropertyDescriptor(" +
                        "String.prototype, 'valueOf')",
                propertyVerifier("enumerable", booleanVerifier(false)),
                propertyVerifier("writable", booleanVerifier(true)),
                propertyVerifier("configurable", booleanVerifier(true)));
    }


    @Test
    public void testCallValueOfOnWrongType()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testIfThrows("String.prototype.valueOf.call(1)",
                "String.prototype.valueOf requires that 'this' be a String");
        testIfThrows("String.prototype.valueOf.call(null)",
                "String.prototype.valueOf requires that 'this' be a String");
    }

    @Test
    public void testCharAt()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testString("'tests'.charAt(-1)", "");
        testString("'tests'.charAt(0)", "t");
        testString("'tests'.charAt(NaN)", "t");
        testString("'tests'.charAt(4)", "s");
        testString("'tests'.charAt(4.9)", "s");
        testString("'tests'.charAt(5)", "");

        // prototype is also a string with value '', so charAt returns empty
        testString("String.prototype.charAt(1)", "");
    }

    @Test
    public void testCharAtProperties()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testFunction("String.prototype.charAt");
        testUndefined("String.prototype.charAt.prototype");
        testIfThrows("new String.prototype.charAt()",
                "charAt is not a constructor");
        testObject("Object.getOwnPropertyDescriptor(" +
                        "String.prototype, 'charAt')",
                propertyVerifier("enumerable", booleanVerifier(false)),
                propertyVerifier("writable", booleanVerifier(true)),
                propertyVerifier("configurable", booleanVerifier(true)));
    }


    @Test
    public void testCallCharAtOnWrongType()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testString("String.prototype.charAt.call(1, 0)", "1");
        testString("String.prototype.charAt.call(true, 2)", "u");
        testString("String.prototype.charAt.call(true, 2)", "u");
        testIfThrows("String.prototype.charAt.call(null)",
                "String.prototype.charAt called on null or undefined");

        runner.define("charAt", "String.prototype.charAt");
        testIfThrows("charAt(1)",
                "String.prototype.charAt called on null or undefined");
    }
}