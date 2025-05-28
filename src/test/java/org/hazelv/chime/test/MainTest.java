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
        assertEquals("44.0", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("Subtract")
    void subtract() {
        Main.main(new String[]{"test/Test2.mid"});
        assertEquals("15.0", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("Divide")
    void divide() {
        Main.main(new String[]{"test/Test3.mid"});
        assertEquals("2.0", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("Multiply")
    void multiply() {
        Main.main(new String[]{"test/Test4.mid"});
        assertEquals("1890.0", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("Hold&Print")
    void print() {
        Main.main(new String[]{"test/Test5.mid"});
        assertEquals("127.0", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("Bad header")
    void badHeader() {
        Main.main(new String[]{"test/Test6.mid"});
        assertEquals("Error: Invalid 'header' for chime file. (Are you sure this is a program?)", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("Input")
    void input() {
        Main.main(new String[]{"test/Test7.mid"});
        assertEquals("71.0", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("PrintCharInput")
    void printCharInput() {
        Main.main(new String[]{"test/Test8.mid"});
        assertEquals("G", outputStreamCaptor.toString().trim());
    }

    @Test
    @DisplayName("PrintLn")
    void printLn() {
        Main.main(new String[]{"test/Test9.mid"});
        assertEquals("\n", outputStreamCaptor.toString());
    }
}