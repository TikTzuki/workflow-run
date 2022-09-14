// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.UpdateProcessInstancesSuspendStateBatchCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.UpdateProcessInstancesSuspendStateCmd;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;
import org.zik.bpm.engine.runtime.UpdateProcessInstancesSuspensionStateBuilder;

public class UpdateProcessInstancesSuspensionStateBuilderImpl implements UpdateProcessInstancesSuspensionStateBuilder
{
    protected List<String> processInstanceIds;
    protected ProcessInstanceQuery processInstanceQuery;
    protected HistoricProcessInstanceQuery historicProcessInstanceQuery;
    protected CommandExecutor commandExecutor;
    protected String processDefinitionId;
    
    public UpdateProcessInstancesSuspensionStateBuilderImpl(final CommandExecutor commandExecutor) {
        this.processInstanceIds = new ArrayList<String>();
        this.commandExecutor = commandExecutor;
    }
    
    public UpdateProcessInstancesSuspensionStateBuilderImpl(final List<String> processInstanceIds) {
        this.processInstanceIds = processInstanceIds;
    }
    
    @Override
    public UpdateProcessInstancesSuspensionStateBuilder byProcessInstanceIds(final List<String> processInstanceIds) {
        this.processInstanceIds.addAll(processInstanceIds);
        return this;
    }
    
    @Override
    public UpdateProcessInstancesSuspensionStateBuilder byProcessInstanceIds(final String... processInstanceIds) {
        this.processInstanceIds.addAll(Arrays.asList(processInstanceIds));
        return this;
    }
    
    @Override
    public UpdateProcessInstancesSuspensionStateBuilder byProcessInstanceQuery(final ProcessInstanceQuery processInstanceQuery) {
        this.processInstanceQuery = processInstanceQuery;
        return this;
    }
    
    @Override
    public UpdateProcessInstancesSuspensionStateBuilder byHistoricProcessInstanceQuery(final HistoricProcessInstanceQuery historicProcessInstanceQuery) {
        this.historicProcessInstanceQuery = historicProcessInstanceQuery;
        return this;
    }
    
    @Override
    public void suspend() {
        this.commandExecutor.execute((Command<Object>)new UpdateProcessInstancesSuspendStateCmd(this.commandExecutor, this, true));
    }
    
    @Override
    public void activate() {
        this.commandExecutor.execute((Command<Object>)new UpdateProcessInstancesSuspendStateCmd(this.commandExecutor, this, false));
    }
    
    @Override
    public Batch suspendAsync() {
        return this.commandExecutor.execute((Command<Batch>)new UpdateProcessInstancesSuspendStateBatchCmd(this.commandExecutor, this, true));
    }
    
    @Override
    public Batch activateAsync() {
        return this.commandExecutor.execute((Command<Batch>)new UpdateProcessInstancesSuspendStateBatchCmd(this.commandExecutor, this, false));
    }
    
    public List<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public ProcessInstanceQuery getProcessInstanceQuery() {
        return this.processInstanceQuery;
    }
    
    public HistoricProcessInstanceQuery getHistoricProcessInstanceQuery() {
        return this.historicProcessInstanceQuery;
    }
}
