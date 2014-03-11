package bootstrap;

import test.mongo.QueryTest;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:28
 */
public class Bootstrap {


    public static void main(String[] args) {
//        new CreateTest().insertData();
        String filterString = "(Age add 5) eq 15";
        QueryTest queryTest = new QueryTest();
        try {
            queryTest.test(filterString);
//            queryTest.testEndswith();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
