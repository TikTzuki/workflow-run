// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.HistoricFormField;
import org.zik.bpm.engine.history.HistoricFormProperty;
import org.zik.bpm.engine.impl.history.event.HistoricFormPropertyEventEntity;

public class HistoricFormPropertyEntity extends HistoricFormPropertyEventEntity implements HistoricFormProperty, HistoricFormField
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public String getPropertyValue() {
        if (this.propertyValue != null) {
            return this.propertyValue.toString();
        }
        return null;
    }
    
    @Override
    public String getFieldId() {
        return this.propertyId;
    }
    
    @Override
    public Object getFieldValue() {
        return this.propertyValue;
    }
}
