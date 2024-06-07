package com.kalynx.uitestframework.arg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.python.google.common.util.concurrent.AtomicDouble;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class ArgParserTest {

    @Test
    void parse_testInteger() {
        ArgParser parser = new ArgParser();
        AtomicInteger inte = new AtomicInteger();
        parser.addArg("port", Integer.class)
                .setDefault(3)
                .setCommand(inte::set)
                .setShortKey('p');
        parser.parse("--port", "1337");
        Assertions.assertEquals(1337, inte.get());

        parser.parse("-p", "123");
        Assertions.assertEquals(123, inte.get());

        parser.parse();
        Assertions.assertEquals(3, inte.get());
    }

    @Test
    void parse_testBoolean() {
        ArgParser parser = new ArgParser();
        AtomicBoolean inte = new AtomicBoolean();
        parser.addArg("test", Boolean.class)
                .setDefault(false)
                .setCommand(inte::set)
                .setShortKey('t');
        parser.parse("--test", "true");
        Assertions.assertTrue(inte.get());

        parser.parse("-t", "true");
        Assertions.assertTrue(inte.get());

        parser.parse();
        Assertions.assertFalse(inte.get());

        parser.parse("-t", "True");
        Assertions.assertTrue(inte.get());

        parser.parse("-t", "TRUE");
        Assertions.assertTrue(inte.get());

        parser.parse("-t", "bad");
        Assertions.assertFalse(inte.get());
    }

    @Test
    void parse_testDouble() {
        ArgParser parser = new ArgParser();
        AtomicDouble inte = new AtomicDouble();
        parser.addArg("test", Double.class)
                .setDefault(1.0)
                .setCommand(inte::set)
                .setShortKey('t');
        parser.parse("--test", "1");
        Assertions.assertEquals(1, inte.get());

        parser.parse("-t", "1");
        Assertions.assertEquals(1, inte.get());

        parser.parse();
        Assertions.assertEquals(1, inte.get());

        parser.parse("-t", "1.0");
        Assertions.assertEquals(1, inte.get());

        parser.parse("-t", "12");
        Assertions.assertEquals(12, inte.get());

        parser.parse("--test", "-2");
        Assertions.assertEquals(-2, inte.get());

        parser.parse("--test", "-1");
        Assertions.assertEquals(-1, inte.get());

        Assertions.assertThrows(NumberFormatException.class, () -> parser.parse("-t", "bad"));

    }

    @Test
    void parse_testInt() {
        ArgParser parser = new ArgParser();
        AtomicDouble inte = new AtomicDouble();
        parser.addArg("test", Integer.class)
                .setDefault(1)
                .setCommand(inte::set)
                .setShortKey('t');
        parser.parse("--test", "5");
        Assertions.assertEquals(5, inte.get());

        parser.parse("-t", "2");
        Assertions.assertEquals(2, inte.get());

        parser.parse();
        Assertions.assertEquals(1, inte.get());

        parser.parse("-t", "7");
        Assertions.assertEquals(7, inte.get());

        parser.parse("-t", "12");
        Assertions.assertEquals(12, inte.get());

        parser.parse("--test", "-2");
        Assertions.assertEquals(-2, inte.get());

        parser.parse("--test", "-1");
        Assertions.assertEquals(-1, inte.get());

        Assertions.assertThrows(NumberFormatException.class, () -> parser.parse("-t", "bad"));
    }

    @Test
    void parse_testFloat() {
        ArgParser parser = new ArgParser();
        AtomicDouble inte = new AtomicDouble();
        parser.addArg("test", Float.class)
                .setDefault(1f)
                .setCommand(inte::set)
                .setShortKey('t');
        parser.parse("--test", "5");
        Assertions.assertEquals(5, inte.get());

        parser.parse("-t", "2");
        Assertions.assertEquals(2, inte.get());

        parser.parse();
        Assertions.assertEquals(1, inte.get());

        parser.parse("-t", "7");
        Assertions.assertEquals(7, inte.get());

        parser.parse("-t", "12");
        Assertions.assertEquals(12, inte.get());

        parser.parse("--test", "-2");
        Assertions.assertEquals(-2, inte.get());

        parser.parse("--test", "-1");
        Assertions.assertEquals(-1, inte.get());

        Assertions.assertThrows(NumberFormatException.class, () -> parser.parse("-t", "bad"));
    }

    @Test
    void parse_testString() {
        ArgParser parser = new ArgParser();
        AtomicReference<String> inte = new AtomicReference<>("");
        parser.addArg("test", String.class)
                .setDefault("")
                .setCommand(inte::set)
                .setShortKey('t');
        parser.parse("--test", "blarg");
        Assertions.assertEquals("blarg", inte.get());

        parser.parse("-t", "12");
        Assertions.assertEquals("12", inte.get());

        parser.parse();
        Assertions.assertEquals("", inte.get());

        parser.parse("-t", "A Test");
        Assertions.assertEquals("A Test", inte.get());
    }

    @Test
    void parse_intArray() {
        ArgParser parser = new ArgParser();
        AtomicReference<List<Integer>> inte = new AtomicReference<>(new ArrayList<>());
        parser.addArg("test", Integer[].class)
                .setDefault(new Integer[]{})
                .setCommand(v -> inte.set(new ArrayList<>(List.of(v))))
                .setShortKey('t');
        parser.parse("--test", "1","2","3","4");
        Assertions.assertEquals(List.of(1,2,3,4), inte.get());

    }
}
