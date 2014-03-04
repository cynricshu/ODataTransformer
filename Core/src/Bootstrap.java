import transformer.DataTransformer;
import transformer.ODataFilterToHQLWhereClauseTransformer;

import java.util.Scanner;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:28
 */
public class Bootstrap {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        while (in.hasNextLine()) {
            String filter = in.nextLine();

            DataTransformer transformer = new ODataFilterToHQLWhereClauseTransformer();
            String hqlWhereClause = transformer.transform(filter, null);

            System.out.println(hqlWhereClause);
        }
    }
}
