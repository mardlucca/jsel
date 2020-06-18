/*
 * File: JSELRegExp.java
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
package mardlucca.jsel.type;

import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.JSELRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static mardlucca.jsel.JSELRuntimeException.syntaxError;

/**
 * This represents a regexp object type in JSEL. This uses Java's
 * {@link Pattern} class to implement regular expression matching.
 */
public class JSELRegExp extends JSELObject {
    /**
     * Constant for an empty regexp string
     */
    private static final String EMPTY_REGEX_STRING = "(?:)";

    /**
     * Constant used for the internal [[Class]] property for objects of this
     * type.
     */
    public static final String CLASS = "RegExp";

    /**
     * Constant for the name of property "source"
     */
    public static final String SOURCE = "source";

    /**
     * Constant for the name of property "global"
     */
    public static final String GLOBAL = "global";

    /**
     * Constant for the name of property "ignoreCase"
     */
    public static final String IGNORE_CASE = "ignoreCase";

    /**
     * Constant for the name of property "multiline"
     */
    public static final String MULTILINE = "multiline";

    /**
     * Constant for the name of property "lastIndex"
     */
    public static final String LAST_INDEX = "lastIndex";

    private Pattern pattern;
    private Set<Flag> flags;
    private String body;
    private String flagsString;

    /**
     * Creates a new reg exp object with default flags
     * @param aInBody he regular expression. If null, the empty regexp is used.
     */
    public JSELRegExp(String aInBody) {
        this(aInBody, (String) null);
    }

    /**
     * Creates a new reg exp  object
     * @param aInBody he regular expression. If null, the empty regexp is
     *                  used.
     * @param aInFlags a set of regex flags
     */
    public JSELRegExp(String aInBody, Set<Flag> aInFlags) {
        this(aInBody, Flag.getFlagsString(aInFlags));
    }

    /**
     * Creates a new reg exp  object
     * @param aInBody he regular expression. If null, the empty regexp is used.
     * @param aInFlagsString a string containing regular expression flags.
     */
    public JSELRegExp(String aInBody, String aInFlagsString) {
        this(ExecutionContext.getRegExpPrototype(), aInBody, aInFlagsString);
    }

    /**
     * Creates a new reg exp  object
     * @param aInPrototype the RegExp prototype object
     * @param aInBody the regular expression. If null, the empty regexp is used.
     * @param aInFlagString the flags
     */
    protected JSELRegExp(
            JSELObject aInPrototype, String aInBody, String aInFlagString) {
        super(aInPrototype);
        body = aInBody == null
                ? EMPTY_REGEX_STRING
                : aInBody;
        flagsString = aInFlagString == null ? "" : aInFlagString;
        flags = aInFlagString == null
                ? Collections.emptySet()
                : Flag.getFlags(aInFlagString);
        pattern = Pattern.compile(body, Flag.getFlagsAsInt(flags));

        defineOwnProperty(SOURCE, new JSELString(aInBody),
                false, true, false);

        defineOwnProperty(GLOBAL, 
                flags.contains(Flag.GLOBAL) ?
                        JSELBoolean.TRUE : 
                        JSELBoolean.FALSE,
                false, true, false);
        defineOwnProperty(IGNORE_CASE,
                flags.contains(Flag.IGNORE_CASE) ?
                        JSELBoolean.TRUE :
                        JSELBoolean.FALSE,
                false, true, false);
        defineOwnProperty(MULTILINE,
                flags.contains(Flag.MULTILINE) ?
                        JSELBoolean.TRUE :
                        JSELBoolean.FALSE,
                false, true, false);
        
        defineOwnProperty(LAST_INDEX, new JSELNumber(0),
                false, true, false);
    }

    @Override
    public String getObjectClass() {
        return CLASS;
    }

    public Set<Flag> getFlags() {
        return flags;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getBody() {
        return body;
    }

    public String getFlagsString() {
        return flagsString;
    }

    @Override
    public String toString() {
        return "/" + body + "/" + flagsString;
    }

    @Override
    public MatchResult match(String aInString, int aInIndex) {
        Matcher lMatcher = pattern.matcher(aInString);
        if (!lMatcher.find(aInIndex)) {
            return null;
        }

        String[] lCaptures = new String[lMatcher.groupCount() + 1];
        for (int i = 0; i < lCaptures.length; i++) {
            lCaptures[i] = lMatcher.group(i);
        }
        return new MatchResult(lMatcher.start(), lMatcher.end(), lCaptures);
    }

    /**
     * Enum with regular expression flags
     */
    public enum Flag {
        GLOBAL(0),     // no equivalent value in Java
        IGNORE_CASE(Pattern.CASE_INSENSITIVE),
        MULTILINE(Pattern.MULTILINE);

        /**
         * Constructor
         * @param aInFlagValue a numeric value (power of two) to represent a
         *                     flag value.
         */
        Flag(int aInFlagValue) {
            symbol = String.valueOf(Character.toLowerCase(name().charAt(0)));
            flagValue = aInFlagValue;
        }
        
        private String symbol;
        private int flagValue;


        @Override
        public String toString() {
            return symbol;
        }

        /**
         * Converts a set of flags into an int that can be used in a
         * {@link Pattern}
         * @param aInFlags the flags
         * @return the flags as an int
         */
        public static int getFlagsAsInt(Set<Flag> aInFlags) {
            if (aInFlags == null) {
                return 0;
            }
            
            int lIntFlag = 0;
            for (Flag lFlag : aInFlags) {
                lIntFlag |= lFlag.flagValue;
            }

            return lIntFlag;
        }

        /**
         * Converts a string with flags into a set of flags.
         * @param aInFlagString a string with flags.
         * @return the set of flags
         * @throws JSELRuntimeException a "SyntaxError" if the string contains
         * any unsupported flags.
         */
        public static Set<Flag> getFlags(String aInFlagString) {
            if (StringUtils.isEmpty(aInFlagString)) {
                return Collections.emptySet();
            }

            Set<Flag> lSet = new HashSet<>();

            for (int i = 0; i < aInFlagString.length(); i++) {
                Flag lFlag = fromSymbol(Character.toLowerCase(
                        aInFlagString.charAt(i)));
                if (lFlag == null || lSet.contains(lFlag)) {
                    throw syntaxError(
                            "Invalid flags supplied to RegExp constructor '"
                                    + aInFlagString + "'");
                }
                lSet.add(lFlag);
            }
            return lSet;
        }

        /**
         * Returns a Flag from a character symbol.
         * @param aInSymbol the symbol
         * @return the flag
         */
        public static Flag fromSymbol(char aInSymbol) {
            for (Flag lFlag : values()) {
                if (lFlag.symbol.charAt(0) == aInSymbol) {
                    return lFlag;
                }
            }

            return null;
        }

        /**
         * Converts a set of flags into a flags string.
         * @param aInFlags the set of flags
         * @return the flags string
         */
        public static String getFlagsString(Set<Flag> aInFlags) {
            StringBuilder lStringBuilder = new StringBuilder();

            for (Flag lFlag : aInFlags) {
                lStringBuilder.append(lFlag.toString());
            }

            return lStringBuilder.toString();
        }
    }
}
