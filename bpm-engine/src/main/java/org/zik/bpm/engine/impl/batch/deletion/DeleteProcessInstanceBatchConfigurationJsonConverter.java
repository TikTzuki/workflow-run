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
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class DeleteProcessInstanceBatchConfigurationJsonConverter extends JsonObjectConverter<DeleteProcessInstanceBatchConfiguration>
{
    public static final DeleteProcessInstanceBatchConfigurationJsonConverter INSTANCE;
    public static final String DELETE_REASON = "deleteReason";
    public static final String PROCESS_INSTANCE_IDS = "processInstanceIds";
    public static final String PROCESS_INSTANCE_ID_MAPPINGS = "processInstanceIdMappings";
    public static final String SKIP_CUSTOM_LISTENERS = "skipCustomListeners";
    public static final String SKIP_SUBPROCESSES = "skipSubprocesses";
    public static final String FAIL_IF_NOT_EXISTS = "failIfNotExists";
    
    @Override
    public JsonObject toJsonObject(final DeleteProcessInstanceBatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addField(json, "deleteReason", configuration.getDeleteReason());
        JsonUtil.addListField(json, "processInstanceIdMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addListField(json, "processInstanceIds", configuration.getIds());
        JsonUtil.addField(json, "skipCustomListeners", configuration.isSkipCustomListeners());
        JsonUtil.addField(json, "skipSubprocesses", configuration.isSkipSubprocesses());
        JsonUtil.addField(json, "failIfNotExists", configuration.isFailIfNotExists());
        return json;
    }
    
    @Override
    public DeleteProcessInstanceBatchConfiguration toObject(final JsonObject json) {
        final DeleteProcessInstanceBatchConfiguration configuration = new DeleteProcessInstanceBatchConfiguration(this.readProcessInstanceIds(json), this.readIdMappings(json), null, JsonUtil.getBoolean(json, "skipCustomListeners"), JsonUtil.getBoolean(json, "skipSubprocesses"), JsonUtil.getBoolean(json, "failIfNotExists"));
        final String deleteReason = JsonUtil.getString(json, "deleteReason");
        if (deleteReason != null && !deleteReason.isEmpty()) {
            configuration.setDeleteReason(deleteReason);
        }
        return configuration;
    }
    
    protected List<String> readProcessInstanceIds(final JsonObject jsonObject) {
        return JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "processInstanceIds"));
    }
    
    protected DeploymentMappings readIdMappings(final JsonObject json) {
        return JsonUtil.asList(JsonUtil.getArray(json, "processInstanceIdMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new DeleteProcessInstanceBatchConfigurationJsonConverter();
    }
}
