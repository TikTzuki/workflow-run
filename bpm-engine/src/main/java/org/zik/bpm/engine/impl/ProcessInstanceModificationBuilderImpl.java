// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.runtime.ActivityInstantiationBuilder;
import org.zik.bpm.engine.runtime.InstantiationBuilder;
import org.zik.bpm.engine.impl.cmd.ModifyProcessInstanceAsyncCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.ModifyProcessInstanceCmd;
import java.util.Map;
import org.zik.bpm.engine.impl.cmd.TransitionInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityAfterInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.AbstractInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityBeforeInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityCancellationCmd;
import org.zik.bpm.engine.impl.cmd.TransitionInstanceCancellationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityInstanceCancellationCmd;
import org.zik.bpm.engine.runtime.ProcessInstanceModificationBuilder;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import java.util.ArrayList;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.cmd.AbstractProcessInstanceModificationCommand;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.runtime.ProcessInstanceModificationInstantiationBuilder;

public class ProcessInstanceModificationBuilderImpl implements ProcessInstanceModificationInstantiationBuilder
{
    protected CommandExecutor commandExecutor;
    protected CommandContext commandContext;
    protected String processInstanceId;
    protected String modificationReason;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    protected boolean externallyTerminated;
    protected String annotation;
    protected List<AbstractProcessInstanceModificationCommand> operations;
    protected VariableMap processVariables;
    
    public ProcessInstanceModificationBuilderImpl(final CommandExecutor commandExecutor, final String processInstanceId) {
        this(processInstanceId);
        this.commandExecutor = commandExecutor;
    }
    
    public ProcessInstanceModificationBuilderImpl(final CommandContext commandContext, final String processInstanceId) {
        this(processInstanceId);
        this.commandContext = commandContext;
    }
    
    public ProcessInstanceModificationBuilderImpl(final CommandContext commandContext, final String processInstanceId, final String modificationReason) {
        this(processInstanceId);
        this.commandContext = commandContext;
        this.modificationReason = modificationReason;
    }
    
    public ProcessInstanceModificationBuilderImpl(final String processInstanceId) {
        this.skipCustomListeners = false;
        this.skipIoMappings = false;
        this.externallyTerminated = false;
        this.operations = new ArrayList<AbstractProcessInstanceModificationCommand>();
        this.processVariables = (VariableMap)new VariableMapImpl();
        EnsureUtil.ensureNotNull(NotValidException.class, "processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
    }
    
    public ProcessInstanceModificationBuilderImpl() {
        this.skipCustomListeners = false;
        this.skipIoMappings = false;
        this.externallyTerminated = false;
        this.operations = new ArrayList<AbstractProcessInstanceModificationCommand>();
        this.processVariables = (VariableMap)new VariableMapImpl();
    }
    
    @Override
    public ProcessInstanceModificationBuilder cancelActivityInstance(final String activityInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityInstanceId", (Object)activityInstanceId);
        this.operations.add(new ActivityInstanceCancellationCmd(this.processInstanceId, activityInstanceId, this.modificationReason));
        return this;
    }
    
    @Override
    public ProcessInstanceModificationBuilder cancelTransitionInstance(final String transitionInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "transitionInstanceId", (Object)transitionInstanceId);
        this.operations.add(new TransitionInstanceCancellationCmd(this.processInstanceId, transitionInstanceId));
        return this;
    }
    
    @Override
    public ProcessInstanceModificationBuilder cancelAllForActivity(final String activityId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        this.operations.add(new ActivityCancellationCmd(this.processInstanceId, activityId));
        return this;
    }
    
