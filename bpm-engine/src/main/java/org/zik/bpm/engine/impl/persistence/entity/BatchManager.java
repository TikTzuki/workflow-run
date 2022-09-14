// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.batch.Batch;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.batch.BatchQueryImpl;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class BatchManager extends AbstractManager
{
    public void insertBatch(final BatchEntity batch) {
        batch.setCreateUserId(this.getCommandContext().getAuthenticatedUserId());
        this.getDbEntityManager().insert(batch);
    }
    
    public BatchEntity findBatchById(final String id) {
        return this.getDbEntityManager().selectById(BatchEntity.class, id);
    }
    
    public long findBatchCountByQueryCriteria(final BatchQueryImpl batchQuery) {
        this.configureQuery(batchQuery);
        return (long)this.getDbEntityManager().selectOne("selectBatchCountByQueryCriteria", batchQuery);
    }
    
    public List<Batch> findBatchesByQueryCriteria(final BatchQueryImpl batchQuery, final Page page) {
        this.configureQuery(batchQuery);
        return (List<Batch>)this.getDbEntityManager().selectList("selectBatchesByQueryCriteria", batchQuery, page);
    }
    
    public void updateBatchSuspensionStateById(final String batchId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("batchId", batchId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        final ListQueryParameterObject queryParameter = new ListQueryParameterObject();
        queryParameter.setParameter(parameters);
        this.getDbEntityManager().update(BatchEntity.class, "updateBatchSuspensionStateByParameters", queryParameter);
    }
    
    protected void configureQuery(final BatchQueryImpl batchQuery) {
        this.getAuthorizationManager().configureBatchQuery(batchQuery);
        this.getTenantManager().configureQuery(batchQuery);
    }
}
