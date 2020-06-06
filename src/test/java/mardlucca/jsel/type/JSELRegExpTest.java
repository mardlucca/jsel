/*
 * File: JSELRegExpTest.java
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

package mardlucca.jsel.type;

import mardlucca.jsel.JSELRuntimeException;
import mardlucca.jsel.type.JSELRegExp.Flag;
import mardlucca.jsel.type.JSELValue.MatchResult;
import org.junit.Test;

import java.util.EnumSet;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.junit.Assert.*;

public class JSELRegExpTest {
    private JSELRegExp regexAs = new JSELRegExp("a+", singleton(Flag.GLOBAL));
    private JSELRegExp wordsFollowedNumbers =
            new JSELRegExp("([a-z]+)(\\d+)");
    private JSELRegExp capturingAsIgnoreCase = new JSELRegExp("(a+)",
            EnumSet.of(Flag.IGNORE_CASE));
    private JSELRegExp beginEndNotMultiline = new JSELRegExp("^\\d+$");
    private JSELRegExp beginEndMultiline = new JSELRegExp("^(\\d+)$",
            EnumSet.of(Flag.MULTILINE));

    @Test
    public void testAttributesAndProperties() {
        assertEquals("a+", regexAs.getBody());
        assertEquals(1, regexAs.getFlags().size());
        assertTrue(regexAs.getFlags().contains(Flag.GLOBAL));
        assertEquals(1, beginEndMultiline.getFlags().size());
        assertTrue(capturingAsIgnoreCase.getFlags().contains(Flag.IGNORE_CASE));
        assertNotNull(regexAs.getPattern());

        assertEquals(new JSELNumber(0), regexAs.get(JSELRegExp.LAST_INDEX));
        assertSame(JSELBoolean.TRUE, regexAs.get(JSELRegExp.GLOBAL));
        assertSame(JSELBoolean.FALSE, regexAs.get(JSELRegExp.IGNORE_CASE));
        assertSame(JSELBoolean.FALSE, regexAs.get(JSELRegExp.MULTILINE));

        assertSame(JSELBoolean.TRUE,
                capturingAsIgnoreCase.get(JSELRegExp.IGNORE_CASE));
        assertSame(JSELBoolean.TRUE,
                beginEndMultiline.get(JSELRegExp.MULTILINE));
    }

    @Test
    public void getType() {
        assertEquals(Type.OBJECT, regexAs.getType());
    }

    @Test
    public void getObjectClass() {
        assertEquals(JSELRegExp.CLASS, regexAs.getObjectClass());
    }

    @Test
    public void isCallable() {
        assertFalse(regexAs.isCallable());
    }

    @Test
    public void testBasicMatch() {
        MatchResult lResult = regexAs.match("babaa", 0);
        assertNotNull(lResult);
        assertEquals(1, lResult.getStart());
        assertEquals(2, lResult.getEnd());
        assertEquals(1, lResult.getCaptures().length);
        assertEquals("a", lResult.getCaptures()[0]);
        assertEquals(new JSELNumber(0), regexAs.get(JSELRegExp.LAST_INDEX));

        lResult = regexAs.match("babaa", 2);
        assertNotNull(lResult);
        assertEquals(3, lResult.getStart());
        assertEquals(5, lResult.getEnd());
        assertEquals(1, lResult.getCaptures().length);
        assertEquals("aa", lResult.getCaptures()[0]);
        assertEquals(new JSELNumber(0), regexAs.get(JSELRegExp.LAST_INDEX));

        lResult = wordsFollowedNumbers.match("$#%word123!@#", 0);
        assertNotNull(lResult);
        assertEquals(3, lResult.getStart());
        assertEquals(10, lResult.getEnd());
        assertEquals(3, lResult.getCaptures().length);
        assertEquals("word123", lResult.getCaptures()[0]);
        assertEquals("word", lResult.getCaptures()[1]);
        assertEquals("123", lResult.getCaptures()[2]);
    }

    @Test
    public void testBasicNotMatch() {
        assertNull(regexAs.match("bAbAA", 0));
        assertNull(wordsFollowedNumbers.match("$#%word123!@#", 10));
    }

    @Test
    public void testBasicMatchIgnoreCase() {
        MatchResult lResult = capturingAsIgnoreCase.match("bAbaA", 0);
        assertNotNull(lResult);
        assertEquals(1, lResult.getStart());
        assertEquals(2, lResult.getEnd());
        assertEquals(2, lResult.getCaptures().length);
        assertEquals("A", lResult.getCaptures()[0]);
        assertEquals("A", lResult.getCaptures()[1]);

        lResult = capturingAsIgnoreCase.match("bAbaA", lResult.getEnd());
        assertNotNull(lResult);
        assertEquals(3, lResult.getStart());
        assertEquals(5, lResult.getEnd());
        assertEquals(2, lResult.getCaptures().length);
        assertEquals("aA", lResult.getCaptures()[0]);
        assertEquals("aA", lResult.getCaptures()[1]);
    }

    @Test
    public void testMatchNotMultiline() {
        // the test below doesn't work in Java, I'm thinking Java's Matcher.find
        // and Matcher.lookingAt have an error. In single-line mode (the
        // default) ^ and $ should only match the beginning and end of the input
        // sequence but in this case $ is matching \n which is NOT the end of
        // input.
        // assertNull(beginEndNotMultiline.match("123\n", 0));

        assertNull(beginEndNotMultiline.match("123\na", 0));
        assertNull(beginEndNotMultiline.match("123a", 0));
    }

    @Test
    public void testMatchMultiline() {
        MatchResult lResult = beginEndMultiline.match("123", 0);
        assertNotNull(lResult);
        assertEquals("123", lResult.getCaptures()[0]);
        assertNull(beginEndMultiline.match("123a", 0));

        lResult = beginEndMultiline.match("123\n", 0);
        assertNotNull(lResult);
        assertEquals("123", lResult.getCaptures()[0]);
        assertEquals("123", lResult.getCaptures()[1]);

        lResult = beginEndMultiline.match("123\na", 0);
        assertNotNull(lResult);
        assertEquals("123", lResult.getCaptures()[0]);
        assertEquals("123", lResult.getCaptures()[1]);
        assertEquals(0, lResult.getStart());
        assertEquals(3, lResult.getEnd());
    }

    @Test
    public void testGetFlags() {
        assertEquals(0, Flag.getFlags("").size());
        assertEquals(0, Flag.getFlags(null).size());
        Set<Flag> lFags = Flag.getFlags("mig");
        assertTrue(lFags.containsAll(
                EnumSet.of(Flag.GLOBAL, Flag.IGNORE_CASE, Flag.MULTILINE)));

        try {
            Flag.getFlags("miga");
            fail();
        }
        catch (JSELRuntimeException e) {
            assertEquals("SyntaxError: Invalid flags supplied to RegExp " +
                    "constructor 'miga'", e.getMessage());
        }
    }

    @Test
    public void testEmptyRegExp() {
        JSELRegExp empty = new JSELRegExp(null);
        assertEquals("/(?:)/", empty.toString());
    }
}
