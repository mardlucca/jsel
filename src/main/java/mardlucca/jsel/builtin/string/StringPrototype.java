/*
 * File: StringPrototype.java
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
package mardlucca.jsel.builtin.string;

import mardlucca.jsel.builtin.DefaultToStringFunction;
import mardlucca.jsel.builtin.DefaultValueOfFunction;
import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.wrapper.JSELStringObject;
import mardlucca.jsel.builtin.DefaultToStringFunction;
import mardlucca.jsel.builtin.DefaultValueOfFunction;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.wrapper.JSELStringObject;

import static mardlucca.jsel.builtin.object.ObjectPrototype.CONSTRUCTOR_PROPERTY;
import static mardlucca.jsel.builtin.object.ToStringFunction.TO_STRING;
import static mardlucca.jsel.builtin.object.ValueOfFunction.VALUE_OF;
import static mardlucca.jsel.builtin.string.CharAtFunction.CHAR_AT;
import static mardlucca.jsel.builtin.string.CharCodeAtFunction.CHAR_CODE_AT;
import static mardlucca.jsel.builtin.string.IndexOfFunction.INDEX_OF;
import static mardlucca.jsel.builtin.string.LastIndexOfFunction.LAST_INDEX_OF;
import static mardlucca.jsel.builtin.string.LocaleCompareFunction.LOCALE_COMPARE;
import static mardlucca.jsel.builtin.string.MatchFunction.MATCH;
import static mardlucca.jsel.builtin.string.ReplaceFunction.REPLACE;
import static mardlucca.jsel.builtin.string.SearchFunction.SEARCH;
import static mardlucca.jsel.builtin.string.SliceFunction.SLICE;
import static mardlucca.jsel.builtin.string.SplitFunction.SPLIT;
import static mardlucca.jsel.builtin.string.SubstringFunction.SUBSTRING;
import static mardlucca.jsel.builtin.string.ToLocaleLowerCaseFunction.TO_LOCALE_LOWER_CASE;
import static mardlucca.jsel.builtin.string.ToLocaleUpperCaseFunction.TO_LOCALE_UPPER_CASE;
import static mardlucca.jsel.builtin.string.ToLowerCaseFunction.TO_LOWER_CASE;
import static mardlucca.jsel.builtin.string.ToUpperCaseFunction.TO_UPPER_CASE;
import static mardlucca.jsel.builtin.string.TrimFunction.TRIM;

public class StringPrototype extends JSELStringObject {
    public StringPrototype(ObjectPrototype aInPrototype) {
        super(aInPrototype, JSELString.EMPTY_STRING);
    }

    public void initialize() {
        defineOwnProperty(CONSTRUCTOR_PROPERTY, new StringConstructor(),
                false, true, true);

        defineOwnProperty(
                TO_STRING, new DefaultToStringFunction(JSELStringObject.CLASS),
                false, true, true);
        defineOwnProperty(
                VALUE_OF, new DefaultValueOfFunction(JSELStringObject.CLASS),
                false, true, true);

        defineOwnProperty(CHAR_AT,
                new CharAtFunction(), false, true, true);
        defineOwnProperty(CHAR_CODE_AT,
                new CharCodeAtFunction(), false, true, true);
        defineOwnProperty(INDEX_OF,
                new IndexOfFunction(), false, true, true);
        defineOwnProperty(LAST_INDEX_OF,
                new LastIndexOfFunction(), false, true, true);
        defineOwnProperty(LOCALE_COMPARE,
                new LocaleCompareFunction(), false, true, true);
        defineOwnProperty(MATCH,
                new MatchFunction(), false, true, true);
        defineOwnProperty(REPLACE,
                new ReplaceFunction(), false, true, true);
        defineOwnProperty(SEARCH,
                new SearchFunction(), false, true, true);
        defineOwnProperty(SLICE,
                new SliceFunction(), false, true, true);
        defineOwnProperty(SPLIT,
                new SplitFunction(), false, true, true);
        defineOwnProperty(SubstringFunction.SUBSTRING,
                new SubstringFunction(), false, true, true);
        defineOwnProperty(ToLocaleLowerCaseFunction.TO_LOCALE_LOWER_CASE,
                new ToLocaleLowerCaseFunction(), false, true, true);
        defineOwnProperty(ToLocaleUpperCaseFunction.TO_LOCALE_UPPER_CASE,
                new ToLocaleUpperCaseFunction(), false, true, true);
        defineOwnProperty(ToLowerCaseFunction.TO_LOWER_CASE,
                new ToLowerCaseFunction(), false, true, true);
        defineOwnProperty(ToUpperCaseFunction.TO_UPPER_CASE,
                new ToUpperCaseFunction(), false, true, true);
        defineOwnProperty(SubstringFunction.SUBSTRING,
                new SubstringFunction(), false, true, true);
        defineOwnProperty(TrimFunction.TRIM,
                new TrimFunction(), false, true, true);
    }
}
