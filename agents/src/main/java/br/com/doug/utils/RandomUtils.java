package br.com.doug.utils;

import java.util.Random;

public class RandomUtils {

    private static final Random random = new Random();

    public static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

}
