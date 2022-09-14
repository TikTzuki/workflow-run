// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.runtime.InstantiationBuilder;
import org.zik.bpm.engine.impl.cmd.ProcessInstanceModificationBatchCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.ProcessInstanceModificationCmd;
import java.util.Arrays;
import java.util.Collections;
import org.zik.bpm.engine.impl.cmd.ActivityCancellationCmd;
import org.zik.bpm.engine.impl.cmd.TransitionInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityAfterInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityBeforeInstantiationCmd;
import java.util.ArrayList;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.cmd.AbstractProcessInstanceModificationCommand;
import java.util.List;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.runtime.ModificationBuilder;

public class ModificationBuilderImpl implements ModificationBuilder
{
    protected CommandExecutor commandExecutor;
    protected ProcessInstanceQuery processInstanceQuery;
    protected List<String> processInstanceIds;
    protected List<AbstractProcessInstanceModificationCommand> instructions;
    protected String processDefinitionId;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    protected String annotation;
    
    public ModificationBuilderImpl(final CommandExecutor commandExecutor, final String processDefinitionId) {
        this.commandExecutor = commandExecutor;
        EnsureUtil.ensureNotNull(NotValidException.class, "processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        this.processInstanceIds = new ArrayList<String>();
        this.instructions = new ArrayList<AbstractProcessInstanceModificationCommand>();
    }
    
    @Override
    public ModificationBuilder startBeforeActivity(final String activityId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        this.instructions.add(new ActivityBeforeInstantiationCmd(activityId));
        return this;
    }
    
    @Override
    public ModificationBuilder startAfterActivity(final String activityId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        this.instructions.add(new ActivityAfterInstantiationCmd(activityId));
        return this;
    }
    
    @Override
    public ModificationBuilder startTransition(final String transitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "transitionId", (Object)transitionId);
        this.instructions.add(new TransitionInstantiationCmd(transitionId));
        return this;
    }
    
    @Override
    public ModificationBuilder cancelAllForActivity(final String activityId) {
        return this.cancelAllForActivity(activityId, false);
    }
    
    @Override
    public ModificationBuilder cancelAllForActivity(final String activityId, final boolean cancelCurrentActiveActivityInstances) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        final ActivityCancellationCmd activityCancellationCmd = new ActivityCancellationCmd(activityId);
        activityCancellationCmd.setCancelCurrentActiveActivityInstances(cancelCurrentActiveActivityInstances);
        this.instructions.add(activityCancellationCmd);
        return this;
    }
    
    @Override
    public ModificationBuilder processInstanceIds(final List<String> processInstanceIds) {
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public ModificationBuilder processInstanceIds(final String... processInstanceIds) {
        if (processInstanceIds == null) {
            this.processInstanceIds = Collections.emptyList();
        }
        else {
            this.processInstanceIds = Arrays.asList(processInstanceIds);
        }
        return this;
    }
    
    @Override
    public ModificationBuilder processInstanceQuery(final ProcessInstanceQuery processInstanceQuery) {
        this.processInstanceQuery = processInstanceQuery;
        return this;
    }
    
    @Override
    public ModificationBuilder skipCustomListeners() {
        this.skipCustomListeners = true;
        return this;
    }
    
    @Override
    public ModificationBuilder skipIoMappings() {
        this.skipIoMappings = true;
        return this;
    }
    
    @Override
    public ModificationBuilder setAnnotation(final String annotation) {
        this.annotation = annotation;
        return this;
    }
    
    public void execute(final boolean writeUserOperationLog) {
        this.commandExecutor.execute((Command<Object>)new ProcessInstanceModificationCmd(this, writeUserOperationLog));
    }
    
    @Override
    public void execute() {
        this.execute(true);
    }
    
    @Override
    public Batch executeAsync() {
        return this.commandExecutor.execute((Command<Batch>)new ProcessInstanceModificationBatchCmd(this));
    }
    
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }
    
    public ProcessInstanceQuery getProcessInstanceQuery() {
        return this.processInstanceQuery;
    }
    
    public List<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    public List<AbstractProcessInstanceModificationCommand> getInstructions() {
        return this.instructions;
    }
    
    public void setInstructions(final List<AbstractProcessInstanceModificationCommand> instructions) {
        this.instructions = instructions;
    }
    
    public boolean isSkipCustomListeners() {
        return this.skipCustomListeners;
    }
    
    public boolean isSkipIoMappings() {
        return this.skipIoMappings;
    }
    
    public String getAnnotation() {
        return this.annotation;
    }
    
    public void setAnnotationInternal(final String annotation) {
        this.annotation = annotation;
    }
}
