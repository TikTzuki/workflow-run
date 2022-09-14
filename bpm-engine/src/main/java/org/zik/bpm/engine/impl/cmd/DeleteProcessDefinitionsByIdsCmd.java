// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.persistence.entity.UserOperationLogManager;
import org.zik.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.zik.bpm.engine.impl.persistence.deploy.Deployer;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import java.util.HashMap;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionManager;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteProcessDefinitionsByIdsCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final Set<String> processDefinitionIds;
    protected boolean cascadeToHistory;
    protected boolean cascadeToInstances;
    protected boolean skipCustomListeners;
    protected boolean writeUserOperationLog;
    protected boolean skipIoMappings;
    
    public DeleteProcessDefinitionsByIdsCmd(final List<String> processDefinitionIds, final boolean cascade, final boolean skipCustomListeners, final boolean skipIoMappings) {
        this(processDefinitionIds, cascade, cascade, skipCustomListeners, skipIoMappings, true);
    }
    
    public DeleteProcessDefinitionsByIdsCmd(final List<String> processDefinitionIds, final boolean cascadeToHistory, final boolean cascadeToInstances, final boolean skipCustomListeners, final boolean writeUserOperationLog) {
        this(processDefinitionIds, cascadeToHistory, cascadeToInstances, skipCustomListeners, false, writeUserOperationLog);
    }
    
    public DeleteProcessDefinitionsByIdsCmd(final List<String> processDefinitionIds, final boolean cascadeToHistory, final boolean cascadeToInstances, final boolean skipCustomListeners, final boolean skipIoMappings, final boolean writeUserOperationLog) {
        this.processDefinitionIds = new HashSet<String>(processDefinitionIds);
        this.cascadeToHistory = cascadeToHistory;
        this.cascadeToInstances = cascadeToInstances;
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMappings = skipIoMappings;
        this.writeUserOperationLog = writeUserOperationLog;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("processDefinitionIds", this.processDefinitionIds);
        List<ProcessDefinition> processDefinitions;
        if (this.processDefinitionIds.size() == 1) {
            final ProcessDefinition processDefinition = this.getSingleProcessDefinition(commandContext);
            processDefinitions = new ArrayList<ProcessDefinition>();
            processDefinitions.add(processDefinition);
        }
        else {
            final ProcessDefinitionManager processDefinitionManager = commandContext.getProcessDefinitionManager();
            processDefinitions = processDefinitionManager.findDefinitionsByIds(this.processDefinitionIds);
            EnsureUtil.ensureNotEmpty(NotFoundException.class, "No process definition found", "processDefinitions", processDefinitions);
        }
        final Set<ProcessDefinitionGroup> groups = this.groupByKeyAndTenant(processDefinitions);
        for (final ProcessDefinitionGroup group : groups) {
            this.checkAuthorization(group);
        }
        for (final ProcessDefinitionGroup group : groups) {
            this.deleteProcessDefinitions(group);
        }
        return null;
    }
    
    protected ProcessDefinition getSingleProcessDefinition(final CommandContext commandContext) {
        final String processDefinitionId = this.processDefinitionIds.iterator().next();
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        final ProcessDefinition processDefinition = commandContext.getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "No process definition found with id '" + processDefinitionId + "'", "processDefinition", processDefinition);
        return processDefinition;
    }
    
    protected Set<ProcessDefinitionGroup> groupByKeyAndTenant(final List<ProcessDefinition> processDefinitions) {
        final Set<ProcessDefinitionGroup> groups = new HashSet<ProcessDefinitionGroup>();
        final Map<ProcessDefinitionGroup, List<ProcessDefinitionEntity>> map = new HashMap<ProcessDefinitionGroup, List<ProcessDefinitionEntity>>();
        for (final ProcessDefinition current : processDefinitions) {
            final ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)current;
            final ProcessDefinitionGroup group = new ProcessDefinitionGroup();
            group.key = processDefinition.getKey();
            group.tenant = processDefinition.getTenantId();
            List<ProcessDefinitionEntity> definitions = group.processDefinitions;
            if (map.containsKey(group)) {
                definitions = map.get(group);
            }
            else {
                groups.add(group);
                map.put(group, definitions);
            }
            definitions.add(processDefinition);
        }
        return groups;
    }
    
    protected ProcessDefinitionEntity findNewLatestProcessDefinition(final ProcessDefinitionGroup group) {
        ProcessDefinitionEntity newLatestProcessDefinition = null;
        final List<ProcessDefinitionEntity> processDefinitions = group.processDefinitions;
        final ProcessDefinitionEntity firstProcessDefinition = processDefinitions.get(0);
        if (this.isLatestProcessDefinition(firstProcessDefinition)) {
            for (final ProcessDefinitionEntity processDefinition : processDefinitions) {
                final String previousProcessDefinitionId = processDefinition.getPreviousProcessDefinitionId();
                if (previousProcessDefinitionId != null && !this.processDefinitionIds.contains(previousProcessDefinitionId)) {
                    final CommandContext commandContext = Context.getCommandContext();
                    final ProcessDefinitionManager processDefinitionManager = commandContext.getProcessDefinitionManager();
                    newLatestProcessDefinition = processDefinitionManager.findLatestDefinitionById(previousProcessDefinitionId);
                    break;
                }
            }
        }
        return newLatestProcessDefinition;
    }
    
    protected boolean isLatestProcessDefinition(final ProcessDefinitionEntity processDefinition) {
        final ProcessDefinitionManager processDefinitionManager = Context.getCommandContext().getProcessDefinitionManager();
        final String key = processDefinition.getKey();
        final String tenantId = processDefinition.getTenantId();
        final ProcessDefinitionEntity latestProcessDefinition = processDefinitionManager.findLatestDefinitionByKeyAndTenantId(key, tenantId);
        return processDefinition.getId().equals(latestProcessDefinition.getId());
    }
    
    protected void checkAuthorization(final ProcessDefinitionGroup group) {
        final List<CommandChecker> commandCheckers = Context.getCommandContext().getProcessEngineConfiguration().getCommandCheckers();
        final List<ProcessDefinitionEntity> processDefinitions = group.processDefinitions;
        for (final ProcessDefinitionEntity processDefinition : processDefinitions) {
            for (final CommandChecker commandChecker : commandCheckers) {
                commandChecker.checkDeleteProcessDefinitionById(processDefinition.getId());
            }
        }
    }
    
    protected void deleteProcessDefinitions(final ProcessDefinitionGroup group) {
        ProcessDefinitionEntity newLatestProcessDefinition = this.findNewLatestProcessDefinition(group);
        final CommandContext commandContext = Context.getCommandContext();
        final UserOperationLogManager userOperationLogManager = commandContext.getOperationLogManager();
        final ProcessDefinitionManager definitionManager = commandContext.getProcessDefinitionManager();
        final List<ProcessDefinitionEntity> processDefinitions = group.processDefinitions;
        for (final ProcessDefinitionEntity processDefinition : processDefinitions) {
            final String processDefinitionId = processDefinition.getId();
            if (this.writeUserOperationLog) {
                userOperationLogManager.logProcessDefinitionOperation("Delete", processDefinitionId, processDefinition.getKey(), new PropertyChange("cascade", false, this.cascadeToHistory));
            }
            definitionManager.deleteProcessDefinition(processDefinition, processDefinitionId, this.cascadeToHistory, this.cascadeToInstances, this.skipCustomListeners, this.skipIoMappings);
        }
        if (newLatestProcessDefinition != null) {
            final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
            final DeploymentCache deploymentCache = configuration.getDeploymentCache();
            newLatestProcessDefinition = deploymentCache.resolveProcessDefinition(newLatestProcessDefinition);
            final List<Deployer> deployers = configuration.getDeployers();
            for (final Deployer deployer : deployers) {
                if (deployer instanceof BpmnDeployer) {
                    ((BpmnDeployer)deployer).addEventSubscriptions(newLatestProcessDefinition);
                }
            }
        }
    }
    
    private static class ProcessDefinitionGroup
    {
        String key;
        String tenant;
        List<ProcessDefinitionEntity> processDefinitions;
        
        private ProcessDefinitionGroup() {
            this.processDefinitions = new ArrayList<ProcessDefinitionEntity>();
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
            result = 31 * result + ((this.tenant == null) ? 0 : this.tenant.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final ProcessDefinitionGroup other = (ProcessDefinitionGroup)obj;
            if (this.key == null) {
                if (other.key != null) {
                    return false;
                }
            }
            else if (!this.key.equals(other.key)) {
                return false;
            }
            if (this.tenant == null) {
                if (other.tenant != null) {
                    return false;
                }
            }
            else if (!this.tenant.equals(other.tenant)) {
                return false;
            }
            return true;
        }
    }
}
