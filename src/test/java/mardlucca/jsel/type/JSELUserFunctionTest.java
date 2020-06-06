/*
 * File: JSELUserFunctionTest.java
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

import mardlucca.jsel.builtin.object.ObjectConstructor;
import mardlucca.jsel.env.DeclarativeEnvironmentRecord;
import mardlucca.jsel.env.ExecutionContext;
import mardlucca.jsel.expr.JSELExpression;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class JSELUserFunctionTest {
    private JSELObject thisBinding = new JSELObject();
    private ExecutionContext executionContext = new ExecutionContext();
    private JSELUserFunction noParamFn = new JSELUserFunction(
            "noParamFn",
            emptyList(),
            aInContext -> {
                aInContext.bind("local", new JSELString("local"));
                return JSELNull.getInstance();
            },
            executionContext.getEnvironmentRecord());

    @Test
    public void callTest() {
        assertEquals(JSELUndefined.getInstance(),
                executionContext.resolve("local"));
        noParamFn.call(thisBinding, emptyList(), executionContext);
        noParamFn.call(thisBinding, singletonList(JSELBoolean.TRUE),
                executionContext);
        assertEquals(JSELUndefined.getInstance(),
                executionContext.resolve("local"));
    }

    @Test
    public void callTestWithParameters() {
        JSELValue lP1 = new JSELNumber(1);
        JSELBoolean lP2 = JSELBoolean.TRUE;
        JSELValue lReturnValue = JSELBoolean.FALSE;
        JSELObject lThisBind = new JSELObject();

        JSELUserFunction lF1 = new JSELUserFunction(
                "f1",
                asList("p1", "p2"),
                new AssertExpression()
                        .expect("p1", lP1)
                        .expect("p2", lP2)
                        .thisBind(lThisBind)
                        .returnValue(lReturnValue),
                executionContext.getEnvironmentRecord());

        assertEquals(lReturnValue,
                lF1.call(lThisBind, asList(lP1, lP2), executionContext));
    }

    @Test
    public void callTestWithParametersMissing() {
        JSELValue lP1 = new JSELNumber(1);
        JSELValue lReturnValue = JSELBoolean.TRUE;
        JSELObject lThisBind = new JSELObject();

        JSELUserFunction lF1 = new JSELUserFunction(
                asList("p1", "p2"),
                new AssertExpression()
                        .expect("p1", lP1)
                        .expect("p2", JSELUndefined.getInstance())
                        .thisBind(lThisBind)
                        .returnValue(lReturnValue),
                executionContext.getEnvironmentRecord());

        assertEquals(lReturnValue,
                lF1.call(lThisBind, singletonList(lP1), executionContext));
    }

    @Test
    public void toStringTest() {
        assertEquals("f noParamFn() { [source code] }", noParamFn.toString());
    }

    @Test
    public void testInstantiate() {
        ExecutionContext lContext = new ExecutionContext();
        lContext.setAsThreadContext();
        try {
            JSELUserFunction constructor = new JSELUserFunction(
                    singletonList("arg"),
                    aInContext -> {
                        aInContext.getThisBinding().put(
                                "x", aInContext.resolve("arg"));
                        return aInContext.getThisBinding();
                    },
                    new DeclarativeEnvironmentRecord());

            constructor.get(ObjectConstructor.PROTOTYPE)
                    .toObject().put("y", new JSELNumber(20));

            JSELObject lObject = constructor.instantiate(
                    singletonList(new JSELNumber(10)), lContext);
            assertNotNull(lObject);

            assertEquals(
                    constructor.get(ObjectConstructor.PROTOTYPE),
                    lObject.getPrototype());
            assertEquals(new JSELNumber(10), lObject.get("x"));
            assertEquals(new JSELNumber(20), lObject.get("y"));
            assertFalse(lObject.hasOwnProperty("y"));
            assertTrue(lObject.hasProperty("y"));
        }
        finally {
            ExecutionContext.clearThreadContext();
        }
    }

    public class AssertExpression implements JSELExpression {
        private Map<String, JSELValue> expectedArguments = new HashMap<>();
        private JSELValue returnValue;
        private JSELValue thisBind;

        @Override
        public JSELValue execute(ExecutionContext aInContext) {
            for (Map.Entry<String, JSELValue> lEntry:
                    expectedArguments.entrySet()) {
                assertEquals(lEntry.getValue(),
                        aInContext.resolve(lEntry.getKey()));
            }
            assertEquals(thisBind, aInContext.getThisBinding());
            return returnValue;
        }

        public AssertExpression expect(String aInKey, JSELValue aInValue) {
            expectedArguments.put(aInKey, aInValue);
            return this;
        }

        public AssertExpression returnValue(JSELValue aInValue) {
            returnValue = aInValue;
            return this;
        }

        public AssertExpression thisBind(JSELValue aInThisBind) {
            thisBind = aInThisBind;
            return this;
        }
    }
}