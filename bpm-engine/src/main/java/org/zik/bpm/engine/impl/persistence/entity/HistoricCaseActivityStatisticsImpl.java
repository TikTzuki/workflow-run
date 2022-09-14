// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.HistoricCaseActivityStatistics;

public class HistoricCaseActivityStatisticsImpl implements HistoricCaseActivityStatistics
{
    protected String id;
    protected long available;
    protected long enabled;
    protected long disabled;
    protected long active;
    protected long completed;
    protected long terminated;
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public long getAvailable() {
        return this.available;
    }
    
    @Override
    public long getEnabled() {
        return this.enabled;
    }
    
    @Override
    public long getDisabled() {
        return this.disabled;
    }
    
    @Override
    public long getActive() {
        return this.active;
    }
    
    @Override
    public long getCompleted() {
        return this.completed;
    }
    
    @Override
    public long getTerminated() {
        return this.terminated;
    }
}
