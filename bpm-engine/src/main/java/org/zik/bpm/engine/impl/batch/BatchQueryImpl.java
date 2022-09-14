// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.BatchQueryProperty;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.batch.BatchQuery;
import org.zik.bpm.engine.impl.AbstractQuery;

public class BatchQueryImpl extends AbstractQuery<BatchQuery, Batch> implements BatchQuery
{
    private static final long serialVersionUID = 1L;
    protected String batchId;
    protected String type;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected SuspensionState suspensionState;
    
    public BatchQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
    }
    
    @Override
    public BatchQuery batchId(final String batchId) {
        EnsureUtil.ensureNotNull("Batch id", (Object)batchId);
        this.batchId = batchId;
        return this;
    }
    
    public String getBatchId() {
        return this.batchId;
    }
    
    @Override
    public BatchQuery type(final String type) {
        EnsureUtil.ensureNotNull("Type", (Object)type);
        this.type = type;
        return this;
    }
    
    public String getType() {
        return this.type;
    }
    
    @Override
    public BatchQuery tenantIdIn(final String... tenantIds) {
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
    public BatchQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public BatchQuery active() {
        this.suspensionState = SuspensionState.ACTIVE;
        return this;
    }
    
    @Override
    public BatchQuery suspended() {
        this.suspensionState = SuspensionState.SUSPENDED;
        return this;
    }
    
    public SuspensionState getSuspensionState() {
        return this.suspensionState;
    }
    
    @Override
    public BatchQuery orderById() {
        return ((AbstractQuery<BatchQuery, U>)this).orderBy(BatchQueryProperty.ID);
    }
    
    @Override
    public BatchQuery orderByTenantId() {
        return ((AbstractQuery<BatchQuery, U>)this).orderBy(BatchQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getBatchManager().findBatchCountByQueryCriteria(this);
    }
    
    @Override
    public List<Batch> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getBatchManager().findBatchesByQueryCriteria(this, page);
    }
}
