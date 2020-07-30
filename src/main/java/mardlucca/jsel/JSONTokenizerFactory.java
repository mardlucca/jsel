/*
 * File: JSONTokenizerFactory.java
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
import mardlucca.parselib.tokenizer.BasicTokenizer.Builder;
import mardlucca.parselib.tokenizer.Tokenizer;

import java.io.Reader;

import static mardlucca.parselib.tokenizer.Recognizers.*;

public class JSONTokenizerFactory {
    private Builder<TokenEnum> tokenizerBuilder;

    private static final JSONTokenizerFactory instance =
            new JSONTokenizerFactory();

    private JSONTokenizerFactory() {

        tokenizerBuilder = new Builder<TokenEnum>()
                .recognize(whiteSpaces())

                // symbols
                .recognize(symbol(TokenEnum.COLON))
                .recognize(symbol(TokenEnum.MINUS))
                .recognize(symbol(TokenEnum.OPEN_SQUARE_BRACKETS))
                .recognize(symbol(TokenEnum.CLOSE_SQUARE_BRACKETS))
                .recognize(symbol(TokenEnum.OPEN_CURLY_BRACKETS))
                .recognize(symbol(TokenEnum.CLOSE_CURLY_BRACKETS))
                .recognize(symbol(TokenEnum.COMMA))

                // literals
                .recognize(transforming(
                        booleans(TokenEnum.BOOLEAN),
                        JSELBoolean::new))
                .recognize(transforming(
                        symbol("null", TokenEnum.NULL),
                        aInValue -> JSELNull.getInstance()))
                .recognize(transforming(
                        numbers(TokenEnum.NUMBER),
                        aInNumber -> new JSELNumber(aInNumber.doubleValue())))
                .recognize(transforming(
                        strings(TokenEnum.STRING),
                        JSELString::new))
                .recognize(transforming(
                        strings(TokenEnum.STRING, '\''),
                        JSELString::new))
                .recognize(RegExpRecognizer::new)

                .recognize(singleLineComments())
                .recognize(multiLineComments())
                .endOfFile(TokenEnum.EOF);
    }

    public static Tokenizer<TokenEnum> newTokenizer(Reader aInReader) {
        return instance.tokenizerBuilder.build(aInReader);
    }
}
