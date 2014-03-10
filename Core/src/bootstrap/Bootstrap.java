package bootstrap;

import test.mongo.CreateTest;
import test.mongo.QueryTest;
import util.GenData;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:28
 */
public class Bootstrap {


    public static void main(String[] args) {
//        new CreateTest().insertData();
        try {
            new QueryTest().testTrim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
