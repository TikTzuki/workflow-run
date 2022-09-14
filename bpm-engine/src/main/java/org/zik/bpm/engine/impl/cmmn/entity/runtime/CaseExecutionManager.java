// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.runtime;

import org.zik.bpm.engine.runtime.CaseInstance;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.runtime.CaseExecution;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Arrays;
import org.zik.bpm.engine.BadUserRequestException;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class CaseExecutionManager extends AbstractManager
{
    public void insertCaseExecution(final CaseExecutionEntity caseExecution) {
        this.getDbEntityManager().insert(caseExecution);
    }
    
    public void deleteCaseExecution(final CaseExecutionEntity caseExecution) {
        this.getDbEntityManager().delete(caseExecution);
    }
    
    public void deleteCaseInstancesByCaseDefinition(final String caseDefinitionId, final String deleteReason, final boolean cascade) {
        final List<String> caseInstanceIds = (List<String>)this.getDbEntityManager().selectList("selectCaseInstanceIdsByCaseDefinitionId", caseDefinitionId);
        for (final String caseInstanceId : caseInstanceIds) {
            this.deleteCaseInstance(caseInstanceId, deleteReason, cascade);
        }
        if (cascade) {
            Context.getCommandContext().getHistoricCaseInstanceManager().deleteHistoricCaseInstanceByCaseDefinitionId(caseDefinitionId);
        }
    }
    
    public void deleteCaseInstance(final String caseInstanceId, final String deleteReason) {
        this.deleteCaseInstance(caseInstanceId, deleteReason, false);
    }
    
    public void deleteCaseInstance(final String caseInstanceId, final String deleteReason, final boolean cascade) {
        final CaseExecutionEntity execution = this.findCaseExecutionById(caseInstanceId);
        if (execution == null) {
            throw new BadUserRequestException("No case instance found for id '" + caseInstanceId + "'");
        }
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getTaskManager().deleteTasksByCaseInstanceId(caseInstanceId, deleteReason, cascade);
        execution.deleteCascade();
        if (cascade) {
            Context.getCommandContext().getHistoricCaseInstanceManager().deleteHistoricCaseInstancesByIds(Arrays.asList(caseInstanceId));
        }
    }
    
    public CaseExecutionEntity findCaseExecutionById(final String caseExecutionId) {
        return this.getDbEntityManager().selectById(CaseExecutionEntity.class, caseExecutionId);
    }
    
    public CaseExecutionEntity findSubCaseInstanceBySuperCaseExecutionId(final String superCaseExecutionId) {
        return (CaseExecutionEntity)this.getDbEntityManager().selectOne("selectSubCaseInstanceBySuperCaseExecutionId", superCaseExecutionId);
    }
    
    public CaseExecutionEntity findSubCaseInstanceBySuperExecutionId(final String superExecutionId) {
        return (CaseExecutionEntity)this.getDbEntityManager().selectOne("selectSubCaseInstanceBySuperExecutionId", superExecutionId);
    }
    
    public long findCaseExecutionCountByQueryCriteria(final CaseExecutionQueryImpl caseExecutionQuery) {
        this.configureTenantCheck(caseExecutionQuery);
        return (long)this.getDbEntityManager().selectOne("selectCaseExecutionCountByQueryCriteria", caseExecutionQuery);
    }
    
    public List<CaseExecution> findCaseExecutionsByQueryCriteria(final CaseExecutionQueryImpl caseExecutionQuery, final Page page) {
        this.configureTenantCheck(caseExecutionQuery);
        return (List<CaseExecution>)this.getDbEntityManager().selectList("selectCaseExecutionsByQueryCriteria", caseExecutionQuery, page);
    }
    
    public long findCaseInstanceCountByQueryCriteria(final CaseInstanceQueryImpl caseInstanceQuery) {
        this.configureTenantCheck(caseInstanceQuery);
        return (long)this.getDbEntityManager().selectOne("selectCaseInstanceCountByQueryCriteria", caseInstanceQuery);
    }
    
    public List<CaseInstance> findCaseInstanceByQueryCriteria(final CaseInstanceQueryImpl caseInstanceQuery, final Page page) {
        this.configureTenantCheck(caseInstanceQuery);
        return (List<CaseInstance>)this.getDbEntityManager().selectList("selectCaseInstanceByQueryCriteria", caseInstanceQuery, page);
    }
    
    public List<CaseExecutionEntity> findChildCaseExecutionsByParentCaseExecutionId(final String parentCaseExecutionId) {
        return (List<CaseExecutionEntity>)this.getDbEntityManager().selectList("selectCaseExecutionsByParentCaseExecutionId", parentCaseExecutionId);
    }
    
    public List<CaseExecutionEntity> findChildCaseExecutionsByCaseInstanceId(final String caseInstanceId) {
        return (List<CaseExecutionEntity>)this.getDbEntityManager().selectList("selectCaseExecutionsByCaseInstanceId", caseInstanceId);
    }
    
    protected void configureTenantCheck(final AbstractQuery<?, ?> query) {
        this.getTenantManager().configureQuery(query);
    }
}
