/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.javascript.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import org.junit.Test;
import org.mozilla.javascript.*;

/** Test NativeConsole */
public class NativeConsoleTest {

    @Test
    public void testFormatPercentSign() {
        assertFormat(new Object[] {"%%"}, "%");
        assertFormat(new Object[] {"a%%"}, "a%");
        assertFormat(new Object[] {"%%b"}, "%b");
        assertFormat(new Object[] {"a%%b"}, "a%b");
        assertFormat(new Object[] {"a%%%%b"}, "a%%b");
        assertFormat(new Object[] {"a%%c%%b"}, "a%c%b");
    }

    @Test
    public void testFormatInt() {
        assertFormat(new Object[] {"%d", 100}, "100");
        assertFormat(new Object[] {"%d", -100}, "-100");
        assertFormat(new Object[] {"%d", Integer.MAX_VALUE}, String.valueOf(Integer.MAX_VALUE));
        assertFormat(new Object[] {"%d", Integer.MIN_VALUE}, String.valueOf(Integer.MIN_VALUE));
        assertFormat(new Object[] {"%d", Long.MAX_VALUE}, String.valueOf(Long.MAX_VALUE));
        assertFormat(new Object[] {"%d", Long.MIN_VALUE}, String.valueOf(Long.MIN_VALUE));
        assertFormat(new Object[] {"%d", 100.1D}, "100");
        assertFormat(new Object[] {"%d", -100.7D}, "-100");
        assertFormat(new Object[] {"%d", Double.NaN}, "NaN");
        assertFormat(new Object[] {"%d", Double.POSITIVE_INFINITY}, "Infinity");
        assertFormat(new Object[] {"%d", Double.NEGATIVE_INFINITY}, "-Infinity");

        assertFormat(new Object[] {"%d", 9007199254740991.0D}, "9007199254740991");
        assertFormat(new Object[] {"%d", -9007199254740991.0D}, "-9007199254740991");
        assertFormat(new Object[] {"%d", 9007199254740991L}, "9007199254740991");
        assertFormat(new Object[] {"%d", -9007199254740991L}, "-9007199254740991");

        assertFormat(new Object[] {"%d", BigInteger.valueOf(100)}, "100n");
        assertFormat(new Object[] {"%d", BigInteger.valueOf(-100)}, "-100n");
        assertFormat(
                new Object[] {"%d", new BigInteger("1234567890123456789012345678901234567890")},
                "1234567890123456789012345678901234567890n");

        assertFormat(new Object[] {"a%d", 100}, "a100");
        assertFormat(new Object[] {"%db", 100}, "100b");
        assertFormat(new Object[] {"a%db", 100}, "a100b");
        assertFormat(new Object[] {"a%d%db", 100, 200}, "a100200b");
        assertFormat(new Object[] {"a%dc%db", 100, 200}, "a100c200b");
        assertFormat(new Object[] {"a%d%db", 100}, "a100%db");
        assertFormat(new Object[] {"a%db"}, "a%db");

        assertFormat(new Object[] {"%d", SymbolKey.ITERATOR}, "NaN");
    }

    @Test
    public void testFormatFloat() {
        assertFormat(new Object[] {"%f", 100}, "100");
        assertFormat(new Object[] {"%f", -100}, "-100");
        assertFormat(new Object[] {"%f", Integer.MAX_VALUE}, String.valueOf(Integer.MAX_VALUE));
        assertFormat(new Object[] {"%f", Integer.MIN_VALUE}, String.valueOf(Integer.MIN_VALUE));
        assertFormat(new Object[] {"%f", Long.MAX_VALUE}, "9223372036854776000");
        assertFormat(new Object[] {"%f", Long.MIN_VALUE}, "-9223372036854776000");
        assertFormat(new Object[] {"%f", 100.1D}, "100.1");
        assertFormat(new Object[] {"%f", -100.7D}, "-100.7");
        assertFormat(
                new Object[] {"%f", (double) Integer.MAX_VALUE + 0.1D},
                String.valueOf(Integer.MAX_VALUE) + ".1");
        assertFormat(
                new Object[] {"%f", (double) Integer.MIN_VALUE - 0.1D},
                String.valueOf(Integer.MIN_VALUE) + ".1");
        assertFormat(new Object[] {"%f", Double.NaN}, "NaN");
        assertFormat(new Object[] {"%f", Double.POSITIVE_INFINITY}, "Infinity");
        assertFormat(new Object[] {"%f", Double.NEGATIVE_INFINITY}, "-Infinity");

        assertFormat(new Object[] {"%f", 9007199254740991.0D}, "9007199254740991");
        assertFormat(new Object[] {"%f", -9007199254740991.0D}, "-9007199254740991");
        assertFormat(new Object[] {"%f", 9007199254740991L}, "9007199254740991");
        assertFormat(new Object[] {"%f", -9007199254740991L}, "-9007199254740991");

        assertFormat(new Object[] {"%f", BigInteger.valueOf(100)}, "NaN");
        assertFormat(new Object[] {"%f", BigInteger.valueOf(-100)}, "NaN");

        assertFormat(new Object[] {"a%f", 100}, "a100");
        assertFormat(new Object[] {"%fb", 100}, "100b");
        assertFormat(new Object[] {"a%fb", 100}, "a100b");
        assertFormat(new Object[] {"a%f%fb", 100, 200}, "a100200b");
        assertFormat(new Object[] {"a%fc%fb", 100, 200}, "a100c200b");
        assertFormat(new Object[] {"a%f%fb", 100}, "a100%fb");
        assertFormat(new Object[] {"a%fb"}, "a%fb");

        assertFormat(new Object[] {"%f", SymbolKey.ITERATOR}, "NaN");
    }

    private void assertFormat(Object[] args, String expected) {
        try (Context cx = Context.enter()) {
            assertEquals(expected, NativeConsole.format(cx, null, args));
        }
    }

    private void assertException(Object[] args, Class<? extends Exception> ex) {
        try (Context cx = Context.enter()) {
            NativeConsole.format(cx, null, args);
            fail();
        } catch (Exception e) {
            if (!ex.isInstance(e)) {
                fail();
            }
        }
    }
}
