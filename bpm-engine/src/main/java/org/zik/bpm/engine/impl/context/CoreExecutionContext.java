// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.context;

import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;

public abstract class CoreExecutionContext<T extends CoreExecution>
{
    protected T execution;
    
    public CoreExecutionContext(final T execution) {
        this.execution = execution;
    }
    
    public T getExecution() {
        return this.execution;
    }
    
    protected abstract String getDeploymentId();
    
    public DeploymentEntity getDeployment() {
        return Context.getCommandContext().getDeploymentManager().findDeploymentById(this.getDeploymentId());
    }
}
