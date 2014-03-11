package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * User: Cynric
 * Date: 14-3-10
 * Time: 11:12
 */
public class GenData {
    public static SimpleDateFormat _formater = new SimpleDateFormat("yyyy-MM-dd");

    public static Date genDate(String beginDate, String endDate) {

        try {
            Date start = _formater.parse(beginDate);// 构造开始日期
            Date end = _formater.parse(endDate);// 构造结束日期

            long date = random(start.getTime(), end.getTime());

            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int genYear(int begin, int end) {
        return random(begin, end);
    }

    public static int genAge(int begin, int end) {
        return random(begin, end);
    }

    public static int random(int begin, int end) {
        int rtn = begin + (int) (Math.random() * (end - begin));
        return rtn;
    }

    public static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        return rtn;
    }

    public static double random(double begin, double end) {
        Random r = new Random();
        double rtn = begin + r.nextDouble() * (end - begin);
        return rtn;
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
