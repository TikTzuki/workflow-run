// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import com.google.gson.JsonElement;
import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.cmd.AbstractProcessInstanceModificationCommand;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.ModificationBatchConfiguration;

public class ModificationBatchConfigurationJsonConverter extends JsonObjectConverter<ModificationBatchConfiguration>
{
    public static final ModificationBatchConfigurationJsonConverter INSTANCE;
    public static final String INSTRUCTIONS = "instructions";
    public static final String PROCESS_INSTANCE_IDS = "processInstanceIds";
    public static final String PROCESS_INSTANCE_ID_MAPPINGS = "processInstanceIdMappings";
    public static final String SKIP_LISTENERS = "skipListeners";
    public static final String SKIP_IO_MAPPINGS = "skipIoMappings";
    public static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    
    @Override
    public JsonObject toJsonObject(final ModificationBatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addListField(json, "instructions", ModificationCmdJsonConverter.INSTANCE, configuration.getInstructions());
        JsonUtil.addListField(json, "processInstanceIds", configuration.getIds());
        JsonUtil.addListField(json, "processInstanceIdMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addField(json, "processDefinitionId", configuration.getProcessDefinitionId());
        JsonUtil.addField(json, "skipListeners", configuration.isSkipCustomListeners());
        JsonUtil.addField(json, "skipIoMappings", configuration.isSkipIoMappings());
        return json;
    }
    
    @Override
    public ModificationBatchConfiguration toObject(final JsonObject json) {
        final List<String> processInstanceIds = this.readProcessInstanceIds(json);
        final DeploymentMappings mappings = this.readIdMappings(json);
        final String processDefinitionId = JsonUtil.getString(json, "processDefinitionId");
        final List<AbstractProcessInstanceModificationCommand> instructions = JsonUtil.asList(JsonUtil.getArray(json, "instructions"), (JsonObjectConverter<AbstractProcessInstanceModificationCommand>)ModificationCmdJsonConverter.INSTANCE);
        final boolean skipCustomListeners = JsonUtil.getBoolean(json, "skipListeners");
        final boolean skipIoMappings = JsonUtil.getBoolean(json, "skipIoMappings");
        return new ModificationBatchConfiguration(processInstanceIds, mappings, processDefinitionId, instructions, skipCustomListeners, skipIoMappings);
    }
    
    protected List<String> readProcessInstanceIds(final JsonObject jsonObject) {
        return JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "processInstanceIds"));
    }
    
    protected DeploymentMappings readIdMappings(final JsonObject json) {
        return JsonUtil.asList(JsonUtil.getArray(json, "processInstanceIdMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new ModificationBatchConfigurationJsonConverter();
    }
}
