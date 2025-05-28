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
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayInputStream inputStreamCaptor = new ByteArrayInputStream(new byte[0]);

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setIn(inputStreamCaptor);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        System.setIn(standardIn);
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
        Main.main(new String[]{"test/Test1.mid"});
        assertEquals("15.0", outputStreamCaptor.toString().trim());
    }
}