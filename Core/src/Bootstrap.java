import transformer.DataTransformer;
import transformer.ODataFilterToMongoDBTransformer;

import java.util.Scanner;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:28
 */
public class Bootstrap {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        DataTransformer transformer = new ODataFilterToMongoDBTransformer();

        while (in.hasNextLine()) {
            String filterString = in.nextLine();

            if (filterString != null && !"".equals(filterString)) {
                System.out.println(transformer.transform(filterString, null));
            } else {
                System.out.println("no input");
            }
        }
    }
}
