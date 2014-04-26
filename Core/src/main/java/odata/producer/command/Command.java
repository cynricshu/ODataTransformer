package odata.producer.command;


/**
 * User: Cynric
 * Date: 14-3-26
 * Time: 14:43
 */
public interface Command<T> {
    public T execute(CommandContext context);
}
