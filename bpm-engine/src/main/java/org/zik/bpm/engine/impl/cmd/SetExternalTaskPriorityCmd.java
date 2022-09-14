// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;

public class SetExternalTaskPriorityCmd extends ExternalTaskCmd
{
    protected long priority;
    
    public SetExternalTaskPriorityCmd(final String externalTaskId, final long priority) {
        super(externalTaskId);
        this.priority = priority;
    }
    
    @Override
    protected void execute(final ExternalTaskEntity externalTask) {
        externalTask.setPriority(this.priority);
    }
    
    @Override
    protected void validateInput() {
    }
    
    @Override
    protected String getUserOperationLogOperationType() {
        return "SetPriority";
    }
    
    @Override
    protected List<PropertyChange> getUserOperationLogPropertyChanges(final ExternalTaskEntity externalTask) {
        return Collections.singletonList(new PropertyChange("priority", externalTask.getPriority(), this.priority));
    }
}
