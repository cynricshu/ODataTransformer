package odata.producer.command;


import org.odata4j.core.OEntity;
import org.odata4j.producer.EntitiesResponse;
import util.json.JSONObject;

/**
 * User: Cynric
 * Date: 14-3-26
 * Time: 15:07
 */
public class CreateEntityCmd implements Command {

    protected String entitySetName;
    protected OEntity oEntity;
    protected JSONObject entity;

    public CreateEntityCmd(String entitySetName, JSONObject entity) {
        this.entitySetName = entitySetName;
        this.entity = entity;
    }

    @Override
    public EntitiesResponse execute(CommandContext context) {
        return (EntitiesResponse) context.getDateSource().create(oEntity.getEntitySetName(), entity);
    }
}
