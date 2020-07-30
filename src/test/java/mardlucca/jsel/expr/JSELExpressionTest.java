/*
 * File: JSELExpressionTest.java
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

import mardlucca.jsel.*;
import mardlucca.jsel.type.Type;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;
import mardlucca.jsel.AbstractJSELExpressionTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

@SuppressWarnings({"JavaDoc", "HardCodedStringLiteral"})
public class JSELExpressionTest extends AbstractJSELExpressionTest {
    public static final String ARRAY_TEST_CASE =
            "[1.2,'str', true, false, undefined, null, {id:2}]";

    public static final String NESTED_ARRAY_TEST_CASE =
            "[1, " + ARRAY_TEST_CASE + "]";

    public static final String OBJECT_TEST_CASE =
            "{'number':1.2,\"str\":'str', 'true': true, 'false':false, " +
                    "'undefined':undefined, 'null':null, array: [1], " +
                    "'func':x=>x+1, 'int':10}";

    public static final String NESTED_OBJECT_TEST_CASE =
            "{object:" + OBJECT_TEST_CASE+ "}";

    @Before
    public void setUp() throws UnrecognizedCharacterSequenceException,
                               JSELCompilationException, IOException {
        runner.define("object",
                JSELCompiler.getInstance().getInstance().compile(OBJECT_TEST_CASE));
    }

    @Test
    public void testNumberExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("1", 1);
        testNumber("1.25e-3", 1.25e-3);
        testNumber("0x12", 0x12);
        testNumber("072", 072);
    }

    @Test
    public void testStringExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testString("\"test1\"", "test1");
        testString("\"\"", "");
        testString("'test1'", "test1");
        testString("''", "");
    }

    @Test
    public void testTrueExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("\ntrue ", true);
    }

    @Test
    public void testFalseExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("\nfalse ", false);
    }

    @Test
    public void testUndefinedExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testUndefined("\nundefined ");
    }

    @Test
    public void testNullExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNull("\nnull ");
    }

    @Test
    public void testArrayExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testArray(ARRAY_TEST_CASE,
                numberVerifier(1.2), stringVerifier("str"),
                booleanVerifier(true), booleanVerifier(false),
                this::verifyUndefined, this::verifyNull,
                objectVerifier(
                        propertyVerifier("id", numberVerifier(2))));
        // testing nested array
        testArray(
                NESTED_ARRAY_TEST_CASE,
                numberVerifier(1),
                arrayVerifier(
                        numberVerifier(1.2), stringVerifier("str"),
                        booleanVerifier(true), booleanVerifier(false),
                        this::verifyUndefined,
                        this::verifyNull, objectVerifier(
                                propertyVerifier("id", numberVerifier(2)))));

        // testing empty array
        testArray("[ ]");
    }

    @Test
    public void testObjectExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testObject(OBJECT_TEST_CASE,
                propertyVerifier("number", numberVerifier(1.2)),
                propertyVerifier("str", stringVerifier("str")),
                propertyVerifier("true", booleanVerifier(true)),
                propertyVerifier("false", booleanVerifier(false)),
                propertyVerifier("undefined", this::verifyUndefined),
                propertyVerifier("null", this::verifyNull),
                propertyVerifier("array", arrayVerifier(numberVerifier(1))));

        // testing nested object
        testObject(NESTED_OBJECT_TEST_CASE,
                propertyVerifier("object", objectVerifier(
                    propertyVerifier("number", numberVerifier(1.2)),
                    propertyVerifier("str", stringVerifier("str")),
                    propertyVerifier("true", booleanVerifier(true)),
                    propertyVerifier("false", booleanVerifier(false)),
                    propertyVerifier("undefined",
                            this::verifyUndefined),
                    propertyVerifier("null", this::verifyNull),
                    propertyVerifier("array",
                            arrayVerifier(numberVerifier(1))))));


        // testing empty object
        testObject("{ }");
    }

    @Test
    public void testObjectAccess() throws UnrecognizedCharacterSequenceException,
                                         JSELCompilationException, IOException {
        testNumber("object.number", 1.2);
        testNumber("object['number']", 1.2);
        testString("object.str", "str");
        testBoolean("object[\"true\"]", true);
        testBoolean("object['false']", false);
        testBoolean("object[false]", false);
        testUndefined("object2");
        testUndefined("object[undefined]");
        testUndefined("object['undefined']");
        testUndefined("object.blah");
        testUndefined("object['blah']");
        testNull("object['null']");
        testNull("object[null]");
        testNull("object[object['null']]");

        testArray("object['array']", numberVerifier(1));
        testNumber("object['array']['0']", 1);
        testNumber("object.array[0]", 1);
    }

    @Test
    public void testConditionalOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException,
                   IOException {
        testNumber("true ? 10 : object.array[0]", 10);
        testNumber("object[\"true\"] ? 10 : object.array[0]", 10);
        testNumber("object[false] ? 10 : object.array[0]", 1);
        testNumber("object[null] ? 10 : object.array[0]", 1);
        testNumber("object['blah'] ? 10 : object.array[0]", 1);
        testNumber("0 ? 10 : object.array[0]", 1);
        testNumber("1 ? 10 : object.array[0]", 10);
        testNumber("'' ? 10 : object.array[0]", 1);
        testNumber("'str' ? 10 : object.array[0]", 10);
    }

    @Test
    public void testAndOperator() throws UnrecognizedCharacterSequenceException,
                                         JSELCompilationException, IOException {
        testBoolean("true && true", true);
        testBoolean("1 && {}", true);
        testBoolean("true && false", false);
        testBoolean("true && ''", false);
        testBoolean("true && 0", false);
        testBoolean("true && null", false);
        testBoolean("undefined && true", false);
        testBoolean("false && true", false);
        testBoolean("false && false", false);
        // testing shortcut behavior
        testBoolean("false && null.blah", false);
    }

    @Test
    public void testOrOperator() throws UnrecognizedCharacterSequenceException,
                                         JSELCompilationException, IOException {
        testBoolean("true || true", true);
        testBoolean("0 || {}", true);
        testBoolean("true || false", true);
        testBoolean("false || ''", false);
        testBoolean("false || 1", true);
        testBoolean("null || true", true);
        testBoolean("undefined || true", true);
        testBoolean("false || true", true);
        testBoolean("false || false", false);
        // testing shortcut behavior
        testBoolean("true || null.blah", true);

        testBoolean("true || false && true", true);
        testBoolean("false || true && false", false);
        testBoolean("true && false || true", true);
    }

    @Test
    public void testBitwiseOrOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("0xf0 | 0xaa", 0xfa);
        testNumber("-1 | 10", -1);
        testNumber("1.2 | 1.7", 1);
        testNumber("1.2 | -1", -1);
        testNumber("true | 1", 1);
        testNumber("'10' | false", 10);
    }

    @Test
    public void testBitwiseXorOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("1 ^ 1 ", 0);
        testNumber("1 ^ false", 1);
        testNumber("'0' ^ true", 1);
        testNumber("0 ^ null", 0);
        testNumber("-1.2 ^ -1.3", 0);
    }

    @Test
    public void testBitwiseAndOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("0xf0 & 0xaa", 0xa0);
        testNumber("-1 & 10", 10);
        testNumber("1.2 & 1.7", 1);
        testNumber("1.2 & -1", 1);
        testNumber("true & 1", 1);
        testNumber("'10' & -1", 10);

        testNumber("0xa00 | 0x0aa ^ 0x0a0 & 0xf0f", 0xaaa);
        testNumber("0xf0f & 0x0a0 ^ 0x0aa | 0xa00", 0xaaa);
    }

    @Test
    public void testEqualsOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("NaN == NaN", false);
        testBoolean("Infinity == Infinity", true);
        testBoolean("1 == 1", true);
        testBoolean("1 == 0", false);
        testBoolean("true == 1", true);
        testBoolean("false == 1", false);
        testBoolean("true == '1'", true);
        testBoolean("0 == false", true);
        testBoolean("'0' == false", true);
        testBoolean("'0' == 1", false);
        testBoolean("'0' == 0", true);
        testBoolean("'1' == 1", true);
        testBoolean("{} == 1", false);
        testBoolean("{} == {}", false);
    }

    @Test
    public void testNotEqualsOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("NaN != NaN", true);
        testBoolean("Infinity != Infinity", false);
        testBoolean("1 != 1", false);
        testBoolean("1 != 0", true);
        testBoolean("true != 1", false);
        testBoolean("false != 1", true);
        testBoolean("true != '1'", false);
        testBoolean("0 != false", false);
        testBoolean("'0' != false", false);
        testBoolean("'0' != 1", true);
        testBoolean("'0' != 0", false);
        testBoolean("'1' != 1", false);
        testBoolean("{} != 1", true);
        testBoolean("{} != {}", true);
//        testBoolean("{valueOf:x=>1} != 1", true);
    }

    @Test
    public void testStrictEqualsOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("1 === 1", true);
        testBoolean("false === false", true);
        testBoolean("false === true", false);
        testBoolean("'string' === 'string'", true);
        testBoolean("1 === 0", false);
        testBoolean("true === 1", false);
        testBoolean("false === 1", false);
        testBoolean("true === '1'", false);
        testBoolean("0 === false", false);
        testBoolean("'0' === false", false);
        testBoolean("'0' === 1", false);
        testBoolean("'0' === 0", false);
        testBoolean("'1' === 1", false);
        testBoolean("{} === 1", false);
        testBoolean("{} === {}", false);
//        testBoolean("{valueOf:x=>1} === 1", false);
    }

    @Test
    public void testStrictNotEqualsOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("1 !== 1", false);
        testBoolean("false !== false", false);
        testBoolean("false !== true", true);
        testBoolean("'string' !== 'string'", false);
        testBoolean("1 !== 0", true);
        testBoolean("true !== 1", true);
        testBoolean("false !== 1", true);
        testBoolean("true !== '1'", true);
        testBoolean("0 !== false", true);
        testBoolean("'0' !== false", true);
        testBoolean("'0' !== 1", true);
        testBoolean("'0' !== 0", true);
        testBoolean("'1' !== 1", true);
        testBoolean("{} !== 1", true);
        testBoolean("{} !== {}", true);
//        testBoolean("{valueOf:x=>1} !== 1", true);

        testNumber("0xff & 0x0a == 0x0a", 1);
        testNumber("0x0a == 0x0a & 0xff", 1);
    }

    @Test
    public void testLessThanOrEqualTo()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("1 <= 0", false);
        testBoolean("1 <= 1", true);
        testBoolean("1 <= 2", true);
        testBoolean("1 <= 12", true);

        testBoolean("1 <= '00'", false);
        testBoolean("1 <= '01'", true);
        testBoolean("1 <= '02'", true);
        testBoolean("1 <= '12'", true);

        testBoolean("'1' <= '00'", false);
        testBoolean("'1' <= '1'", true);
        testBoolean("'1' <= '02'", false);
        testBoolean("'1' <= '12'", true);

        testBoolean("1 <= true", true);
        testBoolean("0 <= null", true);
    }

    @Test
    public void testLessThan()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("1 < 0", false);
        testBoolean("1 < 1", false);
        testBoolean("1 < 2", true);
        testBoolean("1 < 12", true);

        testBoolean("1 < '00'", false);
        testBoolean("1 < '01'", false);
        testBoolean("1 < '02'", true);
        testBoolean("1 < '12'", true);

        testBoolean("'1' < '00'", false);
        testBoolean("'1' < '1'", false);
        testBoolean("'1' < '02'", false);
        testBoolean("'1' < '12'", true);

        testBoolean("1 < true", false);
        testBoolean("0 < null", false);
    }

    @Test
    public void testGreaterThan()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("0 > 1", false);
        testBoolean("1 > 1", false);
        testBoolean("2 > 1", true);
        testBoolean("12 > 1", true);

        testBoolean("'00' > 1", false);
        testBoolean("'01' > 1", false);
        testBoolean("'02' > 1", true);
        testBoolean("'12' > 1", true);

        testBoolean("'00' > '1'", false);
        testBoolean("'1' > '1'", false);
        testBoolean("'02' > '1'", false);
        testBoolean("'12'> '1'", true);

        testBoolean("true > 1", false);
        testBoolean("null > 0", false);
    }

    @Test
    public void testGreaterThanOrEqualTo()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("0 >= 1", false);
        testBoolean("1 >= 1", true);
        testBoolean("2 >= 1", true);
        testBoolean("12 >= 1", true);

        testBoolean("'00' >= 1", false);
        testBoolean("'01' >= 1", true);
        testBoolean("'02' >= 1", true);
        testBoolean("'12' >= 1", true);

        testBoolean("'00' >= '1'", false);
        testBoolean("'1' >= '1'", true);
        testBoolean("'02' >= '1'", false);
        testBoolean("'12' >= '1'", true);

        testBoolean("true >= 1", true);
        testBoolean("null >= 0", true);

        testBoolean("undefined == null >= 1", false);
        testBoolean("(undefined == null) >= 1", true);
        testBoolean("1 <= null == undefined", false);
        testBoolean("1 <= (null == undefined)", true);
    }

    @Test
    public void testIn()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testBoolean("'number' in object", true);
        testBoolean("'blah' in object", false);
        testBoolean("'true' in object", true);
        testBoolean("true in object", true);

        try{
            testBoolean("'toString' in object.number", false);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals(
                    "Cannot use 'in' operator to search for 'toString' in 1.2",
                    e.getMessage());
        }
    }

    @Test
    public void testShiftLeft()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("1 << 3", 8);
        testNumber("-1 << '3'", -8);
        testNumber("1.2 << 3.2", 8);
        testNumber("-1.2 << 3.2", -8);
        testNumber("-1.2 << -3.5", -536870912);
        testNumber("1 << 31", Integer.MIN_VALUE);
        testNumber("1 << 32", 1);
        testNumber("1 << 33", 2);
        testNumber("1 << 34", 4);

        testBoolean("1 << 3 > 7", true);
        testBoolean("7 < 1 << 3", true);
        testNumber("(7 < 1) << 3", 0);
    }

    @Test
    public void testShiftRight()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("8 >> 3", 1);
        testNumber("-8 >> '3'", -1);
        testNumber("8 >> 3.2", 1);
        testNumber("-8 >> 3.2", -1);
        testNumber("8 >> -3", 0);
        testNumber("8 >> 4", 0);
        testNumber("8 >> 32", 8);
        testNumber("8 >> 33", 4);
    }

    @Test
    public void testUnsignedShiftRight()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("8 >>> 3", 1);
        testNumber("-8 >>> '2'", 0x3ffffffe);
        testNumber("8 >>> 3.2", 1);
        testNumber("-8 >>> 2.2", 0x3ffffffe);
        testNumber("8 >>> -3", 0);
        testNumber("8 >>> 4", 0);
        testNumber("8 >>> 32", 8);
        testNumber("8 >>> 33", 4);
    }

    @Test
    public void testPlus()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("0 + 1", 1);
        testNumber("1 + 1", 2);
        testNumber("2 + 1", 3);
        testNumber("12 + 1", 13);
        testNumber("NaN + 1", Double.NaN);
        testNumber("NaN + NaN", Double.NaN);
        testNumber("Infinity + 1", Double.POSITIVE_INFINITY);
        testNumber("Infinity + Infinity", Double.POSITIVE_INFINITY);
        testNumber("NaN + Infinity", Double.NaN);

        testString("'00' + 1", "001");
        testString("'01' + 1", "011");
        testString("'02' + 1", "021");
        testString("'12' + 1", "121");

        testString("'00' + '1'", "001");
        testString("'1' + '1'", "11");
        testString("'02' + '1'", "021");
        testString("'12'+ '1'", "121");
        testString("-1 + '1'", "-11");
        testString("-0 + '1'", "01");
        testString("1 + {}", "1[object Object]");

        testNumber("true + 1", 2);
        testNumber("null + 0", 0);
    }

    @Test
    public void testMinus()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("0 - 1", -1);
        testNumber("1 - 1", 0);
        testNumber("2 - 1", 1);
        testNumber("12 - 1", 11);
        testNumber("NaN - 1", Double.NaN);
        testNumber("NaN - NaN", Double.NaN);
        testNumber("NaN - Infinity", Double.NaN);
        testNumber("Infinity - 1", Double.POSITIVE_INFINITY);
        testNumber("Infinity - Infinity", Double.NaN);
        testNumber("1 - Infinity", Double.NEGATIVE_INFINITY);

        testNumber("'00' - 1", -1);
        testNumber("'01' - 1", 0);
        testNumber("'02' - 1", 1);
        testNumber("'12' - 1", 11);

        testNumber("'00' - '1'", -1);
        testNumber("'1' - '1'", 0);
        testNumber("'02' - '1'", 1);
        testNumber("'12'- '1'", 11);

        testNumber("true - 1", 0);
        testNumber("null - 0", 0);
        testNumber("'true' - 0", Double.NaN);
    }

    @Test
    public void testMultiplication()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("0 * 1", 0);
        testNumber("1 * 1", 1);
        testNumber("2 * 1", 2);
        testNumber("12 * 2", 24);
        testNumber("NaN * 1", Double.NaN);
        testNumber("NaN * NaN", Double.NaN);
        testNumber("NaN * Infinity", Double.NaN);
        testNumber("1 * Infinity", Double.POSITIVE_INFINITY);
        testNumber("Infinity * Infinity", Double.POSITIVE_INFINITY);
        testNumber("Infinity * -Infinity", Double.NEGATIVE_INFINITY);
        testNumber("-Infinity * -Infinity", Double.POSITIVE_INFINITY);

        testNumber("'00' * 1", 0);
        testNumber("'01' * 1", 1);
        testNumber("'02' * 1", 2);
        testNumber("'12' * 1", 12);

        testNumber("'00' * '1'", 0);
        testNumber("'1' * '1'", 1);
        testNumber("'02' * '1'", 2);
        testNumber("'12'* '2'", 24);

        testNumber("true * 1", 1);
        testNumber("null * 0", 0);
        testNumber("'true' * 0", Double.NaN);
    }

    @Test
    public void testDivision()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("0 / 1", 0);
        testNumber("1 / 1", 1);
        testNumber("2 / 1", 2);
        testNumber("12 / 2", 6);
        testNumber("1 / 0", Double.POSITIVE_INFINITY);
        testNumber("1 / -0", Double.NEGATIVE_INFINITY);
        testNumber("-1 / 0", Double.NEGATIVE_INFINITY);
        testNumber("-1 / -0", Double.POSITIVE_INFINITY);
        testNumber("1 / Infinity", 0);
        testNumber("Infinity / Infinity", Double.NaN);
        testNumber("-Infinity / Infinity", Double.NaN);
        testNumber("Infinity / -Infinity", Double.NaN);
        testNumber("-Infinity / -Infinity", Double.NaN);
        testNumber("NaN / 1", Double.NaN);
        testNumber("NaN / NaN", Double.NaN);

        testNumber("'00' / 1", 0);
        testNumber("'01' / 1", 1);
        testNumber("'02' / 1", 2);
        testNumber("'12' / 1", 12);

        testNumber("'00' / '1'", 0);
        testNumber("'1' / '1'", 1);
        testNumber("'02' / '1'", 2);
        testNumber("'12'/ '2'", 6);

        testNumber("true / 1", 1);
        testNumber("0 / 0", Double.NaN);
        testNumber("null / 0", Double.NaN);
        testNumber("'true' / 0", Double.NaN);

        testNumber("1 + 4 / 2", 3);
        testNumber("(1 + 4) / 2", 2.5);
        testNumber("4 / 2 + 1", 3);
        testNumber("4 / (2 + 2)", 1);
    }

    @Test
    public void testUnaryPlusOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("+1", 1);
        testNumber("+NaN", Double.NaN);
        testNumber("+true", 1);
        testNumber("+'20'", 20);
        testNumber("+null", 0);
        testNumber("+undefined", Double.NaN);
        testNumber("+'Infinity'", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testUnaryMinusOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("-1", -1);
        testNumber("-NaN", Double.NaN);
        testNumber("-true", -1);
        testNumber("-'20'", -20);
        testNumber("-0", 0);
        testNumber("-null", 0);
        testNumber("-undefined", Double.NaN);
        testNumber("-'-Infinity'", Double.POSITIVE_INFINITY);
        testBoolean("-0 == 0", true);
        testBoolean("(-1) == -1", true);
    }

    @Test
    public void testNotOperator() throws UnrecognizedCharacterSequenceException,
                                         JSELCompilationException, IOException {
        testBoolean("!true || true", true);
        testBoolean("0 || !{}", false);
        testBoolean("!true || false", false);
        testBoolean("false || !''", true);
        testBoolean("false || !1", false);
        testBoolean("!null && true", true);
        testBoolean("!undefined && true", true);
        testBoolean("!false && true", true);
        testBoolean("!(false || false)", true);
        testBoolean("! false && ! false", true);
        testBoolean("! NaN", true);
        testBoolean("! Infinity", false);
    }

    @Test
    public void testBitwiseNotOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testNumber("~0", -1);
        testNumber("~NaN", -1);
        testNumber("~Infinity", -1);
        testNumber("~(-1)", 0);
        testNumber("~-1", 0);
        testNumber("-~1", 2);
    }

    @Test
    public void testTypeOfOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testString("typeof true", Type.BOOLEAN.toString());
        testString("typeof 1", Type.NUMBER.toString());
        testString("typeof '1'", Type.STRING.toString());
        testString("typeof null", Type.NULL.toString());
        testString("typeof NaN", Type.NUMBER.toString());
        testString("typeof Infinity", Type.NUMBER.toString());
        testString("typeof undefined", Type.UNDEFINED.toString());
        testString("typeof {}", Type.OBJECT.toString());
        testString("typeof []", Type.OBJECT.toString());

        testString("typeof '1' + 1", Type.STRING.toString() + 1);
        testString("1 + typeof '1'", 1 + Type.STRING.toString());

        testString("typeof object.blah", "undefined");
        testString("typeof blah", "undefined");
        testString("typeof object.toString.prototype", "undefined");
        testString("typeof object.toString", "function");
        testBoolean("typeof object.func == 'function'", true);
        testBoolean("typeof object.func.prototype == 'object'", true);
    }

    @Test
    public void testVoidOperator()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testUndefined("void true");
        testUndefined("void 1");
        testUndefined("void '1'");
        testUndefined("void null");
        testUndefined("void NaN");
        testUndefined("void Infinity");
        testUndefined("void undefined");
        testUndefined("void {}");
        testUndefined("void []");
        try {
            testUndefined("void null.blah");
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("null cannot be converted to object", e.getMessage());
        }
    }

    @Test
    public void testFunctionCallExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testString("object.toString()", "[object Object]");
        testString("object.toString(1, 2, 3) + 2",
                "[object Object]2");
        testNumber("object.func(2)", 3);

        try {
            testUndefined("blah()");
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("undefined is not a function", e.getMessage());
        }
    }

    @Test
    public void testLambdaExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testFunction("(x)=>10", "x");
        testFunction("y=>10", "y");
        testFunction("(x, y)=>10", "x", "y");
        testFunction("()=>10");
        testFunction("x=>y=>x+1", "x");
        testFunction("object.func", "x");

        testBoolean("10 == {valueOf:() => 10}", true);
        testNumber("((x, y) => null).length", 2);
        testNumber("(()=>10).length", 0);
        testNumber("10 + {valueOf:() => 10}", 20);
        testNumber("1 + {valueOf:() => object.number}", 2.2);
        testNumber("{valueOf:() => object.number} + 1", 2.2);
        testString("{toString:() => object.str} + 1", "str1");
        // testing closures (x is the closure for y=>x+y+object.int) and object
        // is in global scope
        testBoolean("(x=>y=>x-y+object.int)(1)(2) == 9 " +
                "&& typeof x == 'undefined'", true);
    }

    @Test
    public void testStatementLambdaExpression()
            throws UnrecognizedCharacterSequenceException, IOException {
        try {
            JSELCompiler.getInstance().compile("x => {}");
            fail();
        }
        catch (JSELCompilationException e) {
            assertEquals("Compilation failed with the following errors: "
                    + "\nStatement lambdas are not supported", e.getMessage());
        }

        try {
            JSELCompiler.getInstance().compile("(x, y) => {}");
            fail();
        }
        catch (JSELCompilationException e) {
            assertEquals("Compilation failed with the following errors: "
                    + "\nStatement lambdas are not supported", e.getMessage());
        }
    }

        @Test
    public void testRegExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testRegExp("/a/", "/a/");
        testRegExp("/a/mig", "/a/mig");
        testRegExp("/a/gim", "/a/gim");
        testRegExp("/a[/gim]/mig", "/a[/gim]/mig");
        testRegExp("/a(\\n)/mig", "/a(\\n)/mig");
        testBoolean("typeof /a[/gim]/mig == 'object'", true);
        testString("/a[/gim]/mig + 1", "/a[/gim]/mig1");
        testNumber("/a[/gim]/mig - 1", Double.NaN);
        testBoolean("/a/==/a/", false);
        testBoolean("typeof /a/['test'] == 'function'", true);
        testBoolean("typeof (/a/).test == 'function'", true);
        testNumber("/a[/gim]/mig ? 1 : 2", 1);

        try {
            testNumber("/a/(1)", 1);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("/a/ is not a function", e.getMessage());
        }

    }

    @Test
    public void testExpressionWithComments()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        testRegExp("/a/ //this is a comment", "/a/");
        testRegExp("/a/ /*this is a comment*/", "/a/");
        testNumber("/a/ / /*this is a comment*/ /b/ ", Double.NaN);
    }


    @Test
    public void testEmptyExpression()
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        try {
            testUndefined("");
            fail();
        }
        catch (JSELCompilationException e) {
            assertEquals("Compilation failed with the following errors: "
                    + "\nSyntax error", e.getMessage());
        }

        try {
            testUndefined("// comment");
            fail();
        }
        catch (JSELCompilationException e) {
            assertEquals("Compilation failed with the following errors: "
                    + "\nSyntax error", e.getMessage());
        }

        try {
            testUndefined("/* multiline \n comment*/");
            fail();
        }
        catch (JSELCompilationException e) {
            assertEquals("Compilation failed with the following errors: "
                    + "\nSyntax error", e.getMessage());
        }
    }

}