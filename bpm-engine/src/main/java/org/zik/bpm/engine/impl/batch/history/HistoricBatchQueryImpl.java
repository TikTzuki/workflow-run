// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.history;

import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.HistoricBatchQueryProperty;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.batch.history.HistoricBatch;
import org.zik.bpm.engine.batch.history.HistoricBatchQuery;
import org.zik.bpm.engine.impl.AbstractQuery;

public class HistoricBatchQueryImpl extends AbstractQuery<HistoricBatchQuery, HistoricBatch> implements HistoricBatchQuery
{
    private static final long serialVersionUID = 1L;
    protected String batchId;
    protected String type;
    protected Boolean completed;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    
    public HistoricBatchQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
    }
    
    @Override
    public HistoricBatchQuery batchId(final String batchId) {
        EnsureUtil.ensureNotNull("Batch id", (Object)batchId);
        this.batchId = batchId;
        return this;
    }
    
    public String getBatchId() {
        return this.batchId;
    }
    
    @Override
    public HistoricBatchQuery type(final String type) {
        EnsureUtil.ensureNotNull("Type", (Object)type);
        this.type = type;
        return this;
    }
    
    @Override
    public HistoricBatchQuery completed(final boolean completed) {
        this.completed = completed;
        return this;
    }
    
    @Override
    public HistoricBatchQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    public String[] getTenantIds() {
        return this.tenantIds;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    @Override
    public HistoricBatchQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    public String getType() {
        return this.type;
    }
    
    @Override
    public HistoricBatchQuery orderById() {
        return ((AbstractQuery<HistoricBatchQuery, U>)this).orderBy(HistoricBatchQueryProperty.ID);
    }
    
    @Override
    public HistoricBatchQuery orderByStartTime() {
        return ((AbstractQuery<HistoricBatchQuery, U>)this).orderBy(HistoricBatchQueryProperty.START_TIME);
    }
    
    @Override
    public HistoricBatchQuery orderByEndTime() {
        return ((AbstractQuery<HistoricBatchQuery, U>)this).orderBy(HistoricBatchQueryProperty.END_TIME);
    }
    
    @Override
    public HistoricBatchQuery orderByTenantId() {
        return ((AbstractQuery<HistoricBatchQuery, U>)this).orderBy(HistoricBatchQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricBatchManager().findBatchCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricBatch> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricBatchManager().findBatchesByQueryCriteria(this, page);
    }
}
