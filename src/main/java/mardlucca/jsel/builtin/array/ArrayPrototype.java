/*
 * File: ArrayPrototype.java
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
package mardlucca.jsel.builtin.array;

import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.type.JSELArray;
import mardlucca.jsel.builtin.object.ObjectPrototype;
import mardlucca.jsel.type.JSELArray;

import java.util.Map;

import static mardlucca.jsel.builtin.object.ObjectPrototype.CONSTRUCTOR_PROPERTY;

public class ArrayPrototype extends JSELArray {
    public ArrayPrototype(
            ObjectPrototype aInPrototype) {
        super(aInPrototype, 0);
    }

    public void initialize() {
        defineOwnProperty(ObjectPrototype.CONSTRUCTOR_PROPERTY,
                new ArrayConstructor(),
                false, true, true);
        defineOwnProperty(ToStringFunction.NAME,
                new ToStringFunction(),
                false, true, true);
        defineOwnProperty(ToLocaleStringFunction.NAME,
                new ToLocaleStringFunction(),
                false, true, true);
        defineOwnProperty(ConcatFunction.NAME,
                new ConcatFunction(),
                false, true, true);
        defineOwnProperty(JoinFunction.NAME,
                new JoinFunction(),
                false, true, true);
        defineOwnProperty(PopFunction.NAME,
                new PopFunction(),
                false, true, true);
        defineOwnProperty(PushFunction.NAME,
                new PushFunction(),
                false, true, true);
        defineOwnProperty(ReverseFunction.NAME,
                new ReverseFunction(),
                false, true, true);
        defineOwnProperty(ShiftFunction.NAME,
                new ShiftFunction(),
                false, true, true);
        defineOwnProperty(SliceFunction.NAME,
                new SliceFunction(),
                false, true, true);
        defineOwnProperty(SortFunction.NAME,
                new SortFunction(),
                false, true, true);
        defineOwnProperty(SpliceFunction.NAME,
                new SpliceFunction(),
                false, true, true);
        defineOwnProperty(UnshiftFunction.NAME,
                new UnshiftFunction(),
                false, true, true);
        defineOwnProperty(IndexOfFunction.NAME,
                new IndexOfFunction(),
                false, true, true);
        defineOwnProperty(LastIndexOfFunction.NAME,
                new LastIndexOfFunction(),
                false, true, true);
        defineOwnProperty(EveryFunction.NAME,
                new EveryFunction(),
                false, true, true);
        defineOwnProperty(SomeFunction.NAME,
                new SomeFunction(),
                false, true, true);
        defineOwnProperty(ForEachFunction.NAME,
                new EveryFunction(),
                false, true, true);
        defineOwnProperty(MapFunction.NAME,
                new MapFunction(),
                false, true, true);
        defineOwnProperty(FilterFunction.NAME,
                new FilterFunction(),
                false, true, true);
        defineOwnProperty(ReduceFunction.NAME,
                new ReduceFunction(),
                false, true, true);
        defineOwnProperty(ReduceRightFunction.NAME,
                new ReduceRightFunction(),
                false, true, true);
    }
}
