// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.deletion;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import com.google.gson.JsonElement;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class DeleteHistoricProcessInstanceBatchConfigurationJsonConverter extends JsonObjectConverter<BatchConfiguration>
{
    public static final DeleteHistoricProcessInstanceBatchConfigurationJsonConverter INSTANCE;
    public static final String HISTORIC_PROCESS_INSTANCE_IDS = "historicProcessInstanceIds";
    public static final String HISTORIC_PROCESS_INSTANCE_ID_MAPPINGS = "historicProcessInstanceIdMappings";
    public static final String FAIL_IF_NOT_EXISTS = "failIfNotExists";
    
    @Override
    public JsonObject toJsonObject(final BatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addListField(json, "historicProcessInstanceIdMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addListField(json, "historicProcessInstanceIds", configuration.getIds());
        JsonUtil.addField(json, "failIfNotExists", configuration.isFailIfNotExists());
        return json;
    }
    
    @Override
    public BatchConfiguration toObject(final JsonObject json) {
        final BatchConfiguration configuration = new BatchConfiguration(this.readProcessInstanceIds(json), this.readIdMappings(json), JsonUtil.getBoolean(json, "failIfNotExists"));
        return configuration;
    }
    
    protected List<String> readProcessInstanceIds(final JsonObject jsonObject) {
        return JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "historicProcessInstanceIds"));
    }
    
    protected DeploymentMappings readIdMappings(final JsonObject json) {
        return JsonUtil.asList(JsonUtil.getArray(json, "historicProcessInstanceIdMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new DeleteHistoricProcessInstanceBatchConfigurationJsonConverter();
    }
}
