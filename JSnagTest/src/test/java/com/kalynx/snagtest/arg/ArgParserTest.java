package com.kalynx.snagtest.arg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class ArgParserTest {

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
}
