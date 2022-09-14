// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.externaltask;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import com.google.gson.JsonElement;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.batch.SetRetriesBatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class SetExternalTaskRetriesBatchConfigurationJsonConverter extends JsonObjectConverter<SetRetriesBatchConfiguration>
{
    public static final SetExternalTaskRetriesBatchConfigurationJsonConverter INSTANCE;
    public static final String EXTERNAL_TASK_IDS = "externalTaskIds";
    public static final String EXTERNAL_TASK_ID_MAPPINGS = "externalTaskIdMappingss";
    public static final String RETRIES = "retries";
    
    @Override
    public JsonObject toJsonObject(final SetRetriesBatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addListField(json, "externalTaskIds", configuration.getIds());
        JsonUtil.addListField(json, "externalTaskIdMappingss", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addField(json, "retries", configuration.getRetries());
        return json;
    }
    
    @Override
    public SetRetriesBatchConfiguration toObject(final JsonObject json) {
        return new SetRetriesBatchConfiguration(this.readExternalTaskIds(json), this.readIdMappings(json), JsonUtil.getInt(json, "retries"));
    }
    
    protected List<String> readExternalTaskIds(final JsonObject json) {
        return JsonUtil.asStringList(JsonUtil.getArray(json, "externalTaskIds"));
    }
    
    protected DeploymentMappings readIdMappings(final JsonObject json) {
        return JsonUtil.asList(JsonUtil.getArray(json, "externalTaskIdMappingss"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new SetExternalTaskRetriesBatchConfigurationJsonConverter();
    }
}
