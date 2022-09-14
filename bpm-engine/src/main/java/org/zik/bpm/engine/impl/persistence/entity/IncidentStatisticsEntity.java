// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.management.IncidentStatistics;

public class IncidentStatisticsEntity implements IncidentStatistics
{
    protected String incidentType;
    protected int incidentCount;
    
    @Override
    public String getIncidentType() {
        return this.incidentType;
    }
    
    public void setIncidenType(final String incidentType) {
        this.incidentType = incidentType;
    }
    
    @Override
    public int getIncidentCount() {
        return this.incidentCount;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[incidentType=" + this.incidentType + ", incidentCount=" + this.incidentCount + "]";
    }
}
