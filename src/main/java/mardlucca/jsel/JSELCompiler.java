/*
 * File: JSELCompiler.java
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

import mardlucca.jsel.expr.AccessExpression;
import mardlucca.jsel.expr.AndOperatorExpression;
import mardlucca.jsel.expr.ArrayExpression;
import mardlucca.jsel.expr.BitwiseAndOperatorExpression;
import mardlucca.jsel.expr.BitwiseNotExpression;
import mardlucca.jsel.expr.BitwiseOrOperatorExpression;
import mardlucca.jsel.expr.ConditionalOperatorExpression;
import mardlucca.jsel.expr.DivisionOperatorExpression;
import mardlucca.jsel.expr.EqualsExpression;
import mardlucca.jsel.expr.FunctionCallExpression;
import mardlucca.jsel.expr.GreaterThanExpreassion;
import mardlucca.jsel.expr.GreaterThanOrEqualToExpreassion;
import mardlucca.jsel.expr.IdentifierExpression;
import mardlucca.jsel.expr.InOperatorExpression;
import mardlucca.jsel.expr.JSELExpression;
import mardlucca.jsel.expr.LambdaExpression;
import mardlucca.jsel.expr.LessThanExpreassion;
import mardlucca.jsel.expr.LessThanOrEqualToExpreassion;
import mardlucca.jsel.expr.LiteralExpression;
import mardlucca.jsel.expr.LiteralSupplierExpression;
import mardlucca.jsel.expr.ModulusOperatorExpression;
import mardlucca.jsel.expr.MultiplicationOperatorExpression;
import mardlucca.jsel.expr.NegationExpression;
import mardlucca.jsel.expr.NewExpression;
import mardlucca.jsel.expr.NotEqualsExpression;
import mardlucca.jsel.expr.NotExpression;
import mardlucca.jsel.expr.ObjectExpression;
import mardlucca.jsel.expr.OrOperatorExpression;
import mardlucca.jsel.expr.PlusOperatorExpression;
import mardlucca.jsel.expr.ShiftLeftExpression;
import mardlucca.jsel.expr.SignedShiftRightExpression;
import mardlucca.jsel.expr.StrictEqualsExpression;
import mardlucca.jsel.expr.StrictNotEqualsExpression;
import mardlucca.jsel.expr.SubtractionOperatorExpression;
import mardlucca.jsel.expr.BitwiseXorOperatorExpression;
import mardlucca.jsel.expr.TypeOfExpression;
import mardlucca.jsel.expr.UnaryPlusExpression;
import mardlucca.jsel.expr.UnsignedShiftRightExpression;
import mardlucca.jsel.expr.VoidExpression;
import mardlucca.jsel.type.*;
import mardlucca.parselib.parser.*;
import mardlucca.parselib.parser.Grammar.Production;
import mardlucca.parselib.parser.Grammar.ReduceListener;
import mardlucca.parselib.tokenizer.Token;
import mardlucca.parselib.tokenizer.UnrecognizedCharacterSequenceException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class JSELCompiler {
    private static final JSELCompiler instance = new JSELCompiler();

    private volatile Parser parser;
    
    public static JSELCompiler getInstance() {
        return instance;
    }

    public JSELExpression compile(String aInString)
            throws IOException, UnrecognizedCharacterSequenceException,
            JSELCompilationException {
        return compile(new StringReader(aInString));
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
                                    JSELCompiler.class.getResourceAsStream(
                                            "/META-INF/jsel.grammar")),
                            TokenEnum::fromText);
                    initListeners(lGrammar);

                    LRParsingTable<TokenEnum> lParsingTable =
                            loadTable(lGrammar);

                    parser = lParsingTable.buildParser(
                            JSELTokenizerFactory::newTokenizer);
                }
            }
        }
        return parser;
    }

    private LRParsingTable<TokenEnum> loadTable(Grammar aInGrammar) {
        LRParsingTable<TokenEnum> lParsingTable = LRParsingTableLoader.build(
                aInGrammar,
                ResourceBundle.getBundle("META-INF/jsel-errors"),
                new InputStreamReader(
                        JSELCompiler.class.getResourceAsStream(
                                "/META-INF/jsel.table")),
                TokenEnum::fromText);

        lParsingTable.getState(37).reduceIf(
                TokenEnum.CLOSE_PARENTHESIS,
                TokenEnum.ARROW,
                79);
        return lParsingTable;
    }


    private synchronized void initListeners(Grammar aInGrammar) {

        aInGrammar
                .onDefaultReduce((aInProduction, aInValues) -> aInValues[0])

                        // E' -> E
                .onReduce("E -> id => E", (aInProduction, aInValues) ->
                        new LambdaExpression(
                                ((Token<?,String>)aInValues[0]).getValue(),
                                (JSELExpression) aInValues[2]))
                .onReduce("E -> ( PARAMS ) => E", (aInProduction, aInValues) ->
                        new LambdaExpression(
                                (List<String>) aInValues[1],
                                (JSELExpression) aInValues[4]))
                .onReduce("E -> LO ? E : E", (aInProduction, aInValues) ->
                        new ConditionalOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2],
                                (JSELExpression) aInValues[4]))

                        // E -> LO

                .onReduce("LO -> LO || LA", (aInProduction, aInValues) ->
                        new OrOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                        // LO -> LA

                .onReduce("LA -> LA && BO", (aInProduction, aInValues) ->
                        new AndOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                        // LA -> BO

                .onReduce("BO -> BO | BX", (aInProduction, aInValues) ->
                        new BitwiseOrOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                        // BO -> BX

                .onReduce("BX -> BX ^ BA", (aInProduction, aInValues) ->
                        new BitwiseXorOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                        // BX -> BA

                .onReduce("BA -> BA & EQ", (aInProduction, aInValues) ->
                        new BitwiseAndOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                        // BA -> EQ

                .onReduce("EQ -> EQ == REL", (aInProduction, aInValues) ->
                        new EqualsExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("EQ -> EQ === REL", (aInProduction, aInValues) ->
                        new StrictEqualsExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("EQ -> EQ != REL", (aInProduction, aInValues) ->
                        new NotEqualsExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("EQ -> EQ !== REL", (aInProduction, aInValues) ->
                        new StrictNotEqualsExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                        // EQ -> REL

                .onReduce("REL -> REL <= SFT", (aInProduction, aInValues) ->
                        new LessThanOrEqualToExpreassion(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("REL -> REL < SFT", (aInProduction, aInValues) ->
                        new LessThanExpreassion(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("REL -> REL > SFT", (aInProduction, aInValues) ->
                        new GreaterThanExpreassion(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("REL -> REL >= SFT", (aInProduction, aInValues) ->
                        new GreaterThanOrEqualToExpreassion(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("REL -> REL in SFT", (aInProduction, aInValues) ->
                        new InOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
//        .onReduce("REL -> REL instof SFT", (aInProduction, aInValues) ->
//                new LessThanOrEqualToExpreassion(
//                        (JSELExpression) aInValues[0],
//                        (JSELExpression) aInValues[2]))
                        // REL -> SFT

                .onReduce("SFT -> SFT << AD", (aInProduction, aInValues) ->
                        new ShiftLeftExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("SFT -> SFT >> AD", (aInProduction, aInValues) ->
                        new SignedShiftRightExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("SFT -> SFT >>> AD", (aInProduction, aInValues) ->
                        new UnsignedShiftRightExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                        // SFT -> AD

                .onReduce("AD -> AD + MUL", (aInProduction, aInValues) ->
                        new PlusOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("AD -> AD - MUL", (aInProduction, aInValues) ->
                        new SubtractionOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                        // AD -> MUL

                .onReduce("MUL -> MUL * UNR", (aInProduction, aInValues) ->
                        new MultiplicationOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("MUL -> MUL / UNR", (aInProduction, aInValues) ->
                        new DivisionOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                .onReduce("MUL -> MUL % UNR", (aInProduction, aInValues) ->
                        new ModulusOperatorExpression(
                                (JSELExpression) aInValues[0],
                                (JSELExpression) aInValues[2]))
                        // MUL -> UNR

                .onReduce("UNR -> + UNR", (aInProduction, aInValues) ->
                        new UnaryPlusExpression((JSELExpression) aInValues[1]))
                .onReduce("UNR -> - UNR", (aInProduction, aInValues) ->
                        new NegationExpression((JSELExpression) aInValues[1]))
                .onReduce("UNR -> ! UNR", (aInProduction, aInValues) ->
                        new NotExpression((JSELExpression) aInValues[1]))
                .onReduce("UNR -> ~ UNR", (aInProduction, aInValues) ->
                        new BitwiseNotExpression((JSELExpression) aInValues[1]))
                .onReduce("UNR -> typeof UNR", (aInProduction, aInValues) ->
                        new TypeOfExpression((JSELExpression) aInValues[1]))
                .onReduce("UNR -> void UNR", (aInProduction, aInValues) ->
                        new VoidExpression((JSELExpression) aInValues[1]))
                        // UNR -> CALL
                        // UNR -> NEW

                .onReduce("CALL -> AC ( ARGS )", (aInProduction, aInValues) ->
                        new FunctionCallExpression(
                                (JSELExpression) aInValues[0],
                                (List<JSELExpression>) aInValues[2]))
                .onReduce("CALL -> CALL ( ARGS )", (aInProduction, aInValues) ->
                        new FunctionCallExpression(
                                (JSELExpression) aInValues[0],
                                (List<JSELExpression>) aInValues[2]))
                .onReduce("CALL -> CALL [ E ]", (aInProduction, aInValues) ->
                        new AccessExpression(
                                (JSELExpression) aInValues[2],
                                (JSELExpression) aInValues[0]))
                .onReduce("CALL -> CALL . id", (aInProduction, aInValues) ->
                        new AccessExpression(
                                ((Token<?,String>)aInValues[2]).getValue(),
                                (JSELExpression) aInValues[0]))

                .onReduce("NEW -> new AC", (aInProduction, aInValues) ->
                        new NewExpression(
                                (JSELExpression) aInValues[1], emptyList()))
                        // NEW -> AC
                .onReduce("AC -> AC [ E ]", (aInProduction, aInValues) ->
                        new AccessExpression(
                                (JSELExpression) aInValues[2],
                                (JSELExpression) aInValues[0]))
                .onReduce("AC -> AC . id", (aInProduction, aInValues) ->
                        new AccessExpression(
                                ((Token<?,String>)aInValues[2]).getValue(),
                                (JSELExpression) aInValues[0]))
                .onReduce("AC -> new AC ( ARGS )", (aInProduction, aInValues) ->
                        new NewExpression(
                                (JSELExpression) aInValues[1],
                                (List<JSELExpression>) aInValues[3]))
                        // AC -> VAL

                .onReduce("VAL -> ( E )", (aInProduction, aInValues) ->
                        aInValues[1])
                .onReduce("VAL -> id", (aInProduction, aInValues) ->
                        new IdentifierExpression(
                                ((Token<?,String>)aInValues[0]).getValue()))
                .onReduce("VAL -> num", (aInProduction, aInValues) ->
                        new LiteralExpression(
                                ((Token<?,JSELValue>)aInValues[0]).getValue()))
                .onReduce("VAL -> str", (aInProduction, aInValues) ->
                        new LiteralExpression(
                                ((Token<?,JSELValue>)aInValues[0]).getValue()))
                .onReduce("VAL -> boolean", (aInProduction, aInValues) ->
                        new LiteralExpression(
                                ((Token<?,JSELValue>)aInValues[0]).getValue()))
                .onReduce("VAL -> null", (aInProduction, aInValues) ->
                        new LiteralExpression(JSELNull.getInstance()))
                .onReduce("VAL -> regex", (aInProduction, aInValues) -> {
                    String[] lValue = (String[])
                            ((Token<?,?>)aInValues[0]).getValue();
                    try {
                        return new LiteralSupplierExpression(()->
                                new JSELRegExp(lValue[0], lValue[1]));
                    }
                    catch (JSELRuntimeException e) {
                        throw new ParsingException(e.getMessage());
                    }
                })
                .onReduce("VAL -> [ ARGS ]", (aInProduction, aInValues) ->
                        new ArrayExpression(
                                (List<JSELExpression>) aInValues[1]))
                .onReduce("VAL -> { PROPS }", (aInProduction, aInValues) ->
                        aInValues[1])

                        // ARGS -> ARGS2 default
                .onReduce("ARGS -> ''", (aInProduction, aInValues) ->
                        new ArrayList<>())
                .onReduce("ARGS2 -> ARGS2 , E", (aInProduction, aInValues) -> {
                    ((ArrayList)aInValues[0]).add(aInValues[2]);
                    return aInValues[0];
                })
                .onReduce("ARGS2 -> E", (aInProduction, aInValues) ->
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
                .onReduce("PROP -> str : E", (aInProduction, aInValues) ->
                        Pair.of(((Token<?,JSELString>)aInValues[0]).getValue()
                                .toString(),
                                (JSELExpression) aInValues[2]))
                .onReduce("PROP -> id : E", (aInProduction, aInValues) ->
                        Pair.of(((Token<?,String>)aInValues[0]).getValue(),
                                (JSELExpression) aInValues[2]))

                        // PARAMS -> PARAMS2
                .onReduce("PARAMS -> ''", (aInProduction, aInValues) ->
                        new ArrayList<>())
                .onReduce("PARAMS2 -> PARAMS2 , id",
                        (aInProduction, aInValues) -> {
                    ((ArrayList)aInValues[0]).add(
                        ((Token<?,?>)aInValues[2]).getValue());
                    return aInValues[0];
                })
                .onReduce("PARAMS2 -> id", (aInProduction, aInValues) ->
                        new ArrayList<>(singletonList(
                                ((Token<?,String>)aInValues[0]).getValue())))

        ;
    }
}
