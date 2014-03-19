package util;

import java.util.ArrayList;

/**
 * User: Cynric
 * Date: 14-3-9
 * Time: 14:02
 */
public class Tree<T> {
    public Node<T> root;

    public Tree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<>();
    }
}
