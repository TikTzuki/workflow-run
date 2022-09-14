// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricProcessInstancesCmd implements Command<Void>, Serializable
{
    protected final List<String> processInstanceIds;
    protected final boolean failIfNotExists;
    
    public DeleteHistoricProcessInstancesCmd(final List<String> processInstanceIds, final boolean failIfNotExists) {
        this.processInstanceIds = processInstanceIds;
        this.failIfNotExists = failIfNotExists;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "processInstanceIds", this.processInstanceIds);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "processInstanceId is null", "processInstanceIds", this.processInstanceIds);
        final List<HistoricProcessInstance> instances = commandContext.runWithoutAuthorization((Callable<List<HistoricProcessInstance>>)new Callable<List<HistoricProcessInstance>>() {
            @Override
            public List<HistoricProcessInstance> call() throws Exception {
                return ((Query<T, HistoricProcessInstance>)new HistoricProcessInstanceQueryImpl().processInstanceIds(new HashSet<String>(DeleteHistoricProcessInstancesCmd.this.processInstanceIds))).list();
            }
        });
        if (this.failIfNotExists) {
            if (this.processInstanceIds.size() == 1) {
                EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "No historic process instance found with id: " + this.processInstanceIds.get(0), "historicProcessInstanceIds", instances);
            }
            else {
                EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "No historic process instances found", "historicProcessInstanceIds", instances);
            }
        }
        final List<String> existingIds = new ArrayList<String>();
        for (final HistoricProcessInstance historicProcessInstance : instances) {
            existingIds.add(historicProcessInstance.getId());
            for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                checker.checkDeleteHistoricProcessInstance(historicProcessInstance);
            }
            EnsureUtil.ensureNotNull(BadUserRequestException.class, "Process instance is still running, cannot delete historic process instance: " + historicProcessInstance, "instance.getEndTime()", historicProcessInstance.getEndTime());
        }
        if (this.failIfNotExists) {
            final ArrayList<String> nonExistingIds = new ArrayList<String>(this.processInstanceIds);
            nonExistingIds.removeAll(existingIds);
            if (nonExistingIds.size() != 0) {
                throw new BadUserRequestException("No historic process instance found with id: " + nonExistingIds);
            }
        }
        if (existingIds.size() > 0) {
            commandContext.getHistoricProcessInstanceManager().deleteHistoricProcessInstanceByIds(existingIds);
        }
        this.writeUserOperationLog(commandContext, existingIds.size());
        return null;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, false));
        commandContext.getOperationLogManager().logProcessInstanceOperation("DeleteHistory", null, null, null, propertyChanges);
    }
}
