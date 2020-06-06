/*
 * File: TokenEnum.java
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

import java.util.Objects;

public enum TokenEnum {
    IDENTIFIER("id"),

    FALSE("false"),
    IN("in"),
    INFINITY("Infinity", "inf"),
    INSTANCEOF("instanceof", "instof"),
    NAN("NaN", "nan"),
    NEW("new"),
    NULL("null"),
    THIS("this"),
    TRUE("true"),
    TYPEOF("typeof"),
    UNDEFINED("undefined", "undef"),
    VOID("void"),

    ARROW("=>"),
    OPEN_PARENTHESIS("("),
    CLOSE_PARENTHESIS(")"),
    QUESTION_MARK("?"),
    COLON(":"),
    AND("&&"),
    OR("||"),
    BITWISE_AND("&"),
    BITWISE_OR("|"),
    BITWISE_XOR("^"),
    EQUAL("=="),
    STRICT_EQUAL("==="),
    NOT_EQUAL("!="),
    STRICT_NOT_EQUAL("!=="),
    LESS_THAN_OR_EQUAL("<="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    LEFT_SHIFT("<<"),
    RIGHT_SHIFT(">>"),
    RIGHT_SHIFT_UNSIGNED(">>>"),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULUS("%"),
    NOT("!", "not"),
    BITWISE_NOT("~"),
    OPEN_SQUARE_BRACKETS("["),
    CLOSE_SQUARE_BRACKETS("]"),
    OPEN_CURLY_BRACKETS("{"),
    CLOSE_CURLY_BRACKETS("}"),
    DOT("."),
    COMMA(","),

    NUMBER("num"),
    STRING("str"),
    REGEX("regex"),
    EOF("$");

    TokenEnum(String aInText) {
        this(aInText, null);
    }

    TokenEnum(String aInText, String aInAlias) {
        text = aInText;
        alias = aInAlias;
    }

    private String text;

    private String alias;

    public static TokenEnum fromText(String aInTextValue) {
        for (TokenEnum lToken : TokenEnum.values()) {
            if (lToken.text.equals(aInTextValue)
                    || (Objects.equals(lToken.alias, aInTextValue))) {
                return lToken;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return text;
    }
}
