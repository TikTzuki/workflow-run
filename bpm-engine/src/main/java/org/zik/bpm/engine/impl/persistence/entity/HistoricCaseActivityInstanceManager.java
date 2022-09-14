// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricCaseActivityInstance;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricCaseActivityInstanceQueryImpl;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricCaseActivityInstanceManager extends AbstractHistoricManager
{
    public void deleteHistoricCaseActivityInstancesByCaseInstanceIds(final List<String> historicCaseInstanceIds) {
        if (this.isHistoryEnabled()) {
            this.getDbEntityManager().delete(HistoricCaseActivityInstanceEntity.class, "deleteHistoricCaseActivityInstancesByCaseInstanceIds", historicCaseInstanceIds);
        }
    }
    
    public void insertHistoricCaseActivityInstance(final HistoricCaseActivityInstanceEntity historicCaseActivityInstance) {
        this.getDbEntityManager().insert(historicCaseActivityInstance);
    }
    
    public HistoricCaseActivityInstanceEntity findHistoricCaseActivityInstance(final String caseActivityId, final String caseInstanceId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("caseActivityId", caseActivityId);
        parameters.put("caseInstanceId", caseInstanceId);
        return (HistoricCaseActivityInstanceEntity)this.getDbEntityManager().selectOne("selectHistoricCaseActivityInstance", parameters);
    }
    
    public long findHistoricCaseActivityInstanceCountByQueryCriteria(final HistoricCaseActivityInstanceQueryImpl historicCaseActivityInstanceQuery) {
        this.configureHistoricCaseActivityInstanceQuery(historicCaseActivityInstanceQuery);
        return (long)this.getDbEntityManager().selectOne("selectHistoricCaseActivityInstanceCountByQueryCriteria", historicCaseActivityInstanceQuery);
    }
    
    public List<HistoricCaseActivityInstance> findHistoricCaseActivityInstancesByQueryCriteria(final HistoricCaseActivityInstanceQueryImpl historicCaseActivityInstanceQuery, final Page page) {
        this.configureHistoricCaseActivityInstanceQuery(historicCaseActivityInstanceQuery);
        return (List<HistoricCaseActivityInstance>)this.getDbEntityManager().selectList("selectHistoricCaseActivityInstancesByQueryCriteria", historicCaseActivityInstanceQuery, page);
    }
    
    public List<HistoricCaseActivityInstance> findHistoricCaseActivityInstancesByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<HistoricCaseActivityInstance>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricCaseActivityInstanceByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findHistoricCaseActivityInstanceCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectHistoricCaseActivityInstanceCountByNativeQuery", parameterMap);
    }
    
    protected void configureHistoricCaseActivityInstanceQuery(final HistoricCaseActivityInstanceQueryImpl query) {
        this.getTenantManager().configureQuery(query);
    }
}
