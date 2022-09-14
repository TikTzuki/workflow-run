// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.List;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class ExternalTaskCmd implements Command<Void>
{
    protected String externalTaskId;
    
    public ExternalTaskCmd(final String externalTaskId) {
        this.externalTaskId = externalTaskId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("externalTaskId", (Object)this.externalTaskId);
        this.validateInput();
        final ExternalTaskEntity externalTask = commandContext.getExternalTaskManager().findExternalTaskById(this.externalTaskId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "Cannot find external task with id " + this.externalTaskId, "externalTask", externalTask);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessInstanceById(externalTask.getProcessInstanceId());
        }
        this.writeUserOperationLog(commandContext, externalTask, this.getUserOperationLogOperationType(), this.getUserOperationLogPropertyChanges(externalTask));
        this.execute(externalTask);
        return null;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final ExternalTaskEntity externalTask, final String operationType, final List<PropertyChange> propertyChanges) {
        if (operationType != null) {
            commandContext.getOperationLogManager().logExternalTaskOperation(operationType, externalTask, (propertyChanges == null || propertyChanges.isEmpty()) ? Collections.singletonList(PropertyChange.EMPTY_CHANGE) : propertyChanges);
        }
    }
    
    protected String getUserOperationLogOperationType() {
        return null;
    }
    
    protected List<PropertyChange> getUserOperationLogPropertyChanges(final ExternalTaskEntity externalTask) {
        return Collections.emptyList();
    }
    
    protected abstract void execute(final ExternalTaskEntity p0);
    
    protected abstract void validateInput();
}
