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
import mardlucca.parselib.tokenizer.Recognizers;
import mardlucca.parselib.tokenizer.Tokenizer;

import java.io.Reader;

import static mardlucca.parselib.tokenizer.Recognizers.*;

public class JSELTokenizerFactory {
    public static final String NAN_CHAR_SEQUENCE = "NaN";

    public static final String INFINITY_CHAR_SEQUENCE = "Infinity";

    private Builder<TokenEnum> tokenizerBuilder;

    private static final JSELTokenizerFactory instance =
            new JSELTokenizerFactory();

    private JSELTokenizerFactory() {
        tokenizerBuilder = new Builder<TokenEnum>()
                .recognize(symbol(TokenEnum.FALSE))
                .recognize(symbol(TokenEnum.IN))
                .recognize(symbol(INFINITY_CHAR_SEQUENCE, TokenEnum.INFINITY))
                .recognize(symbol(TokenEnum.INSTANCEOF))
                .recognize(symbol(NAN_CHAR_SEQUENCE, TokenEnum.NAN))
                .recognize(symbol(TokenEnum.NEW))
                .recognize(symbol(TokenEnum.NULL))
                .recognize(symbol(TokenEnum.THIS))
                .recognize(symbol(TokenEnum.TRUE))
                .recognize(symbol(TokenEnum.TYPEOF))
                .recognize(symbol(TokenEnum.UNDEFINED))
                .recognize(symbol(TokenEnum.VOID))

                .recognize(symbol(TokenEnum.ARROW))
                .recognize(symbol(TokenEnum.OPEN_PARENTHESIS))
                .recognize(symbol(TokenEnum.CLOSE_PARENTHESIS))
                .recognize(symbol(TokenEnum.QUESTION_MARK))
                .recognize(symbol(TokenEnum.COLON))
                .recognize(symbol(TokenEnum.AND))
                .recognize(symbol(TokenEnum.OR))
                .recognize(symbol(TokenEnum.BITWISE_AND))
                .recognize(symbol(TokenEnum.BITWISE_OR))
                .recognize(symbol(TokenEnum.BITWISE_XOR))
                .recognize(symbol(TokenEnum.EQUAL))
                .recognize(symbol(TokenEnum.STRICT_EQUAL))
                .recognize(symbol(TokenEnum.NOT_EQUAL))
                .recognize(symbol(TokenEnum.STRICT_NOT_EQUAL))
                .recognize(symbol(TokenEnum.LESS_THAN_OR_EQUAL))
                .recognize(symbol(TokenEnum.LESS_THAN))
                .recognize(symbol(TokenEnum.GREATER_THAN))
                .recognize(symbol(TokenEnum.GREATER_THAN_OR_EQUAL))
                .recognize(symbol(TokenEnum.LEFT_SHIFT))
                .recognize(symbol(TokenEnum.RIGHT_SHIFT))
                .recognize(symbol(TokenEnum.RIGHT_SHIFT_UNSIGNED))
                .recognize(symbol(TokenEnum.PLUS))
                .recognize(symbol(TokenEnum.MINUS))
                .recognize(symbol(TokenEnum.MULTIPLY))
                .recognize(symbol(TokenEnum.DIVIDE))
                .recognize(symbol(TokenEnum.MODULUS))
                .recognize(symbol(TokenEnum.NOT))
                .recognize(symbol(TokenEnum.BITWISE_NOT))
                .recognize(symbol(TokenEnum.OPEN_SQUARE_BRACKETS))
                .recognize(symbol(TokenEnum.CLOSE_SQUARE_BRACKETS))
                .recognize(symbol(TokenEnum.OPEN_CURLY_BRACKETS))
                .recognize(symbol(TokenEnum.CLOSE_CURLY_BRACKETS))
                .recognize(symbol(TokenEnum.DOT))
                .recognize(symbol(TokenEnum.COMMA))

                .recognize(numbers(TokenEnum.NUMBER))
                .recognize(strings(TokenEnum.STRING))
                .recognize(strings(TokenEnum.STRING, '\''))
                .recognize(RegExpRecognizer::new)

                .recognize(identifiers(TokenEnum.IDENTIFIER))
                .recognize(singleLineComments())
                .recognize(multiLineComments())
                .endOfFile(TokenEnum.EOF);
    }

    public static Tokenizer<TokenEnum> newTokenizer(Reader aInReader) {
        return instance.tokenizerBuilder.build(aInReader);
    }
}
