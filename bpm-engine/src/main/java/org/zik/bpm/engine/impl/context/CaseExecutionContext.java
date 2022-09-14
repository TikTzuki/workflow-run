// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.context;

import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;

public class CaseExecutionContext extends CoreExecutionContext<CaseExecutionEntity>
{
    public CaseExecutionContext(final CaseExecutionEntity execution) {
        super(execution);
    }
    
    public CaseExecutionEntity getCaseInstance() {
        return ((CaseExecutionEntity)this.execution).getCaseInstance();
    }
    
    public CaseDefinitionEntity getCaseDefinition() {
        return (CaseDefinitionEntity)((CaseExecutionEntity)this.execution).getCaseDefinition();
    }
    
    @Override
    protected String getDeploymentId() {
        return this.getCaseDefinition().getDeploymentId();
    }
}
