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
import mardlucca.jsel.builtin.object.ToStringFunction;
import mardlucca.jsel.builtin.object.ValueOfFunction;
import mardlucca.jsel.type.JSELString;
import mardlucca.jsel.type.wrapper.JSELStringObject;

import static mardlucca.jsel.builtin.object.ObjectPrototype.CONSTRUCTOR_PROPERTY;

public class StringPrototype extends JSELStringObject {
    public StringPrototype(ObjectPrototype aInPrototype) {
        super(aInPrototype, JSELString.EMPTY_STRING);
    }

    public void initialize() {
        defineOwnProperty(CONSTRUCTOR_PROPERTY, new StringConstructor(),
                false, true, true);

        defineOwnProperty(
                ToStringFunction.NAME,
                new DefaultToStringFunction(JSELStringObject.CLASS),
                false, true, true);
        defineOwnProperty(
                ValueOfFunction.NAME,
                new DefaultValueOfFunction(JSELStringObject.CLASS),
                false, true, true);

        defineOwnProperty(CharAtFunction.NAME,
                new CharAtFunction(), false, true, true);
        defineOwnProperty(CharCodeAtFunction.NAME,
                new CharCodeAtFunction(), false, true, true);
        defineOwnProperty(ConcatFunction.NAME,
                new ConcatFunction(), false, true, true);
        defineOwnProperty(IndexOfFunction.NAME,
                new IndexOfFunction(), false, true, true);
        defineOwnProperty(LastIndexOfFunction.NAME,
                new LastIndexOfFunction(), false, true, true);
        defineOwnProperty(LocaleCompareFunction.NAME,
                new LocaleCompareFunction(), false, true, true);
        defineOwnProperty(MatchFunction.NAME,
                new MatchFunction(), false, true, true);
        defineOwnProperty(ReplaceFunction.NAME,
                new ReplaceFunction(), false, true, true);
        defineOwnProperty(SearchFunction.NAME,
                new SearchFunction(), false, true, true);
        defineOwnProperty(SliceFunction.NAME,
                new SliceFunction(), false, true, true);
        defineOwnProperty(SplitFunction.NAME,
                new SplitFunction(), false, true, true);
        defineOwnProperty(SubstringFunction.NAME,
                new SubstringFunction(), false, true, true);
        defineOwnProperty(ToLowerCaseFunction.NAME,
                new ToLowerCaseFunction(), false, true, true);
        defineOwnProperty(ToLocaleLowerCaseFunction.NAME,
                new ToLocaleLowerCaseFunction(), false, true, true);
        defineOwnProperty(ToUpperCaseFunction.NAME,
                new ToUpperCaseFunction(), false, true, true);
        defineOwnProperty(ToLocaleUpperCaseFunction.NAME,
                new ToLocaleUpperCaseFunction(), false, true, true);
        defineOwnProperty(TrimFunction.NAME,
                new TrimFunction(), false, true, true);
    }
}
