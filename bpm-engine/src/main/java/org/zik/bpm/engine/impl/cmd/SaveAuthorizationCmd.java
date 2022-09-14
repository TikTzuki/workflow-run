// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Objects;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import java.util.function.Supplier;
import org.zik.bpm.engine.authorization.Resources;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.zik.bpm.engine.authorization.Authorization;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SaveAuthorizationCmd implements Command<Authorization>
{
    protected AuthorizationEntity authorization;
    
    public SaveAuthorizationCmd(final Authorization authorization) {
        this.authorization = (AuthorizationEntity)authorization;
        this.validate();
    }
    
    protected void validate() {
        EnsureUtil.ensureOnlyOneNotNull("Authorization must either have a 'userId' or a 'groupId'.", this.authorization.getUserId(), this.authorization.getGroupId());
        EnsureUtil.ensureNotNull("Authorization 'resourceType' cannot be null.", "authorization.getResource()", this.authorization.getResource());
    }
    
    @Override
    public Authorization execute(final CommandContext commandContext) {
        final AuthorizationManager authorizationManager = commandContext.getAuthorizationManager();
        authorizationManager.validateResourceCompatibility(this.authorization);
        this.provideRemovalTime(commandContext);
        String operationType = null;
        AuthorizationEntity previousValues = null;
        if (this.authorization.getId() == null) {
            authorizationManager.insert(this.authorization);
            operationType = "Create";
        }
        else {
            previousValues = commandContext.getDbEntityManager().selectById(AuthorizationEntity.class, this.authorization.getId());
            authorizationManager.update(this.authorization);
            operationType = "Update";
        }
        commandContext.getOperationLogManager().logAuthorizationOperation(operationType, this.authorization, previousValues);
        return this.authorization;
    }
    
    protected void provideRemovalTime(final CommandContext commandContext) {
        for (final Map.Entry<Resources, Supplier<HistoryEvent>> resourceEntry : this.getHistoricInstanceResources(commandContext)) {
            final Resources resource = resourceEntry.getKey();
            if (this.isResourceEqualTo(resource)) {
                final Supplier<HistoryEvent> historyEventSupplier = resourceEntry.getValue();
                final HistoryEvent historyEvent = historyEventSupplier.get();
                this.provideRemovalTime(historyEvent);
                break;
            }
        }
    }
    
    protected Set<Map.Entry<Resources, Supplier<HistoryEvent>>> getHistoricInstanceResources(final CommandContext commandContext) {
        final Map<Resources, Supplier<HistoryEvent>> resources = new HashMap<Resources, Supplier<HistoryEvent>>();
        resources.put(Resources.HISTORIC_PROCESS_INSTANCE, () -> this.getHistoricProcessInstance(commandContext));
        resources.put(Resources.HISTORIC_TASK, () -> this.getHistoricTaskInstance(commandContext));
        return resources.entrySet();
    }
    
    protected void provideRemovalTime(final HistoryEvent historicInstance) {
        if (historicInstance != null) {
            final String rootProcessInstanceId = historicInstance.getRootProcessInstanceId();
            this.authorization.setRootProcessInstanceId(rootProcessInstanceId);
            final Date removalTime = historicInstance.getRemovalTime();
            this.authorization.setRemovalTime(removalTime);
        }
        else {
            this.authorization.setRootProcessInstanceId(null);
            this.authorization.setRemovalTime(null);
        }
    }
    
    protected HistoryEvent getHistoricProcessInstance(final CommandContext commandContext) {
        final String historicProcessInstanceId = this.authorization.getResourceId();
        if (this.isNullOrAny(historicProcessInstanceId)) {
            return null;
        }
        return commandContext.getHistoricProcessInstanceManager().findHistoricProcessInstance(historicProcessInstanceId);
    }
    
    protected HistoryEvent getHistoricTaskInstance(final CommandContext commandContext) {
        final String historicTaskInstanceId = this.authorization.getResourceId();
        if (this.isNullOrAny(historicTaskInstanceId)) {
            return null;
        }
        return commandContext.getHistoricTaskInstanceManager().findHistoricTaskInstanceById(historicTaskInstanceId);
    }
    
    protected boolean isNullOrAny(final String resourceId) {
        return resourceId == null || this.isAny(resourceId);
    }
    
    protected boolean isAny(final String resourceId) {
        return Objects.equals("*", resourceId);
    }
    
    protected boolean isResourceEqualTo(final Resources resource) {
        return Objects.equals(resource.resourceType(), this.authorization.getResource());
    }
}
