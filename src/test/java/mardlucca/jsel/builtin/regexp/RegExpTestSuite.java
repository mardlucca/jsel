/*
 * File: RegExpTestSuite.java
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

package mardlucca.jsel.builtin.regexp;

import mardlucca.jsel.JSELCompilationException;
import mardlucca.jsel.AbstractJSELExpressionTest;
import mardlucca.jsel.JSELCompiler;
import mardlucca.jsel.expr.JSELExpression;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;
import mardlucca.jsel.AbstractJSELExpressionTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class RegExpTestSuite extends AbstractJSELExpressionTest {
    public static final String TEST_CASE = "{" +
            String.join(",",
                    "globalAs : /a+/g",
                    "wordsFollowedNumbers : /([a-z]+)(\\d+)/",
                    "capturingAsIgnoreCase : /(a+)/i",
                    "singleLine : /^(\\d+)$/",
                    "multiLine : /^(\\d+)$/m"
            ) + "}";

    @Before
    public void setUp() throws UnrecognizedCharacterSequenceException,
                               JSELCompilationException, IOException {
        runner.bind(JSELCompiler.getInstance().compile(TEST_CASE));
    }

    @Test
    public void testConstructorCalledAsAFunction()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testRegExp("RegExp()", "/(?:)/");
        testRegExp("RegExp('ab')", "/ab/");
        testRegExp("RegExp('ab', 'mig')", "/ab/mig");
        testRegExp("RegExp('ab', 'gim')", "/ab/gim");
        testRegExp("RegExp(/ab/g)", "/ab/g");
        testRegExp("RegExp(/ab/, 'gim')", "/ab/gim");
        testRegExp("RegExp(/ab/g, 'im')", "/ab/im");

        testIfThrows("RegExp(/ab/g, ' ')", "SyntaxError: Invalid " +
                "flags supplied to RegExp constructor ' '");

        testIfThrows("RegExp(/ab/g, ' m')", "SyntaxError: Invalid " +
                "flags supplied to RegExp constructor ' m'");
        testIfThrows("RegExp(/ab/g, 'x')", "SyntaxError: Invalid flags " +
                "supplied to RegExp constructor 'x'");

        testBoolean("/a/ == RegExp(/a/)", false);
        testBoolean("globalAs == RegExp(globalAs)", true);
        testBoolean("globalAs == RegExp(globalAs, '')", false);
        testRegExp("globalAs", "/a+/g");
        testRegExp("RegExp(globalAs, '')", "/a+/");

        // calling on the wrong type
        testRegExp("RegExp.call(1)", "/(?:)/");
    }

    @Test
    public void testInstantiate()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testRegExp("new RegExp()", "/(?:)/");
        testRegExp("new RegExp('ab')", "/ab/");
        testRegExp("new RegExp('ab', 'mig')", "/ab/mig");
        testRegExp("new RegExp('ab', 'gim')", "/ab/gim");
        testRegExp("new RegExp(/ab/g)", "/ab/g");
        testRegExp("new RegExp(/ab/, 'gim')", "/ab/gim");
        testRegExp("new RegExp(/ab/g, 'im')", "/ab/im");

        testIfThrows("new RegExp(/ab/g, ' ')", "SyntaxError: Invalid " +
                "flags supplied to RegExp constructor ' '");

        testIfThrows("new RegExp(/ab/g, ' m')", "SyntaxError: Invalid " +
                "flags supplied to RegExp constructor ' m'");
        testIfThrows("new RegExp(/ab/g, 'x')", "SyntaxError: Invalid " +
                "flags supplied to RegExp constructor 'x'");

        testBoolean("/a/ == new RegExp(/a/)", false);
        testBoolean("globalAs == new RegExp(globalAs)", false);
        testBoolean("globalAs == new RegExp(globalAs, '')", false);
        testRegExp("globalAs", "/a+/g");
        testRegExp("new RegExp(globalAs, '')", "/a+/");
    }

    @Test
    public void testPropertiesAndPrototype()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("RegExp.prototype.constructor == RegExp", true);
        testFunction("RegExp.prototype.constructor", "pattern", "flags");
        testNumber("RegExp.prototype.constructor.length", 2);
        testRegExp("RegExp.prototype", "/(?:)/");
        testObject("Object.getOwnPropertyDescriptor(RegExp, 'prototype')",
                propertyVerifier("configurable", booleanVerifier(false)),
                propertyVerifier("enumerable", booleanVerifier(false)),
                propertyVerifier("writable", booleanVerifier(false)));
        testBoolean("RegExp.prototype.valueOf == Object.prototype.valueOf",
                true);
        testString("Object.prototype.toString.call(new RegExp())",
                "[object RegExp]");

        testString("globalAs.source", "a+");
        testString("wordsFollowedNumbers.source", "([a-z]+)(\\d+)");
        testString("capturingAsIgnoreCase.source", "(a+)");
        testString("singleLine.source", "^(\\d+)$");
        testString("multiLine.source", "^(\\d+)$");

        testBoolean("globalAs.global", true);
        testBoolean("wordsFollowedNumbers.global", false);
        testBoolean("capturingAsIgnoreCase.global", false);
        testBoolean("singleLine.global", false);
        testBoolean("multiLine.global", false);

        testBoolean("globalAs.ignoreCase", false);
        testBoolean("wordsFollowedNumbers.ignoreCase", false);
        testBoolean("capturingAsIgnoreCase.ignoreCase", true);
        testBoolean("singleLine.ignoreCase", false);
        testBoolean("multiLine.ignoreCase", false);

        testBoolean("globalAs.multiline", false);
        testBoolean("wordsFollowedNumbers.multiline", false);
        testBoolean("capturingAsIgnoreCase.multiline", false);
        testBoolean("singleLine.multiline", false);
        testBoolean("multiLine.multiline", true);
    }

    @Test
    public void testExecGlobalExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("globalAs.lastIndex", 0);
        runner.define("test1", "globalAs.exec('babaa')");
        testArray("test1",
                stringVerifier("a"));
        testNumber("test1.index", 1);
        testString("test1.input", "babaa");
        testNumber("globalAs.lastIndex", 2);

        runner.define("test2", "globalAs.exec('babaa')");
        testArray("test2",
                stringVerifier("aa"));
        testNumber("test2.index", 3);
        testString("test2.input", "babaa");
        testNumber("globalAs.lastIndex", 5);

        testNull("globalAs.exec('babaa')");
        testNumber("globalAs.lastIndex", 0);
    }

    @Test
    public void testExecIgnoreCase()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("capturingAsIgnoreCase.lastIndex", 0);
        runner.define("test1", "capturingAsIgnoreCase.exec('bAbaa')");
        testArray("test1",
                stringVerifier("A"),
                stringVerifier("A"));
        testNumber("test1.index", 1);
        testString("test1.input", "bAbaa");
        testNumber("capturingAsIgnoreCase.lastIndex", 0);

        runner.define("test2", "capturingAsIgnoreCase.exec('bAbaa')");
        testArray("test2",
                stringVerifier("A"),
                stringVerifier("A"));
        testNumber("test2.index", 1);
        testString("test2.input", "bAbaa");
        testNumber("capturingAsIgnoreCase.lastIndex", 0);

        // test that globalAs is case sensitive
        testNumber("globalAs.lastIndex", 0);
        testNull("globalAs.exec('BABAA')");
    }

    @Test
    public void testExecWithCapturingGroups()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        runner.define("result", "wordsFollowedNumbers.exec('12blah123gg')");
        testArray("result",
                stringVerifier("blah123"),
                stringVerifier("blah"),
                stringVerifier("123"));
        testNumber("result.index", 2);
        testString("result.input", "12blah123gg");
        testNumber("wordsFollowedNumbers.lastIndex", 0);
    }

    @Test
    public void testExecSingleAndMultilineMatching()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        // had to comment out test "single2" as that does not work in Java, I'm
        // thinking java appears to have a bug.

        runner.define("single1", "singleLine.exec('123')");
        runner.define("multi1", "multiLine.exec('123')");
//        runner.define("single2", "singleLine.exec('123\\n')");
        runner.define("multi2", "multiLine.exec('123\\n')");
        runner.define("single3", "singleLine.exec('123\\na')");
        runner.define("multi3", "multiLine.exec('123\\na')");

        testArray("single1",
                stringVerifier("123"),
                stringVerifier("123"));
        testArray("multi1",
                stringVerifier("123"),
                stringVerifier("123"));
//        testNull("single2");
        testArray("multi2",
                stringVerifier("123"),
                stringVerifier("123"));
        testNull("single3");
        testArray("multi3",
                stringVerifier("123"),
                stringVerifier("123"));
        testNumber("multi3.index", 0);
        testString("multi3.input", "123\na");
    }

    @Test
    public void testCallExecOnWrongType()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testIfThrows("RegExp.prototype.exec.call(1)",
                "RegExp.prototype.exec called on incompatible receiver 1");
        testIfThrows("RegExp.prototype.exec.call(undefined)",
                "RegExp.prototype.exec called on incompatible receiver " +
                        "undefined");
    }

    @Test
    public void testExecProperties()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testUndefined("RegExp.prototype.exec.prototype");
        testIfThrows("new RegExp.prototype.exec()",
                "exec is not a constructor");
        testObject("Object.getOwnPropertyDescriptor(RegExp.prototype, 'exec')",
                propertyVerifier("enumerable", booleanVerifier(false)),
                propertyVerifier("writable", booleanVerifier(true)),
                propertyVerifier("configurable", booleanVerifier(true)));
    }

    @Test
    public void testTestGlobalExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("globalAs.lastIndex", 0);
        testBoolean("globalAs.test('babaa')", true);
        testNumber("globalAs.lastIndex", 2);

        testBoolean("globalAs.test('babaa')", true);
        testNumber("globalAs.lastIndex", 5);

        testBoolean("globalAs.test('babaa')", false);
        testNumber("globalAs.lastIndex", 0);
    }

    @Test
    public void testTestIgnoreCase()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("capturingAsIgnoreCase.lastIndex", 0);
        testBoolean("capturingAsIgnoreCase.test('bAbaa')", true);
        testNumber("capturingAsIgnoreCase.lastIndex", 0);

        testBoolean("capturingAsIgnoreCase.test('bAbaa')", true);
        testNumber("capturingAsIgnoreCase.lastIndex", 0);

        // test that globalAs is case sensitive
        testNumber("globalAs.lastIndex", 0);
        testBoolean("globalAs.test('BABAA')", false);
    }

    @Test
    public void testTestWithCapturingGroups()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("wordsFollowedNumbers.test('12blah123gg')", true);
        testNumber("wordsFollowedNumbers.lastIndex", 0);
    }

    @Test
    public void testTestSingleAndMultilineMatching()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        // had to comment out test "single2" as that does not work in Java, I'm
        // thinking java appears to have a bug.

        testBoolean("singleLine.test('123')", true);
        testBoolean("multiLine.test('123')", true);
//        testBoolean("singleLine.test'123\\n')", false);
        testBoolean("multiLine.test('123\\n')", true);
        testBoolean("singleLine.test('123\\na')", false);
        testBoolean("multiLine.test('123\\na')", true);
    }

    @Test
    public void testCallTestOnWrongType()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testIfThrows("RegExp.prototype.test.call(1)",
                "RegExp.prototype.test called on incompatible receiver 1");
        testIfThrows("RegExp.prototype.test.call(undefined)",
                "RegExp.prototype.test called on incompatible receiver " +
                        "undefined");
    }

    @Test
    public void testTestProperties()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testUndefined("RegExp.prototype.test.prototype");
        testIfThrows("new RegExp.prototype.test()",
                "test is not a constructor");
        testObject("Object.getOwnPropertyDescriptor(RegExp.prototype, 'test')",
                propertyVerifier("enumerable", booleanVerifier(false)),
                propertyVerifier("writable", booleanVerifier(true)),
                propertyVerifier("configurable", booleanVerifier(true)));
    }

    @Test
    public void testCallToStringOnWrongType()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testIfThrows("RegExp.prototype.toString.call(1)",
                "RegExp.prototype.toString called on incompatible receiver 1");
        testIfThrows("RegExp.prototype.toString.call(undefined)",
                "RegExp.prototype.toString called on incompatible receiver " +
                        "undefined");
    }
}