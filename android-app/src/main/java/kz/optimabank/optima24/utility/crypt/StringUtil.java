package kz.optimabank.optima24.utility.crypt;

import java.nio.charset.StandardCharsets;

/**
  Created by Timur on 13.01.2017.
 */

public final class StringUtil {
    public static byte[] strToBytes(String str) {
        try {
            return str.getBytes(StandardCharsets.UTF_8);
        } catch (Throwable e) {
            return null;
        }
    }

    public static String bytesToStr(byte[] bytes) {
        try {
            return new String(bytes);
        } catch (Throwable e) {
            return null;
        }
    }

    public static String format(String pattern, Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            String searchedPattern = "{" + i + "}";
            int index = pattern.indexOf(searchedPattern);
            pattern = pattern.substring(0, index) + objects[i] + pattern.substring(index + searchedPattern.length());
        }
        return pattern;
    }


    public static String[] split(String value, char delimiter) {
        return split(value, new char[]{delimiter});
    }

    public static String[] split(String value, char[] delimiters) {
        char[] valueChars = value.toCharArray();
        int lastIndex = 0;
        String[] array = null;
        int size = 0;

        for (int i = 0; i < valueChars.length; i++) {
            char c = valueChars[i];
            if (arrayIncludes(c, delimiters)) {
                if (array == null) {
                    array = new String[30];
                } else if (size > array.length - 1) {
                    String[] oldArray = array;
                    array = new String[array.length + 30];
                    System.arraycopy(oldArray, 0, array, 0, oldArray.length);
                }
                array[size++] = new String(valueChars, lastIndex, i - lastIndex);
                lastIndex = i + 1;
            }
        }

        if (array == null) {
            return new String[]{value};
        } else {
            if (array.length != (size + 1)) {
                String[] newStore = new String[size + 1];
                System.arraycopy(array, 0, newStore, 0, size);
                array = newStore;
            }
            array[array.length - 1] = new String(valueChars, lastIndex, valueChars.length - lastIndex);
            return array;
        }
    }

    private static boolean arrayIncludes(char c, char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == c)
                return true;
        }
        return false;
    }
}
