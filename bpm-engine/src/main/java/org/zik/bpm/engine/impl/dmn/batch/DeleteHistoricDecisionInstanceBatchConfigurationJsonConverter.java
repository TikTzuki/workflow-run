// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.batch;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import com.google.gson.JsonElement;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class DeleteHistoricDecisionInstanceBatchConfigurationJsonConverter extends JsonObjectConverter<BatchConfiguration>
{
    public static final DeleteHistoricDecisionInstanceBatchConfigurationJsonConverter INSTANCE;
    public static final String HISTORIC_DECISION_INSTANCE_IDS = "historicDecisionInstanceIds";
    public static final String HISTORIC_DECISION_INSTANCE_ID_MAPPINGS = "historicDecisionInstanceIdMappingss";
    
    @Override
    public JsonObject toJsonObject(final BatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addListField(json, "historicDecisionInstanceIds", configuration.getIds());
        JsonUtil.addListField(json, "historicDecisionInstanceIdMappingss", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        return json;
    }
    
    @Override
    public BatchConfiguration toObject(final JsonObject json) {
        final BatchConfiguration configuration = new BatchConfiguration(this.readDecisionInstanceIds(json), this.readMappings(json));
        return configuration;
    }
    
    protected List<String> readDecisionInstanceIds(final JsonObject jsonNode) {
        return JsonUtil.asStringList(JsonUtil.getArray(jsonNode, "historicDecisionInstanceIds"));
    }
    
    protected DeploymentMappings readMappings(final JsonObject jsonNode) {
        return JsonUtil.asList(JsonUtil.getArray(jsonNode, "historicDecisionInstanceIdMappingss"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new DeleteHistoricDecisionInstanceBatchConfigurationJsonConverter();
    }
}
