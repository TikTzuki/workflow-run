// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import com.google.gson.JsonElement;
import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import org.zik.bpm.engine.impl.cmd.AbstractProcessInstanceModificationCommand;
import org.zik.bpm.engine.impl.json.ModificationCmdJsonConverter;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class RestartProcessInstancesBatchConfigurationJsonConverter extends JsonObjectConverter<RestartProcessInstancesBatchConfiguration>
{
    public static final RestartProcessInstancesBatchConfigurationJsonConverter INSTANCE;
    public static final String PROCESS_INSTANCE_IDS = "processInstanceIds";
    public static final String PROCESS_INSTANCE_ID_MAPPINGS = "processInstanceIdMappings";
    public static final String INSTRUCTIONS = "instructions";
    public static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    public static final String INITIAL_VARIABLES = "initialVariables";
    public static final String SKIP_CUSTOM_LISTENERS = "skipCustomListeners";
    public static final String SKIP_IO_MAPPINGS = "skipIoMappings";
    public static final String WITHOUT_BUSINESS_KEY = "withoutBusinessKey";
    
    @Override
    public JsonObject toJsonObject(final RestartProcessInstancesBatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addListField(json, "processInstanceIds", configuration.getIds());
        JsonUtil.addListField(json, "processInstanceIdMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addField(json, "processDefinitionId", configuration.getProcessDefinitionId());
        JsonUtil.addListField(json, "instructions", ModificationCmdJsonConverter.INSTANCE, configuration.getInstructions());
        JsonUtil.addField(json, "initialVariables", configuration.isInitialVariables());
        JsonUtil.addField(json, "skipCustomListeners", configuration.isSkipCustomListeners());
        JsonUtil.addField(json, "skipIoMappings", configuration.isSkipIoMappings());
        JsonUtil.addField(json, "withoutBusinessKey", configuration.isWithoutBusinessKey());
        return json;
    }
    
    @Override
    public RestartProcessInstancesBatchConfiguration toObject(final JsonObject json) {
        final List<String> processInstanceIds = this.readProcessInstanceIds(json);
        final DeploymentMappings idMappings = this.readIdMappings(json);
        final List<AbstractProcessInstanceModificationCommand> instructions = JsonUtil.asList(JsonUtil.getArray(json, "instructions"), (JsonObjectConverter<AbstractProcessInstanceModificationCommand>)ModificationCmdJsonConverter.INSTANCE);
        return new RestartProcessInstancesBatchConfiguration(processInstanceIds, idMappings, instructions, JsonUtil.getString(json, "processDefinitionId"), JsonUtil.getBoolean(json, "initialVariables"), JsonUtil.getBoolean(json, "skipCustomListeners"), JsonUtil.getBoolean(json, "skipIoMappings"), JsonUtil.getBoolean(json, "withoutBusinessKey"));
    }
    
    protected List<String> readProcessInstanceIds(final JsonObject jsonObject) {
        return JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "processInstanceIds"));
    }
    
    protected DeploymentMappings readIdMappings(final JsonObject jsonObject) {
        return JsonUtil.asList(JsonUtil.getArray(jsonObject, "processInstanceIdMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
    }
    
    static {
        INSTANCE = new RestartProcessInstancesBatchConfigurationJsonConverter();
    }
}
