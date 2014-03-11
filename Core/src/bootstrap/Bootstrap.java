package bootstrap;

import test.mongo.MongoCreateTest;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:28
 */
public class Bootstrap {


    public static void main(String[] args) {
//        for (int i = 0; i < 1000; i++) {
//            DecimalFormat decimalFormat = new DecimalFormat("#.00");
//            double result = GenData.random(1.0, 50.0);
//
//        }

        new MongoCreateTest().insertData();

//        String filterString = "(Age add 5) eq 15";
//        MongoQueryTest mongoQueryTest = new MongoQueryTest();
//        try {
//            mongoQueryTest.test(filterString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
