// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.management.UpdateJobDefinitionSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.util.JsonUtil;
import org.zik.bpm.engine.impl.cmd.AbstractSetJobDefinitionStateCmd;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public abstract class TimerChangeJobDefinitionSuspensionStateJobHandler implements JobHandler<TimerChangeJobDefinitionSuspensionStateJobHandler.JobDefinitionSuspensionStateConfiguration>
{
    protected static final String JOB_HANDLER_CFG_BY = "by";
    protected static final String JOB_HANDLER_CFG_JOB_DEFINITION_ID = "jobDefinitionId";
    protected static final String JOB_HANDLER_CFG_PROCESS_DEFINITION_ID = "processDefinitionId";
    protected static final String JOB_HANDLER_CFG_PROCESS_DEFINITION_KEY = "processDefinitionKey";
    protected static final String JOB_HANDLER_CFG_PROCESS_DEFINITION_TENANT_ID = "processDefinitionTenantId";
    protected static final String JOB_HANDLER_CFG_INCLUDE_JOBS = "includeJobs";
    
    @Override
    public void execute(final JobDefinitionSuspensionStateConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final AbstractSetJobDefinitionStateCmd cmd = this.getCommand(configuration);
        cmd.disableLogUserOperation();
        cmd.execute(commandContext);
    }
    
    protected abstract AbstractSetJobDefinitionStateCmd getCommand(final JobDefinitionSuspensionStateConfiguration p0);
    
    @Override
    public JobDefinitionSuspensionStateConfiguration newConfiguration(final String canonicalString) {
        final JsonObject jsonObject = JsonUtil.asObject(canonicalString);
        return JobDefinitionSuspensionStateConfiguration.fromJson(jsonObject);
    }
    
    @Override
    public void onDelete(final JobDefinitionSuspensionStateConfiguration configuration, final JobEntity jobEntity) {
    }
    
    public static class JobDefinitionSuspensionStateConfiguration implements JobHandlerConfiguration
    {
        protected String jobDefinitionId;
        protected String processDefinitionKey;
        protected String processDefinitionId;
        protected boolean includeJobs;
        protected String tenantId;
        protected boolean isTenantIdSet;
        protected String by;
        
        @Override
        public String toCanonicalString() {
            final JsonObject json = JsonUtil.createObject();
            JsonUtil.addField(json, "by", this.by);
            JsonUtil.addField(json, "jobDefinitionId", this.jobDefinitionId);
            JsonUtil.addField(json, "processDefinitionKey", this.processDefinitionKey);
            JsonUtil.addField(json, "includeJobs", this.includeJobs);
            JsonUtil.addField(json, "processDefinitionId", this.processDefinitionId);
            if (this.isTenantIdSet) {
                if (this.tenantId != null) {
                    JsonUtil.addField(json, "processDefinitionTenantId", this.tenantId);
                }
                else {
                    JsonUtil.addNullField(json, "processDefinitionTenantId");
                }
            }
            return json.toString();
        }
        
        public UpdateJobDefinitionSuspensionStateBuilderImpl createBuilder() {
            final UpdateJobDefinitionSuspensionStateBuilderImpl builder = new UpdateJobDefinitionSuspensionStateBuilderImpl();
            if ("processDefinitionId".equals(this.by)) {
                builder.byProcessDefinitionId(this.processDefinitionId);
            }
            else if ("jobDefinitionId".equals(this.by)) {
                builder.byJobDefinitionId(this.jobDefinitionId);
            }
            else {
                if (!"processDefinitionKey".equals(this.by)) {
                    throw new ProcessEngineException("Unexpected job handler configuration for property 'by': " + this.by);
                }
                builder.byProcessDefinitionKey(this.processDefinitionKey);
                if (this.isTenantIdSet) {
                    if (this.tenantId != null) {
                        builder.processDefinitionTenantId(this.tenantId);
                    }
                    else {
                        builder.processDefinitionWithoutTenantId();
                    }
                }
            }
            builder.includeJobs(this.includeJobs);
            return builder;
        }
        
        public static JobDefinitionSuspensionStateConfiguration fromJson(final JsonObject jsonObject) {
            final JobDefinitionSuspensionStateConfiguration config = new JobDefinitionSuspensionStateConfiguration();
            config.by = JsonUtil.getString(jsonObject, "by");
            if (jsonObject.has("jobDefinitionId")) {
                config.jobDefinitionId = JsonUtil.getString(jsonObject, "jobDefinitionId");
            }
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
            if (jsonObject.has("includeJobs")) {
                config.includeJobs = JsonUtil.getBoolean(jsonObject, "includeJobs");
            }
            return config;
        }
        
        public static JobDefinitionSuspensionStateConfiguration byJobDefinitionId(final String jobDefinitionId, final boolean includeJobs) {
            final JobDefinitionSuspensionStateConfiguration configuration = new JobDefinitionSuspensionStateConfiguration();
            configuration.by = "jobDefinitionId";
            configuration.jobDefinitionId = jobDefinitionId;
            configuration.includeJobs = includeJobs;
            return configuration;
        }
        
        public static JobDefinitionSuspensionStateConfiguration byProcessDefinitionId(final String processDefinitionId, final boolean includeJobs) {
            final JobDefinitionSuspensionStateConfiguration configuration = new JobDefinitionSuspensionStateConfiguration();
            configuration.by = "processDefinitionId";
            configuration.processDefinitionId = processDefinitionId;
            configuration.includeJobs = includeJobs;
            return configuration;
        }
        
        public static JobDefinitionSuspensionStateConfiguration byProcessDefinitionKey(final String processDefinitionKey, final boolean includeJobs) {
            final JobDefinitionSuspensionStateConfiguration configuration = new JobDefinitionSuspensionStateConfiguration();
            configuration.by = "processDefinitionKey";
            configuration.processDefinitionKey = processDefinitionKey;
            configuration.includeJobs = includeJobs;
            return configuration;
        }
        
        public static JobDefinitionSuspensionStateConfiguration ByProcessDefinitionKeyAndTenantId(final String processDefinitionKey, final String tenantId, final boolean includeProcessInstances) {
            final JobDefinitionSuspensionStateConfiguration configuration = byProcessDefinitionKey(processDefinitionKey, includeProcessInstances);
            configuration.isTenantIdSet = true;
            configuration.tenantId = tenantId;
            return configuration;
        }
    }
}
