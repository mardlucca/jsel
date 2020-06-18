/*
 * File: JSELTokenizerFactoryTest.java
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

import mardlucca.jsel.type.JSELBoolean;
import mardlucca.jsel.type.JSELNull;
import mardlucca.jsel.type.JSELNumber;
import mardlucca.jsel.type.JSELString;
import mardlucca.parselib.tokenizer.Token;
import mardlucca.parselib.tokenizer.Tokenizer;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static mardlucca.jsel.JSELTokenizerFactory.newTokenizer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSELTokenizerFactoryTest {
    private static String TEST_CASE=
            "id\t=>\t(\t)\t?\t:\t||\t&&\t|\t^\t&\t==\t===\t!=\t!==\t<=\t<\t" +
                    ">\t>=\tin\tinstanceof\t<<\t>>\t>>>\t+\t-\t*\t/\t%\t!\t" +
                    "~\ttypeof\tvoid\t[\t]\t.\tnew\t1.2\t'str'\ttrue\tfalse\t" +
                    "undefined\tnull\tNaN\tInfinity\t{\t}\t,\tthis";


    @Test
    public void tokenizeTest() {
        Tokenizer<TokenEnum> lTokenizer = newTokenizer(
                new StringReader(TEST_CASE));
        List<Token<TokenEnum,?>> lTokens = new ArrayList<>();

        for (Token<TokenEnum, ?> lToken : lTokenizer) {
            lTokens.add(lToken);
        }

        assertSymbol(TokenEnum.IDENTIFIER, lTokens.get(0));
        assertSymbol(TokenEnum.ARROW, lTokens.get(1));
        assertSymbol(TokenEnum.OPEN_PARENTHESIS, lTokens.get(2));
        assertSymbol(TokenEnum.CLOSE_PARENTHESIS, lTokens.get(3));
        assertSymbol(TokenEnum.QUESTION_MARK, lTokens.get(4));
        assertSymbol(TokenEnum.COLON, lTokens.get(5));
        assertSymbol(TokenEnum.OR, lTokens.get(6));
        assertSymbol(TokenEnum.AND, lTokens.get(7));
        assertSymbol(TokenEnum.BITWISE_OR, lTokens.get(8));
        assertSymbol(TokenEnum.BITWISE_XOR, lTokens.get(9));
        assertSymbol(TokenEnum.BITWISE_AND, lTokens.get(10));

        assertSymbol(TokenEnum.EQUAL, lTokens.get(11));
        assertSymbol(TokenEnum.STRICT_EQUAL, lTokens.get(12));
        assertSymbol(TokenEnum.NOT_EQUAL, lTokens.get(13));
        assertSymbol(TokenEnum.STRICT_NOT_EQUAL, lTokens.get(14));
        assertSymbol(TokenEnum.LESS_THAN_OR_EQUAL, lTokens.get(15));
        assertSymbol(TokenEnum.LESS_THAN, lTokens.get(16));
        assertSymbol(TokenEnum.GREATER_THAN, lTokens.get(17));
        assertSymbol(TokenEnum.GREATER_THAN_OR_EQUAL, lTokens.get(18));
        assertSymbol(TokenEnum.IN, lTokens.get(19));
        assertSymbol(TokenEnum.INSTANCEOF, lTokens.get(20));

        assertSymbol(TokenEnum.LEFT_SHIFT, lTokens.get(21));
        assertSymbol(TokenEnum.RIGHT_SHIFT, lTokens.get(22));
        assertSymbol(TokenEnum.RIGHT_SHIFT_UNSIGNED, lTokens.get(23));
        assertSymbol(TokenEnum.PLUS, lTokens.get(24));
        assertSymbol(TokenEnum.MINUS, lTokens.get(25));
        assertSymbol(TokenEnum.MULTIPLY, lTokens.get(26));
        assertSymbol(TokenEnum.DIVIDE, lTokens.get(27));
        assertSymbol(TokenEnum.MODULUS, lTokens.get(28));
        assertSymbol(TokenEnum.NOT, lTokens.get(29));
        assertSymbol(TokenEnum.BITWISE_NOT, lTokens.get(30));

        assertSymbol(TokenEnum.TYPEOF, lTokens.get(31));
        assertSymbol(TokenEnum.VOID, lTokens.get(32));
        assertSymbol(TokenEnum.OPEN_SQUARE_BRACKETS, lTokens.get(33));
        assertSymbol(TokenEnum.CLOSE_SQUARE_BRACKETS, lTokens.get(34));
        assertSymbol(TokenEnum.DOT, lTokens.get(35));
        assertSymbol(TokenEnum.NEW, lTokens.get(36));
        assertNumber(lTokens.get(37), "1.2", 1.2);
        assertString(lTokens.get(38), "'str'", "str");
        assertBoolean(lTokens.get(39), "true", true);
        assertBoolean(lTokens.get(40), "false", false);

        assertToken(TokenEnum.IDENTIFIER, "undefined", lTokens.get(41));
        assertJSELNull(lTokens.get(42));
        assertToken(TokenEnum.IDENTIFIER, "NaN", lTokens.get(43));
        assertToken(TokenEnum.IDENTIFIER, "Infinity", lTokens.get(44));
        assertSymbol(TokenEnum.OPEN_CURLY_BRACKETS, lTokens.get(45));
        assertSymbol(TokenEnum.CLOSE_CURLY_BRACKETS, lTokens.get(46));
        assertSymbol(TokenEnum.COMMA, lTokens.get(47));
        assertEquals(TokenEnum.THIS, lTokens.get(48).getId());
        assertEquals(TokenEnum.EOF, lTokens.get(49).getId());
    }

    private void assertSymbol(TokenEnum aInSymbol, Token<TokenEnum, ?> aInToken) {
        assertEquals(aInSymbol, aInToken.getId());
        assertEquals(aInSymbol.toString(), aInToken.getCharSequence());
        assertEquals(aInSymbol.toString(), aInToken.getValue());
    }

    private void assertToken(
            TokenEnum aInEnum, String aInValue, Token<TokenEnum, ?> aInToken) {
        assertEquals(aInEnum, aInToken.getId());
        assertEquals(aInValue.toString(), aInToken.getValue());
    }

    private void assertString(Token<TokenEnum, ?> aInToken, String aInCharSequence,
            String aInValue) {
        assertEquals(TokenEnum.STRING, aInToken.getId());
        assertEquals(aInCharSequence, aInToken.getCharSequence());
        assertTrue(aInToken.getValue() instanceof JSELString);
        assertEquals(new JSELString(aInValue), aInToken.getValue());
    }

    private void assertNumber(Token<TokenEnum, ?> aInToken,
                              String aInCharSequence,
                              double aInNumber) {
        assertEquals(TokenEnum.NUMBER, aInToken.getId());
        assertEquals(aInCharSequence, aInToken.getCharSequence());
        assertTrue(aInToken.getValue() instanceof JSELNumber);
        assertEquals(new JSELNumber(aInNumber), aInToken.getValue());
    }

    private void assertBoolean(Token<TokenEnum, ?> aInToken,
                              String aInCharSequence,
                              boolean aInBoolean) {
        assertEquals(TokenEnum.BOOLEAN, aInToken.getId());
        assertEquals(aInCharSequence, aInToken.getCharSequence());
        assertTrue(aInToken.getValue() instanceof JSELBoolean);
        assertEquals(new JSELBoolean(aInBoolean), aInToken.getValue());
    }

    private void assertJSELNull(Token<TokenEnum, ?> aInToken) {
        assertEquals(TokenEnum.NULL, aInToken.getId());
        assertEquals("null", aInToken.getCharSequence());
        assertTrue(aInToken.getValue() instanceof JSELNull);
        assertEquals(JSELNull.getInstance(), aInToken.getValue());
    }
}