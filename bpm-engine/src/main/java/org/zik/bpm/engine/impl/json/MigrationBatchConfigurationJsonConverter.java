// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import com.google.gson.JsonElement;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.migration.batch.MigrationBatchConfiguration;

public class MigrationBatchConfigurationJsonConverter extends JsonObjectConverter<MigrationBatchConfiguration>
{
    public static final MigrationBatchConfigurationJsonConverter INSTANCE;
    public static final String MIGRATION_PLAN = "migrationPlan";
    public static final String PROCESS_INSTANCE_IDS = "processInstanceIds";
    public static final String PROCESS_INSTANCE_ID_MAPPINGS = "processInstanceIdMappings";
    public static final String SKIP_LISTENERS = "skipListeners";
    public static final String SKIP_IO_MAPPINGS = "skipIoMappings";
    public static final String BATCH_ID = "batchId";
    
    @Override
    public JsonObject toJsonObject(final MigrationBatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addField(json, "migrationPlan", MigrationPlanJsonConverter.INSTANCE, configuration.getMigrationPlan());
        JsonUtil.addListField(json, "processInstanceIds", configuration.getIds());
        JsonUtil.addListField(json, "processInstanceIdMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addField(json, "skipListeners", configuration.isSkipCustomListeners());
        JsonUtil.addField(json, "skipIoMappings", configuration.isSkipIoMappings());
        JsonUtil.addField(json, "batchId", configuration.getBatchId());
        return json;
    }
    
    @Override
    public MigrationBatchConfiguration toObject(final JsonObject json) {
        return new MigrationBatchConfiguration(this.readProcessInstanceIds(json), this.readIdMappings(json), JsonUtil.asJavaObject(JsonUtil.getObject(json, "migrationPlan"), (JsonObjectConverter<MigrationPlan>)MigrationPlanJsonConverter.INSTANCE), JsonUtil.getBoolean(json, "skipListeners"), JsonUtil.getBoolean(json, "skipIoMappings"), JsonUtil.getString(json, "batchId"));
    }
    
    protected List<String> readProcessInstanceIds(final JsonObject jsonObject) {
        return JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "processInstanceIds"));
    }
    
    protected DeploymentMappings readIdMappings(final JsonObject json) {
        return JsonUtil.asList(JsonUtil.getArray(json, "processInstanceIdMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new MigrationBatchConfigurationJsonConverter();
    }
}
