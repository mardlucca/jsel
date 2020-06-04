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

public class JSELRegExp extends JSELObject
{
    private static final String EMPTY_REGEX_STRING = "(?:)";
    
    public static final String CLASS = "RegExp";
    
    public static final String SOURCE = "source";
    public static final String GLOBAL = "global";
    public static final String IGNORE_CASE = "ignoreCase";
    public static final String MULTILINE = "multiline";
    public static final String LAST_INDEX = "lastIndex";

    private Pattern pattern;
    private Set<Flag> flags;
    private String body;
    private String flagsString;

    public JSELRegExp(String aInBody)
    {
        this(aInBody, (String) null);
    }

    public JSELRegExp(String aInSource, Set<Flag> aInFlags)
    {
        this(aInSource, Flag.getFlagsString(aInFlags));
    }

    public JSELRegExp(String aInBody, String aInFlagsString)
    {
        this(ExecutionContext.getRetExpPrototype(), aInBody, aInFlagsString);
    }

    protected JSELRegExp(
            JSELObject aInPrototype, String aInBody, String aInFlagString)
    {
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
    public String getObjectClass()
    {
        return CLASS;
    }

    public Set<Flag> getFlags()
    {
        return flags;
    }

    public Pattern getPattern()
    {
        return pattern;
    }

    public String getBody()
    {
        return body;
    }

    public String getFlagsString()
    {
        return flagsString;
    }

    @Override
    public String toString()
    {
        return "/" + body + "/" + flagsString;
    }

    @Override
    public MatchResult match(String aInString, int aInIndex)
    {
        Matcher lMatcher = pattern.matcher(aInString);
        if (!lMatcher.find(aInIndex))
        {
            return null;
        }

        String[] lCaptures = new String[lMatcher.groupCount() + 1];
        for (int i = 0; i < lCaptures.length; i++)
        {
            lCaptures[i] = lMatcher.group(i);
        }
        return new MatchResult(lMatcher.start(), lMatcher.end(), lCaptures);
    }

    public enum Flag
    {
        GLOBAL(0),     // no equivalent value in Java
        IGNORE_CASE(Pattern.CASE_INSENSITIVE),
        MULTILINE(Pattern.MULTILINE);

        Flag(int aInFlagValue)
        {
            symbol = String.valueOf(Character.toLowerCase(name().charAt(0)));
            flagValue = aInFlagValue;
        }
        
        private String symbol;
        private int flagValue;


        @Override
        public String toString()
        {
            return symbol;
        }
        
        public static int getFlagsAsInt(Set<Flag> aInFlags)
        {
            if (aInFlags == null)
            {
                return 0;
            }
            
            int lIntFlag = 0;
            for (Flag lFlag : aInFlags)
            {
                lIntFlag |= lFlag.flagValue;
            }

            return lIntFlag;
        }

        public static Set<Flag> getFlags(String aInFlagString)
        {
            if (StringUtils.isEmpty(aInFlagString))
            {
                return Collections.emptySet();
            }

            Set<Flag> lSet = new HashSet<>();

            for (int i = 0; i < aInFlagString.length(); i++)
            {
                Flag lFlag = fromSymbol(Character.toLowerCase(
                        aInFlagString.charAt(i)));
                if (lFlag == null || lSet.contains(lFlag))
                {
                    throw JSELRuntimeException.syntaxError(
                            "Invalid flags supplied to RegExp constructor '"
                                    + aInFlagString + "'");
                }
                lSet.add(lFlag);
            }
            return lSet;
        }

        public static Flag fromSymbol(char aInSymbol)
        {
            for (Flag lFlag : values())
            {
                if (lFlag.symbol.charAt(0) == aInSymbol)
                {
                    return lFlag;
                }
            }

            return null;
        }

        public static String getFlagsString(Set<Flag> aInFlags)
        {
            StringBuilder lStringBuilder = new StringBuilder();

            for (Flag lFlag : aInFlags)
            {
                lStringBuilder.append(lFlag.toString());
            }

            return lStringBuilder.toString();
        }
    }
}
