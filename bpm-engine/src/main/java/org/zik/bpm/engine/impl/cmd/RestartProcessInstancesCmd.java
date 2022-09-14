// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.history.HistoricActivityInstanceQuery;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.history.HistoricVariableInstance;
import org.zik.bpm.engine.history.HistoricActivityInstance;
import org.zik.bpm.engine.history.HistoricVariableUpdate;
import org.zik.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.impl.HistoricDetailQueryImpl;
import org.zik.bpm.engine.HistoryService;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.ProcessInstanceModificationBuilderImpl;
import org.zik.bpm.engine.impl.ProcessInstantiationBuilderImpl;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import java.util.List;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import java.util.Map;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.RestartProcessInstanceBuilderImpl;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;

public class RestartProcessInstancesCmd extends AbstractRestartProcessInstanceCmd<Void>
{
    private static final CommandLogger LOG;
    
    public RestartProcessInstancesCmd(final CommandExecutor commandExecutor, final RestartProcessInstanceBuilderImpl builder) {
        super(commandExecutor, builder);
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final List<AbstractProcessInstanceModificationCommand> instructions = this.builder.getInstructions();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Restart instructions cannot be empty", "instructions", instructions);
        final Collection<String> processInstanceIds = this.collectProcessInstanceIds();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Process instance ids cannot be empty", "Process instance ids", processInstanceIds);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "Process instance ids cannot be null", "Process instance ids", processInstanceIds);
        final ProcessDefinitionEntity processDefinition = this.getProcessDefinition(commandContext, this.builder.getProcessDefinitionId());
        EnsureUtil.ensureNotNull("Process definition cannot be found", "processDefinition", processDefinition);
        this.checkAuthorization(commandContext, processDefinition);
        this.writeUserOperationLog(commandContext, processDefinition, processInstanceIds.size(), false);
        final String processDefinitionId = this.builder.getProcessDefinitionId();
        final Collection<String> collection;
        final Iterator<String> iterator;
        String processInstanceId;
        HistoricProcessInstance historicProcessInstance;
        final String s;
        ProcessInstantiationBuilderImpl instantiationBuilder;
        final ProcessDefinition processDefinition2;
        ProcessInstanceModificationBuilderImpl modificationBuilder;
        final List<AbstractProcessInstanceModificationCommand> modificationOperations;
        VariableMap variables;
        final Runnable runnable = () -> {
            collection.iterator();
            while (iterator.hasNext()) {
                processInstanceId = iterator.next();
                historicProcessInstance = this.getHistoricProcessInstance(commandContext, processInstanceId);
                EnsureUtil.ensureNotNull(BadUserRequestException.class, "Historic process instance cannot be found", "historicProcessInstanceId", historicProcessInstance);
                this.ensureHistoricProcessInstanceNotActive(historicProcessInstance);
                this.ensureSameProcessDefinition(historicProcessInstance, s);
                instantiationBuilder = this.getProcessInstantiationBuilder(this.commandExecutor, s);
                this.applyProperties(instantiationBuilder, processDefinition2, historicProcessInstance);
                modificationBuilder = instantiationBuilder.getModificationBuilder();
                modificationBuilder.setModificationOperations(modificationOperations);
                variables = this.collectVariables(commandContext, historicProcessInstance);
                instantiationBuilder.setVariables((Map<String, Object>)variables);
                instantiationBuilder.execute(this.builder.isSkipCustomListeners(), this.builder.isSkipIoMappings());
            }
            return;
        };
        ProcessApplicationContextUtil.doContextSwitch(runnable, processDefinition);
        return null;
    }
    
    protected void checkAuthorization(final CommandContext commandContext, final ProcessDefinition processDefinition) {
        commandContext.getAuthorizationManager().checkAuthorization(Permissions.READ_HISTORY, Resources.PROCESS_DEFINITION, processDefinition.getKey());
    }
    
