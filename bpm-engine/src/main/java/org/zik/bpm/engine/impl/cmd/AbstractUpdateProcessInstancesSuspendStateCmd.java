// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.UpdateProcessInstancesSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractUpdateProcessInstancesSuspendStateCmd<T> implements Command<T>
{
    protected UpdateProcessInstancesSuspensionStateBuilderImpl builder;
    protected CommandExecutor commandExecutor;
    protected boolean suspending;
    
    public AbstractUpdateProcessInstancesSuspendStateCmd(final CommandExecutor commandExecutor, final UpdateProcessInstancesSuspensionStateBuilderImpl builder, final boolean suspending) {
        this.commandExecutor = commandExecutor;
        this.builder = builder;
        this.suspending = suspending;
    }
    
    protected BatchElementConfiguration collectProcessInstanceIds(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        final List<String> processInstanceIds = this.builder.getProcessInstanceIds();
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "Cannot be null.", "Process Instance ids", processInstanceIds);
        if (!CollectionUtil.isEmpty(processInstanceIds)) {
            final ProcessInstanceQueryImpl query = new ProcessInstanceQueryImpl();
            query.processInstanceIds(new HashSet<String>(processInstanceIds));
            final BatchElementConfiguration batchElementConfiguration = elementConfiguration;
            final ProcessInstanceQueryImpl obj = query;
            Objects.requireNonNull(obj);
            batchElementConfiguration.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings), processInstanceIds);
        }
        final ProcessInstanceQueryImpl processInstanceQuery = (ProcessInstanceQueryImpl)this.builder.getProcessInstanceQuery();
        if (processInstanceQuery != null) {
            elementConfiguration.addDeploymentMappings(processInstanceQuery.listDeploymentIdMappings());
        }
        final HistoricProcessInstanceQueryImpl historicProcessInstanceQuery = (HistoricProcessInstanceQueryImpl)this.builder.getHistoricProcessInstanceQuery();
        if (historicProcessInstanceQuery != null) {
            elementConfiguration.addDeploymentMappings(historicProcessInstanceQuery.listDeploymentIdMappings());
        }
        return elementConfiguration;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances, final boolean async) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, async));
        String operationType;
        if (this.suspending) {
            operationType = "SuspendJob";
        }
        else {
            operationType = "ActivateJob";
        }
        commandContext.getOperationLogManager().logProcessInstanceOperation(operationType, null, null, null, propertyChanges);
    }
    
    protected void writeUserOperationLogAsync(final CommandContext commandContext, final int numInstances) {
        this.writeUserOperationLog(commandContext, numInstances, true);
    }
}
