/*
 * File: RegExpRecognizerTest.java
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

import mardlucca.parselib.parser.LRParsingTable;
import mardlucca.parselib.tokenizer.MatchResult;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static mardlucca.parselib.tokenizer.MatchResult.MATCH;
import static mardlucca.parselib.tokenizer.MatchResult.NOT_A_MATCH;
import static mardlucca.parselib.tokenizer.MatchResult.PARTIAL_MATCH;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegExpRecognizerTest {
    private static RegExpRecognizer recognizer = new RegExpRecognizer();
    private LRParsingTable<TokenEnum>.State divState =
            mock(LRParsingTable.State.class);
    private LRParsingTable<TokenEnum>.State regexState =
            mock(LRParsingTable.State.class);

    @Before
    public void setUp() {
        when(divState.hasAction(TokenEnum.DIVIDE)).thenReturn(true);
    }

    @Test
    public void testSuccessCases() {
        test("/ab/ab1+", regexState, PARTIAL_MATCH, PARTIAL_MATCH,
                PARTIAL_MATCH, MATCH, MATCH, MATCH, MATCH, NOT_A_MATCH);
        test("/ab/ab1+", regexState, PARTIAL_MATCH, PARTIAL_MATCH,
                PARTIAL_MATCH, MATCH, MATCH, MATCH, MATCH, NOT_A_MATCH);
        test("/[]/", regexState, PARTIAL_MATCH, PARTIAL_MATCH,
                PARTIAL_MATCH, MATCH);
        test("/]\\%/", regexState, PARTIAL_MATCH, PARTIAL_MATCH, PARTIAL_MATCH,
                PARTIAL_MATCH, MATCH);
        test("/[]/", regexState, PARTIAL_MATCH, PARTIAL_MATCH, PARTIAL_MATCH,
                MATCH);
        test("/[[]/]/", regexState, PARTIAL_MATCH, PARTIAL_MATCH, PARTIAL_MATCH,
                PARTIAL_MATCH, PARTIAL_MATCH, PARTIAL_MATCH, MATCH);
        test("/**/", regexState, PARTIAL_MATCH, NOT_A_MATCH, NOT_A_MATCH,
                NOT_A_MATCH);
        test("//a/", regexState, PARTIAL_MATCH, NOT_A_MATCH, NOT_A_MATCH,
                NOT_A_MATCH);
        test("a/a/", regexState, NOT_A_MATCH, NOT_A_MATCH, NOT_A_MATCH,
                NOT_A_MATCH);
        test("/a[/gim]/mig", regexState, PARTIAL_MATCH, PARTIAL_MATCH,
                PARTIAL_MATCH, PARTIAL_MATCH, PARTIAL_MATCH, PARTIAL_MATCH,
                PARTIAL_MATCH, PARTIAL_MATCH, MATCH, MATCH, MATCH, MATCH);
    }

    @Test
    public void testDivMode() {
        test("//a/", divState, NOT_A_MATCH, NOT_A_MATCH, NOT_A_MATCH,
                NOT_A_MATCH);
    }

    @Test
    public void testErrorMessages() {
        testError("/");
        testError("/a");
        testError("/[");
        testError("/[[[]]/");
        testError("/\\\n");
        testError("/\\");
    }

    @Test
    public void testGetValue() {
        assertNull("", recognizer.getValue("//"));
        assertArrayEquals(new String[] {"a", ""}, recognizer.getValue("/a/"));
        assertArrayEquals(new String[] {"aaa", ""},
                recognizer.getValue("/aaa/"));
        assertArrayEquals(new String[] {"aaa", "b"},
                recognizer.getValue("/aaa/b"));
        assertArrayEquals(new String[] {"a[/gim]", "mig"},
                recognizer.getValue("/a[/gim]/mig"));

    }

    private void test(String aInString, Object aInState,
            MatchResult ... aInResults) {
        recognizer.reset();
        for (int i = 0; i < aInString.length(); i++) {
            assertEquals(aInResults[i],
                    recognizer.test(aInString.charAt(i), aInState));
        }
    }

    private void testError(String aInString) {
        recognizer.reset();
        Reader in = new StringReader(aInString);

        int lChar;
        do {
            try {
                lChar = in.read();
                if (recognizer.test(lChar, regexState)
                        == MatchResult.NOT_A_MATCH) {
                    break;
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        while (lChar >= 0);
        assertEquals("Invalid regular expression: missing /",
                recognizer.getFailureReason());
    }

}