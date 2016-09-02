package com.huskyyy.anotheryouku.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Wang on 2016/7/23.
 */
public class Others {

    private Others() {}

    /**
     * https://svn.apache.org/repos/asf/cayenne/main/branches/cayenne-jdk1.5-generics-unpublished/src/main/java/org/apache/cayenne/conf/Rot47PasswordEncoder.java
     */
    public static String rot47(String value) {
        int length = value.length();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);

            // Process letters, numbers, and symbols -- ignore spaces.
            if (c != ' ') {
                // Add 47 (it is ROT-47, after all).
                c += 47;

                // If character is now above printable range, make it printable.
                // Range of printable characters is ! (33) to ~ (126).  A value
                // of 127 (just above ~) would therefore get rotated down to a
                // 33 (the !).  The value 94 comes from 127 - 33 = 94, which is
                // therefore the value that needs to be subtracted from the
                // non-printable character to put it into the correct printable
                // range.
                if (c > '~') {
                    c -= 94;
                }
            }

            result.append(c);
        }

        return result.toString();
    }
}
