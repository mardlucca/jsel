/*
 * File: RegExpRecognizer.java
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

import mardlucca.parselib.parser.LRParser;
import mardlucca.parselib.tokenizer.BaseTokenRecognizer;
import mardlucca.parselib.tokenizer.IdentifierRecognizer;
import mardlucca.parselib.tokenizer.MatchResult;

public class RegExpRecognizer extends BaseTokenRecognizer<TokenEnum> {
    private State state = State.INITIAL;

    private int numberOpenBrackets = 0;

    private IdentifierRecognizer<?>  identifierRecognizer =
            new IdentifierRecognizer<>(null);

    public RegExpRecognizer() {
        super(TokenEnum.REGEX);
    }

    @Override
    public MatchResult test(int aInChar, Object aInSyntacticContext) {
        LRParser<TokenEnum, String>.State lCurrentState =
                (LRParser<TokenEnum, String>.State) aInSyntacticContext;

        if (lCurrentState == null || lCurrentState.hasAction(TokenEnum.DIVIDE)) {
            // if the current state allows for the / (division) operator, we
            // will use that instead, otherwise we assume they're trying to
            // create a regex literal.
            return MatchResult.NOT_A_MATCH;
        }

        switch (state) {
            case INITIAL:
                return handleInitialState(aInChar);
            case READING_ESCAPE_CHAR:
                return handleReadingEscapeCharState(aInChar);
            case READING_FIRST_CHAR:
                return handleReadingFirstCharState(aInChar);
            case READING_FLAGS:
                return handleReadingFlagsState(aInChar);
            case READING_CHARS:
                return handleReadingCharsState(aInChar);
            case SUCCESS:
            case FAILURE:
        }
        return failure(null);
    }

    private MatchResult handleInitialState(int aInChar) {
        if (aInChar == '/') {
            state = State.READING_FIRST_CHAR;
            return MatchResult.PARTIAL_MATCH;
        }

        state = State.FAILURE;
        return MatchResult.NOT_A_MATCH;
    }

    private MatchResult handleReadingEscapeCharState(int aInChar) {
        if (aInChar == '\n' || aInChar == -1) {
            return failure("Invalid regular expression: missing /");
        }

        state = State.READING_CHARS;
        return MatchResult.PARTIAL_MATCH;
    }

    private MatchResult handleReadingFirstCharState(int aInChar) {
        if (aInChar == '\\') {
            state = State.READING_ESCAPE_CHAR;
            return MatchResult.PARTIAL_MATCH;
        }

        if (aInChar == '[') {
            numberOpenBrackets++;
            state = State.READING_CHARS;
            return MatchResult.PARTIAL_MATCH;
        }

        if (aInChar == '\n' || aInChar == -1) {
            // this failure will typically not be reported back as javascript
            // also includes / as the division operator
            return failure("Invalid regular expression: missing /");
        }

        if (aInChar == '*' || aInChar == '/') {
            // token is either single line or multi line comment
            state = State.FAILURE;
            return MatchResult.NOT_A_MATCH;
        }

        state = State.READING_CHARS;
        return MatchResult.PARTIAL_MATCH;
    }

    private MatchResult handleReadingFlagsState(int aInChar) {
        return identifierRecognizer.test(aInChar);
    }

    private MatchResult handleReadingCharsState(int aInChar) {
        if (aInChar == ']') {
            if (numberOpenBrackets > 0) {
                numberOpenBrackets--;
            }
            return MatchResult.PARTIAL_MATCH;
        }

        if (aInChar == '\\') {
            state = State.READING_ESCAPE_CHAR;
            return MatchResult.PARTIAL_MATCH;
        }

        if (aInChar == '[') {
            numberOpenBrackets++;
            return MatchResult.PARTIAL_MATCH;
        }

        if (aInChar == '\n' || aInChar == -1) {
            return failure("Invalid regular expression: missing /");
        }

        if (aInChar == '/' && numberOpenBrackets == 0) {
            state = State.READING_FLAGS;
            return MatchResult.MATCH;
        }
        // else / is treated like a normal character, not as the end of the body
        // part

        return MatchResult.PARTIAL_MATCH;
    }

    @Override
    public void reset() {
        super.reset();
        state = State.INITIAL;
        numberOpenBrackets = 0;
        identifierRecognizer.reset();
    }

    @Override
    public String[] getValue(String aInCharSequence) {
        if (aInCharSequence.length() < 3) {
            return null;
        }

        int lLastSlash = aInCharSequence.lastIndexOf('/');
        return new String[] {
                aInCharSequence.substring(1, lLastSlash),
                lLastSlash + 1 < aInCharSequence.length()
                        ? aInCharSequence.substring(lLastSlash + 1)
                        : ""
        };
    }

    private MatchResult failure(String aInReason) {
        setFailureReason(aInReason);
        state = State.FAILURE;
        return MatchResult.NOT_A_MATCH;
    }

    private enum State {
        INITIAL,
        READING_FIRST_CHAR,
        READING_ESCAPE_CHAR,
        READING_CHARS,
        READING_FLAGS,
        SUCCESS,
        FAILURE
    }
}
