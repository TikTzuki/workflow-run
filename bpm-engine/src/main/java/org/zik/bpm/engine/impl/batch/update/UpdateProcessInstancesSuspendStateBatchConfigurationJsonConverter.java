// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.update;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import com.google.gson.JsonElement;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class UpdateProcessInstancesSuspendStateBatchConfigurationJsonConverter extends JsonObjectConverter<UpdateProcessInstancesSuspendStateBatchConfiguration>
{
    public static final UpdateProcessInstancesSuspendStateBatchConfigurationJsonConverter INSTANCE;
    public static final String PROCESS_INSTANCE_IDS = "processInstanceIds";
    public static final String PROCESS_INSTANCE_ID_MAPPINGS = "processInstanceIdMappings";
    public static final String SUSPENDING = "suspended";
    
    @Override
    public JsonObject toJsonObject(final UpdateProcessInstancesSuspendStateBatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addListField(json, "processInstanceIds", configuration.getIds());
        JsonUtil.addListField(json, "processInstanceIdMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addField(json, "suspended", configuration.getSuspended());
        return json;
    }
    
    @Override
    public UpdateProcessInstancesSuspendStateBatchConfiguration toObject(final JsonObject json) {
        final UpdateProcessInstancesSuspendStateBatchConfiguration configuration = new UpdateProcessInstancesSuspendStateBatchConfiguration(this.readProcessInstanceIds(json), this.readMappings(json), JsonUtil.getBoolean(json, "suspended"));
        return configuration;
    }
    
    protected List<String> readProcessInstanceIds(final JsonObject jsonObject) {
        return JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "processInstanceIds"));
    }
    
    protected DeploymentMappings readMappings(final JsonObject jsonObject) {
        return JsonUtil.asList(JsonUtil.getArray(jsonObject, "processInstanceIdMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new UpdateProcessInstancesSuspendStateBatchConfigurationJsonConverter();
    }
}
