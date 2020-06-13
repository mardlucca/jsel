/*
 * File: JSELParserBuilder.java
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
import mardlucca.parselib.parser.LRParsingTableBuilder;

import static java.lang.Character.isUpperCase;

public class JSELParserBuilder extends LRParsingTableBuilder<TokenEnum> {
    JSELParserBuilder() {
        super("jsel",
                TokenEnum::fromText,
                aInString -> isUpperCase(aInString.charAt(0)) ?
                        aInString : null);
    }

    @Override
    public LRParsingTable<TokenEnum> build() {
        LRParsingTable<TokenEnum> lParsingTable = super.build();
        lParsingTable.getState(41).reduceIf(
                TokenEnum.CLOSE_PARENTHESIS,
                TokenEnum.ARROW,
                82);
        return lParsingTable;
    }
}