    @Override
    public ProcessInstanceModificationBuilder cancellationSourceExternal(final boolean external) {
        this.externallyTerminated = external;
        return this;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder startBeforeActivity(final String activityId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        final AbstractInstantiationCmd currentInstantiation = new ActivityBeforeInstantiationCmd(this.processInstanceId, activityId);
        this.operations.add(currentInstantiation);
        return this;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder startBeforeActivity(final String activityId, final String ancestorActivityInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        EnsureUtil.ensureNotNull(NotValidException.class, "ancestorActivityInstanceId", (Object)ancestorActivityInstanceId);
        final AbstractInstantiationCmd currentInstantiation = new ActivityBeforeInstantiationCmd(this.processInstanceId, activityId, ancestorActivityInstanceId);
        this.operations.add(currentInstantiation);
        return this;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder startAfterActivity(final String activityId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        final AbstractInstantiationCmd currentInstantiation = new ActivityAfterInstantiationCmd(this.processInstanceId, activityId);
        this.operations.add(currentInstantiation);
        return this;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder startAfterActivity(final String activityId, final String ancestorActivityInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        EnsureUtil.ensureNotNull(NotValidException.class, "ancestorActivityInstanceId", (Object)ancestorActivityInstanceId);
        final AbstractInstantiationCmd currentInstantiation = new ActivityAfterInstantiationCmd(this.processInstanceId, activityId, ancestorActivityInstanceId);
        this.operations.add(currentInstantiation);
        return this;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder startTransition(final String transitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "transitionId", (Object)transitionId);
        final AbstractInstantiationCmd currentInstantiation = new TransitionInstantiationCmd(this.processInstanceId, transitionId);
        this.operations.add(currentInstantiation);
        return this;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder startTransition(final String transitionId, final String ancestorActivityInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "transitionId", (Object)transitionId);
        EnsureUtil.ensureNotNull(NotValidException.class, "ancestorActivityInstanceId", (Object)ancestorActivityInstanceId);
        final AbstractInstantiationCmd currentInstantiation = new TransitionInstantiationCmd(this.processInstanceId, transitionId, ancestorActivityInstanceId);
        this.operations.add(currentInstantiation);
        return this;
    }
    
    protected AbstractInstantiationCmd getCurrentInstantiation() {
        if (this.operations.isEmpty()) {
            return null;
        }
        final AbstractProcessInstanceModificationCommand lastInstantiationCmd = this.operations.get(this.operations.size() - 1);
        if (!(lastInstantiationCmd instanceof AbstractInstantiationCmd)) {
            throw new ProcessEngineException("last instruction is not an instantiation");
        }
        return (AbstractInstantiationCmd)lastInstantiationCmd;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder setVariable(final String name, final Object value) {
        EnsureUtil.ensureNotNull(NotValidException.class, "Variable name must not be null", "name", name);
        final AbstractInstantiationCmd currentInstantiation = this.getCurrentInstantiation();
        if (currentInstantiation != null) {
            currentInstantiation.addVariable(name, value);
        }
        else {
            this.processVariables.put((Object)name, value);
        }
        return this;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder setVariableLocal(final String name, final Object value) {
        EnsureUtil.ensureNotNull(NotValidException.class, "Variable name must not be null", "name", name);
        final AbstractInstantiationCmd currentInstantiation = this.getCurrentInstantiation();
        if (currentInstantiation != null) {
            currentInstantiation.addVariableLocal(name, value);
        }
        else {
            this.processVariables.put((Object)name, value);
        }
        return this;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder setVariables(final Map<String, Object> variables) {
        EnsureUtil.ensureNotNull(NotValidException.class, "Variable map must not be null", "variables", variables);
        final AbstractInstantiationCmd currentInstantiation = this.getCurrentInstantiation();
        if (currentInstantiation != null) {
            currentInstantiation.addVariables(variables);
        }
        else {
            this.processVariables.putAll((Map)variables);
        }
        return this;
    }
    
    @Override
    public ProcessInstanceModificationInstantiationBuilder setVariablesLocal(final Map<String, Object> variables) {
        EnsureUtil.ensureNotNull(NotValidException.class, "Variable map must not be null", "variablesLocal", variables);
        final AbstractInstantiationCmd currentInstantiation = this.getCurrentInstantiation();
        if (currentInstantiation != null) {
            currentInstantiation.addVariablesLocal(variables);
        }
        else {
            this.processVariables.putAll((Map)variables);
        }
        return this;
    }
    
    @Override
    public ProcessInstanceModificationBuilder setAnnotation(final String annotation) {
        EnsureUtil.ensureNotNull(NotValidException.class, "Annotation must not be null", "annotation", annotation);
        this.annotation = annotation;
        return this;
    }
    
    @Override
    public void execute() {
        this.execute(false, false);
    }
    
    @Override
    public void execute(final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.execute(true, skipCustomListeners, skipIoMappings);
    }
    
    public void execute(final boolean writeUserOperationLog, final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMappings = skipIoMappings;
        final ModifyProcessInstanceCmd cmd = new ModifyProcessInstanceCmd(this, writeUserOperationLog);
        if (this.commandExecutor != null) {
            this.commandExecutor.execute((Command<Object>)cmd);
        }
        else {
            cmd.execute(this.commandContext);
        }
    }
    
    @Override
    public Batch executeAsync() {
        return this.executeAsync(false, false);
    }
    
    @Override
    public Batch executeAsync(final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMappings = skipIoMappings;
        return this.commandExecutor.execute((Command<Batch>)new ModifyProcessInstanceAsyncCmd(this));
    }
    
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }
    
    public CommandContext getCommandContext() {
        return this.commandContext;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public List<AbstractProcessInstanceModificationCommand> getModificationOperations() {
        return this.operations;
    }
    
    public void setModificationOperations(final List<AbstractProcessInstanceModificationCommand> operations) {
        this.operations = operations;
    }
    
    public boolean isSkipCustomListeners() {
        return this.skipCustomListeners;
    }
    
    public boolean isSkipIoMappings() {
        return this.skipIoMappings;
    }
    
    public boolean isExternallyTerminated() {
        return this.externallyTerminated;
    }
    
    public void setSkipCustomListeners(final boolean skipCustomListeners) {
        this.skipCustomListeners = skipCustomListeners;
    }
    
    public void setSkipIoMappings(final boolean skipIoMappings) {
        this.skipIoMappings = skipIoMappings;
    }
    
    public VariableMap getProcessVariables() {
        return this.processVariables;
    }
    
    public String getModificationReason() {
        return this.modificationReason;
    }
    
    public void setModificationReason(final String modificationReason) {
        this.modificationReason = modificationReason;
    }
    
    public String getAnnotation() {
        return this.annotation;
    }
    
    public void setAnnotationInternal(final String annotation) {
        this.annotation = annotation;
    }
}
