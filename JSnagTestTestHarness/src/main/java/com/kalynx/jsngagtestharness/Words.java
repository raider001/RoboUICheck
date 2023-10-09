package com.kalynx.jsngagtestharness;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class Words {
    private final List<String> words;
    private final Random r = new Random();

    public Words() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("words_alpha.txt");
        words = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines().toList();
    }

    public String getRandomWord() {
        int i = r.nextInt(0, words.size());
        return words.get((i));
    }
}
