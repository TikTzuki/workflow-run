// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity.optimize;

import org.zik.bpm.engine.impl.persistence.entity.HistoricIdentityLinkLogEntity;

public class OptimizeHistoricIdentityLinkLogEntity extends HistoricIdentityLinkLogEntity
{
    protected String processInstanceId;
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    @Override
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
