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

public enum Type
{
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
    NULL("object"),
    FUNCTION;

    Type()
    {
        typeOfText = name().toLowerCase();
    }

    Type(String aInTypeOfText)
    {
        typeOfText = aInTypeOfText;
    }

    private String typeOfText;

    @Override
    public String toString()
    {
        return typeOfText;
    }
}
