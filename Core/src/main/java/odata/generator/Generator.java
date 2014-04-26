package odata.generator;

import util.Node;

/**
 * User: Cynric
 * Date: 14-3-20
 * Time: 14:42
 */

public abstract class Generator<T extends Object> {
    public abstract T generateQueryString(Node<Object> root);

}