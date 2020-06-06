/*
 * File: JSELRuntimeException.java
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

public class JSELRuntimeException extends RuntimeException {
    public JSELRuntimeException(String message) {
        super(message);
    }

    public static JSELRuntimeException cannotReadProperty(String aInProperty,
            String aInTarget) {
        return new JSELRuntimeException("Cannot read property '" + aInProperty
                + "' of " + aInTarget);
    }

    public static JSELRuntimeException typeError(String aInMessage) {
        return new JSELRuntimeException(aInMessage);
    }

    public static JSELRuntimeException referenceError(String aInReference) {
        return new JSELRuntimeException(aInReference + " is not defined");
    }

    public static JSELRuntimeException notImplemented(String aInMessage) {
        return new JSELRuntimeException(
                "'" + aInMessage + "' is not implemented yet");
    }

    public static JSELRuntimeException syntaxError(String aInMessage) {
        return new JSELRuntimeException("SyntaxError: " + aInMessage);
    }

    public static JSELRuntimeException rangeError(String aInMessage) {
        return new JSELRuntimeException(
                "RangeError: " + aInMessage);
    }
}
