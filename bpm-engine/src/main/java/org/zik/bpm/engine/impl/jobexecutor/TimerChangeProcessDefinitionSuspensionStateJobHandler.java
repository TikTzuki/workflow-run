// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import com.google.gson.JsonObject;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.cmd.AbstractSetProcessDefinitionStateCmd;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.repository.UpdateProcessDefinitionSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.util.JsonUtil;

public abstract class TimerChangeProcessDefinitionSuspensionStateJobHandler implements JobHandler<TimerChangeProcessDefinitionSuspensionStateJobHandler.ProcessDefinitionSuspensionStateConfiguration> {
    protected static final String JOB_HANDLER_CFG_BY = "by";
    protected static final String JOB_HANDLER_CFG_PROCESS_DEFINITION_ID = "processDefinitionId";
    protected static final String JOB_HANDLER_CFG_PROCESS_DEFINITION_KEY = "processDefinitionKey";
    protected static final String JOB_HANDLER_CFG_PROCESS_DEFINITION_TENANT_ID = "processDefinitionTenantId";
    protected static final String JOB_HANDLER_CFG_INCLUDE_PROCESS_INSTANCES = "includeProcessInstances";

    @Override
    public void execute(final ProcessDefinitionSuspensionStateConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final AbstractSetProcessDefinitionStateCmd cmd = this.getCommand(configuration);
        cmd.disableLogUserOperation();
        cmd.execute(commandContext);
    }

    protected abstract AbstractSetProcessDefinitionStateCmd getCommand(final ProcessDefinitionSuspensionStateConfiguration p0);

    @Override
    public ProcessDefinitionSuspensionStateConfiguration newConfiguration(final String canonicalString) {
        final JsonObject jsonObject = JsonUtil.asObject(canonicalString);
        return ProcessDefinitionSuspensionStateConfiguration.fromJson(jsonObject);
    }

    @Override
    public void onDelete(final ProcessDefinitionSuspensionStateConfiguration configuration, final JobEntity jobEntity) {
    }

    public static class ProcessDefinitionSuspensionStateConfiguration implements JobHandlerConfiguration {
        protected String processDefinitionKey;
        protected String processDefinitionId;
        protected boolean includeProcessInstances;
        protected String tenantId;
        protected boolean isTenantIdSet;
        protected String by;

        @Override
        public String toCanonicalString() {
            final JsonObject json = JsonUtil.createObject();
            JsonUtil.addField(json, "by", this.by);
            JsonUtil.addField(json, "processDefinitionKey", this.processDefinitionKey);
            JsonUtil.addField(json, "includeProcessInstances", this.includeProcessInstances);
            JsonUtil.addField(json, "processDefinitionId", this.processDefinitionId);
            if (this.isTenantIdSet) {
                if (this.tenantId != null) {
                    JsonUtil.addField(json, "processDefinitionTenantId", this.tenantId);
                } else {
                    JsonUtil.addNullField(json, "processDefinitionTenantId");
                }
            }
            return json.toString();
        }

        public UpdateProcessDefinitionSuspensionStateBuilderImpl createBuilder() {
            final UpdateProcessDefinitionSuspensionStateBuilderImpl builder = new UpdateProcessDefinitionSuspensionStateBuilderImpl();
            if (this.by.equals("processDefinitionId")) {
                builder.byProcessDefinitionId(this.processDefinitionId);
            } else {
                if (!this.by.equals("processDefinitionKey")) {
                    throw new ProcessEngineException("Unexpected job handler configuration for property 'by': " + this.by);
                }
                builder.byProcessDefinitionKey(this.processDefinitionKey);
                if (this.isTenantIdSet) {
                    if (this.tenantId != null) {
                        builder.processDefinitionTenantId(this.tenantId);
                    } else {
                        builder.processDefinitionWithoutTenantId();
                    }
                }
            }
            builder.includeProcessInstances(this.includeProcessInstances);
            return builder;
        }

        public static ProcessDefinitionSuspensionStateConfiguration fromJson(final JsonObject jsonObject) {
            final ProcessDefinitionSuspensionStateConfiguration config = new ProcessDefinitionSuspensionStateConfiguration();
            config.by = JsonUtil.getString(jsonObject, "by");
            if (jsonObject.has("processDefinitionId")) {
                config.processDefinitionId = JsonUtil.getString(jsonObject, "processDefinitionId");
            }
            if (jsonObject.has("processDefinitionKey")) {
                config.processDefinitionKey = JsonUtil.getString(jsonObject, "processDefinitionKey");
            }
            if (jsonObject.has("processDefinitionTenantId")) {
                config.isTenantIdSet = true;
                if (!JsonUtil.isNull(jsonObject, "processDefinitionTenantId")) {
                    config.tenantId = JsonUtil.getString(jsonObject, "processDefinitionTenantId");
                }
            }
            if (jsonObject.has("includeProcessInstances")) {
                config.includeProcessInstances = JsonUtil.getBoolean(jsonObject, "includeProcessInstances");
            }
            return config;
        }

        public static ProcessDefinitionSuspensionStateConfiguration byProcessDefinitionId(final String processDefinitionId, final boolean includeProcessInstances) {
            final ProcessDefinitionSuspensionStateConfiguration configuration = new ProcessDefinitionSuspensionStateConfiguration();
            configuration.by = "processDefinitionId";
            configuration.processDefinitionId = processDefinitionId;
            configuration.includeProcessInstances = includeProcessInstances;
            return configuration;
        }

        public static ProcessDefinitionSuspensionStateConfiguration byProcessDefinitionKey(final String processDefinitionKey, final boolean includeProcessInstances) {
            final ProcessDefinitionSuspensionStateConfiguration configuration = new ProcessDefinitionSuspensionStateConfiguration();
            configuration.by = "processDefinitionKey";
            configuration.processDefinitionKey = processDefinitionKey;
            configuration.includeProcessInstances = includeProcessInstances;
            return configuration;
        }

        public static ProcessDefinitionSuspensionStateConfiguration byProcessDefinitionKeyAndTenantId(final String processDefinitionKey, final String tenantId, final boolean includeProcessInstances) {
            final ProcessDefinitionSuspensionStateConfiguration configuration = byProcessDefinitionKey(processDefinitionKey, includeProcessInstances);
            configuration.isTenantIdSet = true;
            configuration.tenantId = tenantId;
            return configuration;
        }
    }
}
