// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.producer;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.event.HistoricCaseActivityInstanceEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoricCaseInstanceEventEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;

public class CacheAwareCmmnHistoryEventProducer extends DefaultCmmnHistoryEventProducer
{
    @Override
    protected HistoricCaseInstanceEventEntity loadCaseInstanceEventEntity(final CaseExecutionEntity caseExecutionEntity) {
        final String caseInstanceId = caseExecutionEntity.getCaseInstanceId();
        final HistoricCaseInstanceEventEntity cachedEntity = this.findInCache(HistoricCaseInstanceEventEntity.class, caseInstanceId);
        if (cachedEntity != null) {
            return cachedEntity;
        }
        return this.newCaseInstanceEventEntity(caseExecutionEntity);
    }
    
    @Override
    protected HistoricCaseActivityInstanceEventEntity loadCaseActivityInstanceEventEntity(final CaseExecutionEntity caseExecutionEntity) {
        final String caseActivityInstanceId = caseExecutionEntity.getId();
        final HistoricCaseActivityInstanceEventEntity cachedEntity = this.findInCache(HistoricCaseActivityInstanceEventEntity.class, caseActivityInstanceId);
        if (cachedEntity != null) {
            return cachedEntity;
        }
        return this.newCaseActivityInstanceEventEntity(caseExecutionEntity);
    }
    
    protected <T extends HistoryEvent> T findInCache(final Class<T> type, final String id) {
        return Context.getCommandContext().getDbEntityManager().getCachedEntity(type, id);
    }
}
