package odata.transformer.generator;

import util.Node;

/**
 * User: Cynric
 * Date: 14-3-20
 * Time: 14:42
 */

public abstract class Generator {
    public abstract String generateQueryString(Node<Object> root);

}