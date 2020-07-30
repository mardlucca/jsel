/*
 * File: JSONCompiler.java
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

import mardlucca.jsel.expr.*;
import mardlucca.jsel.type.JSELNull;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.JSELValue;
import mardlucca.parselib.parser.*;
import mardlucca.parselib.tokenizer.Token;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.util.Collections.singletonList;

public class JSONCompiler {
    private static final JSONCompiler instance = new JSONCompiler();

    private volatile Parser parser;
    
    public static JSONCompiler getInstance() {
        return instance;
    }

    public JSELExpression compile(Reader aInReader)
            throws IOException, UnrecognizedCharacterSequenceException,
                   JSELCompilationException {
        ParseResult lResult = getParser().parse(aInReader);
        if (!lResult.getErrors().isEmpty()) {
            throw new JSELCompilationException(lResult.getErrors());
        }
        return (JSELExpression) lResult.getValue();
    }

    private Parser getParser() {
        // Because all state on a JSEL parser is stored in the ParseInvocation
        // it means the parser is thread safe so it can be cached
        if (parser == null) {
            synchronized (this) {
                if (parser == null) {
                    Grammar lGrammar = GrammarLoader.load(
                            new InputStreamReader(
                                    JSONCompiler.class.getResourceAsStream(
                                            "/META-INF/json.grammar")),
                            TokenEnum::fromText);
                    initListeners(lGrammar);

                    LRParsingTable<TokenEnum> lParsingTable =
                            loadTable(lGrammar);

                    parser = lParsingTable.buildParser(
                            JSONTokenizerFactory::newTokenizer);
                }
            }
        }
        return parser;
    }

    private LRParsingTable<TokenEnum> loadTable(Grammar aInGrammar) {
        LRParsingTable<TokenEnum> lParsingTable = LRParsingTableLoader.build(
                aInGrammar,
                ResourceBundle.getBundle("META-INF/json-errors"),
                new InputStreamReader(
                        JSONCompiler.class.getResourceAsStream(
                                "/META-INF/json.table")),
                TokenEnum::fromText);

        return lParsingTable;
    }


    private synchronized void initListeners(Grammar aInGrammar) {

        aInGrammar
                .onDefaultReduce((aInProduction, aInValues) -> aInValues[0])

                        // VAL' -> VAL
                        // AC -> VAL
                .onReduce("VAL -> num", (aInProduction, aInValues) ->
                        new LiteralExpression(
                                ((Token<?,JSELValue>)aInValues[0]).getValue()))
                .onReduce("VAL -> - num", (aInProduction, aInValues) ->
                        new NegationExpression(new LiteralExpression(
                                ((Token<?,JSELValue>)aInValues[0]).getValue())))
                .onReduce("VAL -> str", (aInProduction, aInValues) ->
                        new LiteralExpression(
                                ((Token<?,JSELValue>)aInValues[0]).getValue()))
                .onReduce("VAL -> boolean", (aInProduction, aInValues) ->
                        new LiteralExpression(
                                ((Token<?,JSELValue>)aInValues[0]).getValue()))
                .onReduce("VAL -> null", (aInProduction, aInValues) ->
                        new LiteralExpression(JSELNull.getInstance()))
                .onReduce("VAL -> [ ARGS ]", (aInProduction, aInValues) ->
                        new ArrayExpression(
                                (List<JSELExpression>) aInValues[1]))
                .onReduce("VAL -> { PROPS }", (aInProduction, aInValues) ->
                        aInValues[1])

                        // ARGS -> ARGS2 default
                .onReduce("ARGS -> ''", (aInProduction, aInValues) ->
                        new ArrayList<>())
                .onReduce("ARGS2 -> ARGS2 , VAL", (aInProduction, aInValues) -> {
                    ((ArrayList)aInValues[0]).add(aInValues[2]);
                    return aInValues[0];
                })
                .onReduce("ARGS2 -> VAL", (aInProduction, aInValues) ->
                        new ArrayList<>(singletonList(aInValues[0])))

                        // PROPS -> PROPS2
                .onReduce("PROPS -> ''", (aInProduction, aInValues) ->
                        new ObjectExpression())
                .onReduce("PROPS2 -> PROPS2 , PROP",
                        (aInProduction, aInValues) ->
                                ((ObjectExpression) aInValues[0]).add(
                                        (Pair<String, JSELExpression>) aInValues[2]))
                .onReduce("PROPS2 -> PROP", (aInProduction, aInValues) ->
                        new ObjectExpression().add(
                                (Pair<String, JSELExpression>) aInValues[0]))
                .onReduce("PROP -> str : VAL", (aInProduction, aInValues) ->
                        Pair.of(((Token<?,JSELString>)aInValues[0]).getValue()
                                .toString(),
                                (JSELExpression) aInValues[2]))
        ;
    }
}
