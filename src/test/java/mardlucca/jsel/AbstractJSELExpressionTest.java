/*
 * File: AbstractJSELExpressionTest.java
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

import mardlucca.jsel.expr.JSELExpression;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELFunction;
import mardlucca.jsel.type.JSELNull;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELPropertyReference;
import mardlucca.jsel.type.JSELRegExp;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELUndefined;
import mardlucca.jsel.type.JSELValue;
import mardlucca.jsel.type.Type;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class AbstractJSELExpressionTest {
    protected JSELRunner runner = new JSELRunner();

    protected void testNumber(String aInNumberString, double aInExpected)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(
                "\n" + aInNumberString + " ");
        assertNotNull(lExpression);
        JSELValue lResult = runner.execute(lExpression);
        verifyNumber(lResult, aInExpected);
    }

    protected void testString(String aInString, String aInExpected)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(
                "\n" + aInString + " ");
        assertNotNull(lExpression);
        JSELValue lResult = runner.execute(lExpression);
        verifyString(lResult, aInExpected);
    }

    protected void testBoolean(String aInString, boolean aInExpected)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(
                "\n" + aInString + " ");
        assertNotNull(lExpression);
        JSELValue lResult = runner.execute(lExpression);
        verifyBoolean(lResult, aInExpected);
    }

    protected void testUndefined(String aInString)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(
                "\n" + aInString + " ");
        assertNotNull(lExpression);
        JSELValue lResult = runner.execute(lExpression);
        verifyUndefined(lResult);
    }

    protected void testNull(String aInString)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(aInString);
        assertNotNull(lExpression);
        JSELValue lResult = runner.execute(lExpression);
        verifyNull(lResult);
    }

    protected void testFunction(String aInString, String ... aInParameters)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(aInString);
        assertNotNull(lExpression);
        JSELValue lResult = runner.execute(lExpression);
        verifyFunction(lResult, aInParameters);
    }

    protected void testArray(String aInString, Verifier... aInVerifiers)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(
                "\n" + aInString + " ");
        assertNotNull(lExpression);
        JSELValue lResult = runner.execute(lExpression);
        verifyArray(lResult, aInVerifiers);
    }

    protected void testObject(String aInString, Verifier... aInVerifiers)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(
                "\n" + aInString + " ");
        assertNotNull(lExpression);
        JSELValue lResult = runner.execute(lExpression);
        verifyObject(lResult, aInVerifiers);
    }

    protected void testRegExp(String aInRegExpString,
            String aInExpectedToString)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(
                "\n" + aInRegExpString + " ");
        assertNotNull(lExpression);
        JSELValue lResult = runner.execute(lExpression);
        verifyRegExp(lResult, aInExpectedToString);
    }

    protected void testIfThrows(
            String aInRegExpString,
            String aInExpectedMesage)
            throws UnrecognizedCharacterSequenceException,
                   JSELCompilationException, IOException {
        JSELExpression lExpression = JSELExpression.compile(
                "\n" + aInRegExpString + " ");
        assertNotNull(lExpression);

        try {
            runner.execute(lExpression);
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals(aInExpectedMesage, e.getMessage());
        }

    }

    protected void verifyNumber(JSELValue aInValue, double aInExpected) {
        aInValue = aInValue.getValue();
        assertEquals(Type.NUMBER, aInValue.getType());
        assertTrue(aInValue instanceof JSELNumber);
        JSELNumber lNumber = (JSELNumber) aInValue;
        assertEquals(aInExpected, lNumber.toNumber(), 0.0);
    }

    protected void verifyString(JSELValue aInValue, String aInExpected) {
        aInValue = aInValue.getValue();
        assertEquals(Type.STRING, aInValue.getType());
        assertTrue(aInValue instanceof JSELString);
        JSELString lString = (JSELString) aInValue;
        assertEquals(aInExpected, lString.toString());
    }

    protected void verifyBoolean(JSELValue aInValue, boolean aInExpected) {
        aInValue = aInValue.getValue();
        assertEquals(Type.BOOLEAN, aInValue.getType());
        assertTrue(aInValue instanceof JSELBoolean);
        JSELBoolean lBoolean = (JSELBoolean) aInValue;
        if (aInExpected) {
            assertTrue(lBoolean.toBoolean());
        }
        else {
            assertFalse(lBoolean.toBoolean());
        }
    }

    protected void verifyUndefined(JSELValue aInValue) {
        if (aInValue instanceof JSELPropertyReference) {
            aInValue = aInValue.getValue();
        }

        assertEquals(Type.UNDEFINED, aInValue.getType());
        assertTrue(aInValue instanceof JSELUndefined);
        assertSame(JSELUndefined.getInstance(), aInValue);
    }

    protected void verifyNull(JSELValue aInValue) {
        if (aInValue instanceof JSELPropertyReference) {
            aInValue = aInValue.getValue();
        }

        assertEquals(Type.NULL, aInValue.getType());
        assertTrue(aInValue instanceof JSELNull);
        assertSame(JSELNull.getInstance(), aInValue);
    }

    protected void verifyFunction(
            JSELValue aInValue, String ... aInParameters) {
        aInValue = aInValue.getValue();

        assertEquals(Type.OBJECT, aInValue.getType());
        assertTrue(aInValue instanceof JSELFunction);
        assertTrue(aInValue.isCallable());

        JSELFunction lFunction = (JSELFunction) aInValue;
        assertEquals(
                asList(aInParameters),
                lFunction.getParameters());
        assertEquals(JSELFunction.CLASS, lFunction.getObjectClass());
    }

    protected void verifyArray(JSELValue aInValue, Verifier... aInVerifiers) {
        if (aInValue instanceof JSELPropertyReference) {
            aInValue = aInValue.getValue();
        }

        assertEquals(Type.OBJECT, aInValue.getType());
        assertTrue(aInValue instanceof JSELArray);
        JSELArray lArray = (JSELArray) aInValue;

        JSELValue lLengthObject = lArray.get("length");
        assertNotNull(lLengthObject);
        assertTrue(lLengthObject instanceof JSELNumber);
        JSELNumber lLength = (JSELNumber) lLengthObject;
        assertEquals(aInVerifiers.length, lLength.toNumber(), 0.0);

        for (int i = 0; i < aInVerifiers.length; i++) {
            aInVerifiers[i].verify(lArray.get(i));
        }
    }

    protected void verifyObject(
            JSELValue aInValue, Verifier... aInVerifiers) {
        assertEquals(Type.OBJECT, aInValue.getType());

        for (int i = 0; i < aInVerifiers.length; i++) {
            aInVerifiers[i].verify(aInValue);
        }
    }

    protected void verifyRegExp(JSELValue aInValue,
            String aInExpectedToString) {
        aInValue = aInValue.getValue();
        assertEquals(Type.OBJECT, aInValue.getType());
        assertTrue(aInValue instanceof JSELRegExp);
        assertEquals(JSELRegExp.CLASS, aInValue.toObject().getObjectClass());
        JSELRegExp lRegExp = (JSELRegExp) aInValue;
        assertEquals(aInExpectedToString, lRegExp.toString());
    }

    public interface Verifier {
        void verify(JSELValue aInValue);
    }

    protected Verifier numberVerifier(double aInNumber) {
        return aInValue -> verifyNumber(aInValue, aInNumber);
    }

    protected Verifier stringVerifier(String aInString) {
        return aInValue -> verifyString(aInValue, aInString);
    }

    protected Verifier booleanVerifier(boolean aInBoolean) {
        return aInValue -> verifyBoolean(aInValue, aInBoolean);
    }

    protected Verifier arrayVerifier(Verifier... aInVerifiers) {
        return aInValue -> verifyArray(aInValue, aInVerifiers);
    }

    protected Verifier propertyVerifier(
            String aInProperty, Verifier aInVerifier) {
        return aInValue -> aInVerifier.verify(
                aInValue.toObject().get(aInProperty));
    }

    protected Verifier objectVerifier(Verifier... aInVerifiers) {
        return aInValue -> verifyObject(aInValue, aInVerifiers);
    }

    protected Verifier regexVerifier(String aInExpectedToString) {
        return aInValue -> verifyRegExp(aInValue, aInExpectedToString);
    }

}
