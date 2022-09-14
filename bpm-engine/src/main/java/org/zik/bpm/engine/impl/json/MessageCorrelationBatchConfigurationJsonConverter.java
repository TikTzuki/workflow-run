// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import com.google.gson.JsonElement;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.batch.message.MessageCorrelationBatchConfiguration;

public class MessageCorrelationBatchConfigurationJsonConverter extends JsonObjectConverter<MessageCorrelationBatchConfiguration>
{
    public static final MessageCorrelationBatchConfigurationJsonConverter INSTANCE;
    public static final String MESSAGE_NAME = "messageName";
    public static final String PROCESS_INSTANCE_IDS = "processInstanceIds";
    public static final String PROCESS_INSTANCE_ID_MAPPINGS = "processInstanceIdMappings";
    public static final String BATCH_ID = "batchId";
    
    @Override
    public JsonObject toJsonObject(final MessageCorrelationBatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addField(json, "messageName", configuration.getMessageName());
        JsonUtil.addListField(json, "processInstanceIds", configuration.getIds());
        JsonUtil.addListField(json, "processInstanceIdMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addField(json, "batchId", configuration.getBatchId());
        return json;
    }
    
    @Override
    public MessageCorrelationBatchConfiguration toObject(final JsonObject json) {
        return new MessageCorrelationBatchConfiguration(this.readProcessInstanceIds(json), this.readIdMappings(json), JsonUtil.getString(json, "messageName", null), JsonUtil.getString(json, "batchId"));
    }
    
    protected List<String> readProcessInstanceIds(final JsonObject jsonObject) {
        return JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "processInstanceIds"));
    }
    
    protected DeploymentMappings readIdMappings(final JsonObject json) {
        return JsonUtil.asList(JsonUtil.getArray(json, "processInstanceIdMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new MessageCorrelationBatchConfigurationJsonConverter();
    }
}
