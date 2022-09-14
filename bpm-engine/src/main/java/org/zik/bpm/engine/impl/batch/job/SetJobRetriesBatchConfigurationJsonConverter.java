// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.job;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import com.google.gson.JsonElement;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.batch.SetRetriesBatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class SetJobRetriesBatchConfigurationJsonConverter extends JsonObjectConverter<SetRetriesBatchConfiguration>
{
    public static final SetJobRetriesBatchConfigurationJsonConverter INSTANCE;
    public static final String JOB_IDS = "jobIds";
    public static final String JOB_ID_MAPPINGS = "jobIdMappings";
    public static final String RETRIES = "retries";
    
    @Override
    public JsonObject toJsonObject(final SetRetriesBatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addListField(json, "jobIds", configuration.getIds());
        JsonUtil.addListField(json, "jobIdMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addField(json, "retries", configuration.getRetries());
        return json;
    }
    
    @Override
    public SetRetriesBatchConfiguration toObject(final JsonObject json) {
        final SetRetriesBatchConfiguration configuration = new SetRetriesBatchConfiguration(this.readJobIds(json), this.readIdMappings(json), JsonUtil.getInt(json, "retries"));
        return configuration;
    }
    
    protected List<String> readJobIds(final JsonObject jsonObject) {
        return JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "jobIds"));
    }
    
    protected DeploymentMappings readIdMappings(final JsonObject jsonObject) {
        return JsonUtil.asList(JsonUtil.getArray(jsonObject, "jobIdMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new SetJobRetriesBatchConfigurationJsonConverter();
    }
}
