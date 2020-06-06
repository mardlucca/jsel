/*
 * File: Type.java
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

import java.util.List;

/**
 * These represent the six different date types in JSEL/ECMAScript 5.1.
 * The spec also mentions reference types, list types and completion types, but
 * those are not used in JSEL.
 * <p>A {@link JSELPropertyReference reference type} in JSEL is a special case.
 * The {@link JSELPropertyReference#getType() type of a reference} reported by
 * JSEL is essentially the same type as the underlying property. To check if
 * JSELValue is a reference you need to invoke method {@link JSELValue
 * #isReference()}</p>
 * <p>List types are note really concrete types, both in the spec and in JSEL.
 * Whenever list types are needed in JSEL, an actual {@link java.util.List java
 * list} is used in its place, which is the case, for example, in method {@link
 * JSELFunction#call(JSELValue, List, ExecutionContext)}</p>
 * <p>Finally, since JSEL does not implement ECMAScript 5.1 grammar syntax,
 * completion types are not required.</p>
 */
public enum Type {
    OBJECT,
    NUMBER,
    STRING,
    BOOLEAN,
    UNDEFINED,

    /**
     * This is a little bit of a nuance. While in Javascript,
     * <code>typeof null == 'object'</code>, internally "null" is one of the
     * native primitive types.
     *
     * <p>See <a href="http://www.ecma-international.org/ecma-262/5.1/#sec-8.2">
     *     Section 8.2 in ECMA 5.1 specs</a></p>
     */
    NULL("object");

    /**
     * Constructor
     */
    Type() {
        typeOfText = name().toLowerCase();
    }

    /**
     * Constructor
     * This takes the name of the type as a string, as output by the "typeOf"
     * operator.
     * @param aInTypeOfText the text used in the typeOf operator. Note that
     *                      while "null" has it's unique data type, the typeOf
     *                      operator outputs "object" for it, not sure why.
     */
    Type(String aInTypeOfText) {
        typeOfText = aInTypeOfText;
    }

    private String typeOfText;

    @Override
    public String toString() {
        return typeOfText;
    }
}
