// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;
import java.util.Arrays;
import java.util.Collections;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import org.zik.bpm.engine.externaltask.ExternalTaskQuery;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.externaltask.UpdateExternalTaskRetriesBuilder;

public class UpdateExternalTaskRetriesBuilderImpl implements UpdateExternalTaskRetriesBuilder
{
    protected CommandExecutor commandExecutor;
    protected List<String> externalTaskIds;
    protected List<String> processInstanceIds;
    protected ExternalTaskQuery externalTaskQuery;
    protected ProcessInstanceQuery processInstanceQuery;
    protected HistoricProcessInstanceQuery historicProcessInstanceQuery;
    protected int retries;
    
    public UpdateExternalTaskRetriesBuilderImpl(final List<String> externalTaskIds, final int retries) {
        this.externalTaskIds = externalTaskIds;
        this.retries = retries;
    }
    
    public UpdateExternalTaskRetriesBuilderImpl(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public UpdateExternalTaskRetriesBuilder externalTaskIds(final List<String> externalTaskIds) {
        this.externalTaskIds = externalTaskIds;
        return this;
    }
    
    @Override
    public UpdateExternalTaskRetriesBuilder externalTaskIds(final String... externalTaskIds) {
        if (externalTaskIds == null) {
            this.externalTaskIds = Collections.emptyList();
        }
        else {
            this.externalTaskIds = Arrays.asList(externalTaskIds);
        }
        return this;
    }
    
    @Override
    public UpdateExternalTaskRetriesBuilder processInstanceIds(final List<String> processInstanceIds) {
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public UpdateExternalTaskRetriesBuilder processInstanceIds(final String... processInstanceIds) {
        if (processInstanceIds == null) {
            this.processInstanceIds = Collections.emptyList();
        }
        else {
            this.processInstanceIds = Arrays.asList(processInstanceIds);
        }
        return this;
    }
    
    @Override
    public UpdateExternalTaskRetriesBuilder externalTaskQuery(final ExternalTaskQuery externalTaskQuery) {
        this.externalTaskQuery = externalTaskQuery;
        return this;
    }
    
    @Override
    public UpdateExternalTaskRetriesBuilder processInstanceQuery(final ProcessInstanceQuery processInstanceQuery) {
        this.processInstanceQuery = processInstanceQuery;
        return this;
    }
    
    @Override
    public UpdateExternalTaskRetriesBuilder historicProcessInstanceQuery(final HistoricProcessInstanceQuery historicProcessInstanceQuery) {
        this.historicProcessInstanceQuery = historicProcessInstanceQuery;
        return this;
    }
    
    @Override
    public void set(final int retries) {
        this.retries = retries;
        this.commandExecutor.execute((Command<Object>)new SetExternalTasksRetriesCmd(this));
    }
    
    @Override
    public Batch setAsync(final int retries) {
        this.retries = retries;
        return this.commandExecutor.execute((Command<Batch>)new SetExternalTasksRetriesBatchCmd(this));
    }
    
    public int getRetries() {
        return this.retries;
    }
    
    public List<String> getExternalTaskIds() {
        return this.externalTaskIds;
    }
    
    public List<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public ExternalTaskQuery getExternalTaskQuery() {
        return this.externalTaskQuery;
    }
    
    public ProcessInstanceQuery getProcessInstanceQuery() {
        return this.processInstanceQuery;
    }
    
    public HistoricProcessInstanceQuery getHistoricProcessInstanceQuery() {
        return this.historicProcessInstanceQuery;
    }
}
