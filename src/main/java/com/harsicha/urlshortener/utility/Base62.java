package com.harsicha.urlshortener.utility;

import java.util.Random;

public class Base62 {

    private static final int MAX_LENGTH = 7;
    private static final String BASE62_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random random = new Random();

    public static String Random() {
        StringBuilder sb = new StringBuilder(7);
        for (int i = 0; i < MAX_LENGTH; i++) {
            int randInt = random.nextInt(0, BASE62_CHARS.length());
            sb.append(BASE62_CHARS.charAt(randInt));
        }
        return sb.toString();
    }

}
