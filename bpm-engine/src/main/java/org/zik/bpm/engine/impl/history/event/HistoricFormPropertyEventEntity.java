// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.util.Date;

public class HistoricFormPropertyEventEntity extends HistoricDetailEventEntity
{
    private static final long serialVersionUID = 1L;
    protected String propertyId;
    protected String propertyValue;
    
    public String getPropertyId() {
        return this.propertyId;
    }
    
    public void setPropertyId(final String propertyId) {
        this.propertyId = propertyId;
    }
    
    public Object getPropertyValue() {
        return this.propertyValue;
    }
    
    public void setPropertyValue(final String propertyValue) {
        this.propertyValue = propertyValue;
    }
    
    public Date getTime() {
        return this.timestamp;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[propertyId=" + this.propertyId + ", propertyValue=" + this.propertyValue + ", activityInstanceId=" + this.activityInstanceId + ", eventType=" + this.eventType + ", executionId=" + this.executionId + ", id=" + this.id + ", processDefinitionId=" + this.processDefinitionId + ", processInstanceId=" + this.processInstanceId + ", taskId=" + this.taskId + ", tenantId=" + this.tenantId + "]";
    }
}
