// Copyright 2025 Hazel Viswanath <viswanath.hazel@gmail.com>.

// This file is part of Chime-Lang.

// Chime-Lang is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
// any later version.

// Chime-Lang is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
// the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See LICENSE in the project root for more details.

// You should have received a copy of the GNU General Public License along with Chime-Lang. If not, see <https://www.gnu.org/licenses/>.

package org.hazelv.chime.test;

import org.hazelv.chime.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final PrintStream standardOut = System.out;
    private final InputStream standardIn = System.in;
    private final PrintStream standardErr = System.err;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayInputStream inputStreamCaptor = new ByteArrayInputStream(new byte[]{(byte)'G'});

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(outputStreamCaptor));
        System.setIn(inputStreamCaptor);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        System.setIn(standardIn);
        System.setErr(standardErr);
    }

    @Test
    @DisplayName("Add")
    void add() {
        Main.main(new String[]{"test/Test.mid"});
        assertEquals("44.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Subtract")
    void subtract() {
        Main.main(new String[]{"test/Test2.mid"});
        assertEquals("15.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Divide")
    void divide() {
        Main.main(new String[]{"test/Test3.mid"});
        assertEquals("2.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Multiply")
    void multiply() {
        Main.main(new String[]{"test/Test4.mid"});
        assertEquals("1890.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Hold&Print")
    void print() {
        Main.main(new String[]{"test/Test5.mid"});
        assertEquals("127.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Bad header")
    void badHeader() {
        Main.main(new String[]{"test/Test6.mid"});
        assertEquals("Error: Unknown Chord: [C3, Eb3, G3, C5, Eb5, G5] at index: 0", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("Input")
    void input() {
        Main.main(new String[]{"test/Test7.mid"});
        assertEquals("71.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("PrintCharInput")
    void printCharInput() {
        Main.main(new String[]{"test/Test8.mid"});
        assertEquals("G", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("PrintLn")
    void printLn() {
        Main.main(new String[]{"test/Test9.mid"});
        assertEquals("\n", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Basic Stack Operations (push&pop)")
    void basicStackOperations() {
        Main.main(new String[]{"test/Test10.mid"});
        assertEquals("38.0", outputStreamCaptor.toString());
        assertEquals("38.0", Main.song.data.get(54).pop().toString());
    }

    @Test
    @DisplayName("CurrentVal as a argument")
    void currentVal() {
        Main.main(new String[]{"test/Test11.mid"});
        assertEquals("65.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Eval less than")
    void evalLessThan() {
        Main.main(new String[]{"test/Test12.mid"});
        assertEquals("1.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Eval equals")
    void evalEquals() {
        Main.main(new String[]{"test/Test13.mid"});
        assertEquals("2.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Eval greater than")
    void evalGreaterThan() {
        Main.main(new String[]{"test/Test14.mid"});
        assertEquals("3.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Jump")
    void jump() {
        Main.main(new String[]{"test/Test15.mid"});
        assertEquals("0.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Jump bad index")
    void jumpBadIndex() {
        Main.main(new String[]{"test/Test16.mid"});
        assertEquals("Error: Jump index out of range for jump from chord: 5 to chord: 23.0", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("Jump If")
    void jumpIf() {
        Main.main(new String[]{"test/Test17.mid"});
        assertEquals("12.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Jump to literal")
    void jumpToLiteral() {
        Main.main(new String[]{"test/Test18.mid"});
        assertEquals("Error: Literal before instruction at chord: 21. Bad jump?", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("Jump not equal")
    void jumpNotEqual() {
        Main.main(new String[]{"test/Test19.mid"});
        assertEquals("119.0", outputStreamCaptor.toString());
    }

    @Test
    @DisplayName("Jump not equal w/ different instruments")
    void jumpNotEqualDifferentInstruments() {
        Main.main(new String[]{"test/Test20-inst.mid"});
        assertEquals("119.0", outputStreamCaptor.toString());
    }
}