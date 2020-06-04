/*
 * File: JSELString.java
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

import mardlucca.jsel.type.wrapper.JSELStringObject;
import mardlucca.jsel.util.DecimalFormat;
import mardlucca.jsel.util.DecimalFormat;

public class JSELString extends JSELValue
{
    public static final JSELString EMPTY_STRING = new JSELString("");

    private String string;

    public JSELString(String aInString)
    {
        string = aInString;
    }

    @Override
    public Type getType()
    {
        return Type.STRING;
    }

    @Override
    public boolean isPrimitive()
    {
        return true;
    }

    @Override
    public boolean toBoolean()
    {
        return !string.equals("");
    }

    @Override
    public double toNumber()
    {
        return DecimalFormat.parse(string);
    }

    @Override
    public JSELObject toObject()
    {
        return new JSELStringObject(this);
    }

    @Override
    public JSELValue toPrimitive(GetHint aInHint)
    {
        return this;
    }

    @Override
    public String toString()
    {
        return string;
    }

    @Override
    public boolean equals(JSELValue aInValue)
    {
        aInValue = aInValue.getValue();
        if (aInValue == this)
        {
            return true;
        }

        if (aInValue.getType() == Type.BOOLEAN)
        {
            return new JSELNumber(toNumber()).equals(
                    new JSELNumber(aInValue.toNumber()));
        }

        if (aInValue.getType() == Type.NUMBER)
        {
            return new JSELNumber(toNumber()).equals(aInValue);
        }

        if (aInValue.getType() == Type.STRING)
        {
            return string.equals(aInValue.toString());
        }

        if (aInValue.getType() == Type.OBJECT)
        {
            return equals(aInValue.toPrimitive(GetHint.STRING));
        }

        return false;
    }

    @Override
    public boolean strictEquals(JSELValue aInValue)
    {
        aInValue = aInValue.getValue();
        if (aInValue == this)
        {
            return true;
        }

        if (aInValue.getType() == Type.STRING)
        {
            return string.equals(aInValue.toString());
        }

        return false;
    }

    @Override
    public MatchResult match(String aInString, int aInIndex)
    {
        int lMatchIndex = string.indexOf(aInString, aInIndex);
        return lMatchIndex == -1
                ? null
                : new MatchResult(
                        lMatchIndex,
                        lMatchIndex + aInString.length(),
                        new String[] {aInString});
    }
}
