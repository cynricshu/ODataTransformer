package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * User: Cynric
 * Date: 14-3-10
 * Time: 11:12
 */
public class GenData {
    public static Date genDate() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder stringBuilder = new StringBuilder();
        int year = genYear();
        int month = genMonth();
        int day = genDay();
        stringBuilder.append(String.valueOf(year) + "-" + String.valueOf(month)
                + "-" + String.valueOf(day));
        try {
            return formater.parse(stringBuilder.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int genAge() {
        return genInt(50, 0);
    }

    public static int genYear() {
        return genInt(14, 1990);
    }

    public static int genMonth() {
        return genInt(12, 1);
    }

    public static int genDay() {
        return genInt(29, 1);
    }

    public static int genInt(int base, int shift) {
        return (int) (Math.random() * base) + shift;
    }

    public static String genString(int length) {
        length = (int) (Math.random() * length) + 1;

        StringBuilder builder = new StringBuilder(length);
        builder.append((char) (ThreadLocalRandom.current().nextInt(65, 91)));

        for (int i = 1; i < length; i++) {
            builder.append((char) (ThreadLocalRandom.current().nextInt(97, 123)));
        }
        return builder.toString();
    }
}
