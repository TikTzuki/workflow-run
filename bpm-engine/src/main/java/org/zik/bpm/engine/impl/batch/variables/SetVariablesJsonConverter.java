// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.variables;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import com.google.gson.JsonElement;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class SetVariablesJsonConverter extends JsonObjectConverter<BatchConfiguration>
{
    public static final SetVariablesJsonConverter INSTANCE;
    protected static final String IDS = "ids";
    protected static final String ID_MAPPINGS = "idMappings";
    protected static final String BATCH_ID = "batchId";
    
    @Override
    public JsonObject toJsonObject(final BatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addListField(json, "ids", configuration.getIds());
        JsonUtil.addListField(json, "idMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addField(json, "batchId", configuration.getBatchId());
        return json;
    }
    
    @Override
    public BatchConfiguration toObject(final JsonObject jsonObject) {
        final List<String> instanceIds = JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "ids"));
        final DeploymentMappings mappings = JsonUtil.asList(JsonUtil.getArray(jsonObject, "idMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
        final String batchId = JsonUtil.getString(jsonObject, "batchId");
        return new BatchConfiguration(instanceIds, mappings, batchId);
    }
    
    static {
        INSTANCE = new SetVariablesJsonConverter();
    }
}
