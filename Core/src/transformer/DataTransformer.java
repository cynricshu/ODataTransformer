package transformer;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:45
 */
public interface DataTransformer {
    String transform(String inputData, Object[] params);

    String getSourceTypeName();

    public String getDistTypeName();
}
