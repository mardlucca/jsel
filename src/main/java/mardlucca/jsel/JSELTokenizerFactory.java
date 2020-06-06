/*
 * File: JSELTokenizerFactory.java
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

import mardlucca.parselib.tokenizer.BasicTokenizer.Builder;
import mardlucca.parselib.tokenizer.Tokenizer;

import java.io.Reader;

public class JSELTokenizerFactory {
    public static final String NAN_CHAR_SEQUENCE = "NaN";

    public static final String INFINITY_CHAR_SEQUENCE = "Infinity";

    private Builder<TokenEnum> tokenizerBuilder;

    private static final JSELTokenizerFactory instance =
            new JSELTokenizerFactory();

    private JSELTokenizerFactory() {
        tokenizerBuilder = new Builder<TokenEnum>()
                .identifiers(TokenEnum.IDENTIFIER)

                .keyword(TokenEnum.FALSE)
                .keyword(TokenEnum.IN)
                .keyword(INFINITY_CHAR_SEQUENCE, TokenEnum.INFINITY)
                .keyword(TokenEnum.INSTANCEOF)
                .keyword(NAN_CHAR_SEQUENCE, TokenEnum.NAN)
                .keyword(TokenEnum.NEW)
                .keyword(TokenEnum.NULL)
                .keyword(TokenEnum.THIS)
                .keyword(TokenEnum.TRUE)
                .keyword(TokenEnum.TYPEOF)
                .keyword(TokenEnum.UNDEFINED)
                .keyword(TokenEnum.VOID)

                .symbol(TokenEnum.ARROW)
                .symbol(TokenEnum.OPEN_PARENTHESIS)
                .symbol(TokenEnum.CLOSE_PARENTHESIS)
                .symbol(TokenEnum.QUESTION_MARK)
                .symbol(TokenEnum.COLON)
                .symbol(TokenEnum.AND)
                .symbol(TokenEnum.OR)
                .symbol(TokenEnum.BITWISE_AND)
                .symbol(TokenEnum.BITWISE_OR)
                .symbol(TokenEnum.BITWISE_XOR)
                .symbol(TokenEnum.EQUAL)
                .symbol(TokenEnum.STRICT_EQUAL)
                .symbol(TokenEnum.NOT_EQUAL)
                .symbol(TokenEnum.STRICT_NOT_EQUAL)
                .symbol(TokenEnum.LESS_THAN_OR_EQUAL)
                .symbol(TokenEnum.LESS_THAN)
                .symbol(TokenEnum.GREATER_THAN)
                .symbol(TokenEnum.GREATER_THAN_OR_EQUAL)
                .symbol(TokenEnum.LEFT_SHIFT)
                .symbol(TokenEnum.RIGHT_SHIFT)
                .symbol(TokenEnum.RIGHT_SHIFT_UNSIGNED)
                .symbol(TokenEnum.PLUS)
                .symbol(TokenEnum.MINUS)
                .symbol(TokenEnum.MULTIPLY)
                .symbol(TokenEnum.DIVIDE)
                .symbol(TokenEnum.MODULUS)
                .symbol(TokenEnum.NOT)
                .symbol(TokenEnum.BITWISE_NOT)
                .symbol(TokenEnum.OPEN_SQUARE_BRACKETS)
                .symbol(TokenEnum.CLOSE_SQUARE_BRACKETS)
                .symbol(TokenEnum.OPEN_CURLY_BRACKETS)
                .symbol(TokenEnum.CLOSE_CURLY_BRACKETS)
                .symbol(TokenEnum.DOT)
                .symbol(TokenEnum.COMMA)

                .numberLiterals(TokenEnum.NUMBER)
                .stringLiterals(TokenEnum.STRING)
                .stringLiterals(TokenEnum.STRING, '\'')
                .recognizer(RegExpRecognizer::new)

                .singleLineComments(null)
                .multiLineComments(null)
                .endOfFile(TokenEnum.EOF);
    }

    public static Tokenizer<TokenEnum> newTokenizer(Reader aInReader) {
        return instance.tokenizerBuilder.build(aInReader);
    }
}