    protected HistoricProcessInstance getHistoricProcessInstance(final CommandContext commandContext, final String processInstanceId) {
        final HistoryService historyService = commandContext.getProcessEngineConfiguration().getHistoryService();
        return ((Query<T, HistoricProcessInstance>)historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId)).singleResult();
    }
    
    protected void ensureSameProcessDefinition(final HistoricProcessInstance instance, final String processDefinitionId) {
        if (!processDefinitionId.equals(instance.getProcessDefinitionId())) {
            throw RestartProcessInstancesCmd.LOG.processDefinitionOfHistoricInstanceDoesNotMatchTheGivenOne(instance, processDefinitionId);
        }
    }
    
    protected void ensureHistoricProcessInstanceNotActive(final HistoricProcessInstance instance) {
        if (instance.getEndTime() == null) {
            throw RestartProcessInstancesCmd.LOG.historicProcessInstanceActive(instance);
        }
    }
    
    protected ProcessInstantiationBuilderImpl getProcessInstantiationBuilder(final CommandExecutor commandExecutor, final String processDefinitionId) {
        return (ProcessInstantiationBuilderImpl)ProcessInstantiationBuilderImpl.createProcessInstanceById(commandExecutor, processDefinitionId);
    }
    
    protected void applyProperties(final ProcessInstantiationBuilderImpl instantiationBuilder, final ProcessDefinition processDefinition, final HistoricProcessInstance processInstance) {
        final String tenantId = processInstance.getTenantId();
        if (processDefinition.getTenantId() == null && tenantId != null) {
            instantiationBuilder.tenantId(tenantId);
        }
        if (!this.builder.isWithoutBusinessKey()) {
            instantiationBuilder.businessKey(processInstance.getBusinessKey());
        }
    }
    
    protected VariableMap collectVariables(final CommandContext commandContext, final HistoricProcessInstance processInstance) {
        VariableMap variables = null;
        if (this.builder.isInitialVariables()) {
            variables = this.collectInitialVariables(commandContext, processInstance);
        }
        else {
            variables = this.collectLastVariables(commandContext, processInstance);
        }
        return variables;
    }
    
    protected VariableMap collectInitialVariables(final CommandContext commandContext, final HistoricProcessInstance processInstance) {
        final HistoryService historyService = commandContext.getProcessEngineConfiguration().getHistoryService();
        List<HistoricDetail> historicDetails = ((Query<T, HistoricDetail>)historyService.createHistoricDetailQuery().variableUpdates().executionId(processInstance.getId()).initial()).list();
        if (historicDetails.size() == 0) {
            final HistoricActivityInstance startActivityInstance = this.resolveStartActivityInstance(processInstance);
            if (startActivityInstance != null) {
                final HistoricDetailQueryImpl queryWithStartActivities = (HistoricDetailQueryImpl)historyService.createHistoricDetailQuery().variableUpdates().activityInstanceId(startActivityInstance.getId()).executionId(processInstance.getId());
                historicDetails = ((AbstractQuery<T, HistoricDetail>)queryWithStartActivities.sequenceCounter(1L)).list();
            }
        }
        final VariableMap variables = (VariableMap)new VariableMapImpl();
        for (final HistoricDetail detail : historicDetails) {
            final HistoricVariableUpdate variableUpdate = (HistoricVariableUpdate)detail;
            variables.putValueTyped(variableUpdate.getVariableName(), variableUpdate.getTypedValue());
        }
        return variables;
    }
    
    protected VariableMap collectLastVariables(final CommandContext commandContext, final HistoricProcessInstance processInstance) {
        final HistoryService historyService = commandContext.getProcessEngineConfiguration().getHistoryService();
        final List<HistoricVariableInstance> historicVariables = ((Query<T, HistoricVariableInstance>)historyService.createHistoricVariableInstanceQuery().executionIdIn(processInstance.getId())).list();
        final VariableMap variables = (VariableMap)new VariableMapImpl();
        for (final HistoricVariableInstance variable : historicVariables) {
            variables.putValueTyped(variable.getName(), variable.getTypedValue());
        }
        return variables;
    }
    
    protected HistoricActivityInstance resolveStartActivityInstance(final HistoricProcessInstance processInstance) {
        final HistoryService historyService = Context.getProcessEngineConfiguration().getHistoryService();
        final String processInstanceId = processInstance.getId();
        final String startActivityId = processInstance.getStartActivityId();
        EnsureUtil.ensureNotNull("startActivityId", (Object)startActivityId);
        final List<HistoricActivityInstance> historicActivityInstances = ((Query<T, HistoricActivityInstance>)((Query<HistoricActivityInstanceQuery, U>)historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityId(startActivityId).orderPartiallyByOccurrence()).asc()).list();
        EnsureUtil.ensureNotEmpty("historicActivityInstances", historicActivityInstances);
        final HistoricActivityInstance startActivityInstance = historicActivityInstances.get(0);
        return startActivityInstance;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
