// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.HistoricActivityStatistics;

public class HistoricActivityStatisticsImpl implements HistoricActivityStatistics
{
    protected String id;
    protected long instances;
    protected long finished;
    protected long canceled;
    protected long completeScope;
    protected long openIncidents;
    protected long resolvedIncidents;
    protected long deletedIncidents;
    
    @Override
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public long getInstances() {
        return this.instances;
    }
    
    public void setInstances(final long instances) {
        this.instances = instances;
    }
    
    @Override
    public long getFinished() {
        return this.finished;
    }
    
    public void setFinished(final long finished) {
        this.finished = finished;
    }
    
    @Override
    public long getCanceled() {
        return this.canceled;
    }
    
    public void setCanceled(final long canceled) {
        this.canceled = canceled;
    }
    
    @Override
    public long getCompleteScope() {
        return this.completeScope;
    }
    
    public void setCompleteScope(final long completeScope) {
        this.completeScope = completeScope;
    }
    
    @Override
    public long getOpenIncidents() {
        return this.openIncidents;
    }
    
    public void setOpenIncidents(final long openIncidents) {
        this.openIncidents = openIncidents;
    }
    
    @Override
    public long getResolvedIncidents() {
        return this.resolvedIncidents;
    }
    
    public void setResolvedIncidents(final long resolvedIncidents) {
        this.resolvedIncidents = resolvedIncidents;
    }
    
    @Override
    public long getDeletedIncidents() {
        return this.deletedIncidents;
    }
    
    public void setDeletedIncidents(final long closedIncidents) {
        this.deletedIncidents = closedIncidents;
    }
}
