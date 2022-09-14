// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.runtime.InstantiationBuilder;
import java.util.Collection;
import java.util.Arrays;
import org.zik.bpm.engine.impl.batch.RestartProcessInstancesBatchCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.RestartProcessInstancesCmd;
import org.zik.bpm.engine.impl.cmd.TransitionInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityAfterInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityBeforeInstantiationCmd;
import java.util.ArrayList;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.impl.cmd.AbstractProcessInstanceModificationCommand;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.runtime.RestartProcessInstanceBuilder;

public class RestartProcessInstanceBuilderImpl implements RestartProcessInstanceBuilder
{
    protected CommandExecutor commandExecutor;
    protected List<String> processInstanceIds;
    protected List<AbstractProcessInstanceModificationCommand> instructions;
    protected String processDefinitionId;
    protected HistoricProcessInstanceQuery query;
    protected boolean initialVariables;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    protected boolean withoutBusinessKey;
    
    public RestartProcessInstanceBuilderImpl(final CommandExecutor commandExecutor, final String processDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processDefinitionId", (Object)processDefinitionId);
        this.commandExecutor = commandExecutor;
        this.instructions = new ArrayList<AbstractProcessInstanceModificationCommand>();
        this.processDefinitionId = processDefinitionId;
        this.processInstanceIds = new ArrayList<String>();
    }
    
    public RestartProcessInstanceBuilderImpl(final String processDefinitionId) {
        this(null, processDefinitionId);
    }
    
    @Override
    public RestartProcessInstanceBuilder startBeforeActivity(final String activityId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        this.instructions.add(new ActivityBeforeInstantiationCmd(null, activityId));
        return this;
    }
    
    @Override
    public RestartProcessInstanceBuilder startAfterActivity(final String activityId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        this.instructions.add(new ActivityAfterInstantiationCmd(null, activityId));
        return this;
    }
    
    @Override
    public RestartProcessInstanceBuilder startTransition(final String transitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)transitionId);
        this.instructions.add(new TransitionInstantiationCmd(null, transitionId));
        return this;
    }
    
    @Override
    public void execute() {
        this.commandExecutor.execute((Command<Object>)new RestartProcessInstancesCmd(this.commandExecutor, this));
    }
    
    @Override
    public Batch executeAsync() {
        return this.commandExecutor.execute((Command<Batch>)new RestartProcessInstancesBatchCmd(this.commandExecutor, this));
    }
    
    public List<AbstractProcessInstanceModificationCommand> getInstructions() {
        return this.instructions;
    }
    
    public List<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    @Override
    public RestartProcessInstanceBuilder processInstanceIds(final String... processInstanceIds) {
        this.processInstanceIds.addAll(Arrays.asList(processInstanceIds));
        return this;
    }
    
    @Override
    public RestartProcessInstanceBuilder historicProcessInstanceQuery(final HistoricProcessInstanceQuery query) {
        this.query = query;
        return this;
    }
    
    public HistoricProcessInstanceQuery getHistoricProcessInstanceQuery() {
        return this.query;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setInstructions(final List<AbstractProcessInstanceModificationCommand> instructions) {
        this.instructions = instructions;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public RestartProcessInstanceBuilder processInstanceIds(final List<String> processInstanceIds) {
        this.processInstanceIds.addAll(processInstanceIds);
        return this;
    }
    
    @Override
    public RestartProcessInstanceBuilder initialSetOfVariables() {
        this.initialVariables = true;
        return this;
    }
    
    public boolean isInitialVariables() {
        return this.initialVariables;
    }
    
    @Override
    public RestartProcessInstanceBuilder skipCustomListeners() {
        this.skipCustomListeners = true;
        return this;
    }
    
    @Override
    public RestartProcessInstanceBuilder skipIoMappings() {
        this.skipIoMappings = true;
        return this;
    }
    
    public boolean isSkipCustomListeners() {
        return this.skipCustomListeners;
    }
    
    public boolean isSkipIoMappings() {
        return this.skipIoMappings;
    }
    
    @Override
    public RestartProcessInstanceBuilder withoutBusinessKey() {
        this.withoutBusinessKey = true;
        return this;
    }
    
    public boolean isWithoutBusinessKey() {
        return this.withoutBusinessKey;
    }
}
