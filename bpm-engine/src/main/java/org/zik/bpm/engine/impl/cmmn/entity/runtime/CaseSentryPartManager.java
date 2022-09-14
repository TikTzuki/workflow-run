// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.runtime;

import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.Page;
import java.util.List;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class CaseSentryPartManager extends AbstractManager
{
    public void insertCaseSentryPart(final CaseSentryPartEntity caseSentryPart) {
        this.getDbEntityManager().insert(caseSentryPart);
    }
    
    public void deleteSentryPart(final CaseSentryPartEntity caseSentryPart) {
        this.getDbEntityManager().delete(caseSentryPart);
    }
    
    public List<CaseSentryPartEntity> findCaseSentryPartsByCaseExecutionId(final String caseExecutionId) {
        return (List<CaseSentryPartEntity>)this.getDbEntityManager().selectList("selectCaseSentryPartsByCaseExecutionId", caseExecutionId);
    }
    
    public long findCaseSentryPartCountByQueryCriteria(final CaseSentryPartQueryImpl caseSentryPartQuery) {
        return (long)this.getDbEntityManager().selectOne("selectCaseSentryPartsCountByQueryCriteria", caseSentryPartQuery);
    }
    
    public List<CaseSentryPartEntity> findCaseSentryPartByQueryCriteria(final CaseSentryPartQueryImpl caseSentryPartQuery, final Page page) {
        return (List<CaseSentryPartEntity>)this.getDbEntityManager().selectList("selectCaseSentryPartsByQueryCriteria", caseSentryPartQuery, page);
    }
}
