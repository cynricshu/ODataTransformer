package util;

import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Cynric
 * Date: 14-3-9
 * Time: 14:02
 */
public class Node<T> {
    public T data;
    public Node<T> parent;
    public List<Node<T>> children = new ArrayList<>();
    public BasicDBObject QueryObject = new BasicDBObject();
}
