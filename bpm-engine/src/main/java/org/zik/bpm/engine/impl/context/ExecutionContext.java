// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.context;

import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

@Deprecated
public class ExecutionContext extends CoreExecutionContext<ExecutionEntity>
{
    public ExecutionContext(final ExecutionEntity execution) {
        super(execution);
    }
    
    public ExecutionEntity getProcessInstance() {
        return ((ExecutionEntity)this.execution).getProcessInstance();
    }
    
    public ProcessDefinitionEntity getProcessDefinition() {
        return ((ExecutionEntity)this.execution).getProcessDefinition();
    }
    
    @Override
    protected String getDeploymentId() {
        return this.getProcessDefinition().getDeploymentId();
    }
}
