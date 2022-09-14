// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public class ExecutionTopDownWalker extends ReferenceWalker<ExecutionEntity>
{
    public ExecutionTopDownWalker(final ExecutionEntity initialElement) {
        super(initialElement);
    }
    
    public ExecutionTopDownWalker(final List<ExecutionEntity> initialElements) {
        super(initialElements);
    }
    
    @Override
    protected Collection<ExecutionEntity> nextElements() {
        List<ExecutionEntity> executions = this.getCurrentElement().getExecutions();
        if (executions == null) {
            executions = new ArrayList<ExecutionEntity>();
        }
        return executions;
    }
}
