// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.AuthorizationException;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.Collections;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.management.SchemaLogEntry;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.SchemaLogQueryImpl;
import org.zik.bpm.engine.management.SchemaLogQuery;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class SchemaLogManager extends AbstractManager
{
    public Long findSchemaLogEntryCountByQueryCriteria(final SchemaLogQuery schemaLogQuery) {
        if (this.isAuthorized()) {
            return (Long)this.getDbEntityManager().selectOne("selectSchemaLogEntryCountByQueryCriteria", schemaLogQuery);
        }
        return 0L;
    }
    
    public List<SchemaLogEntry> findSchemaLogEntriesByQueryCriteria(final SchemaLogQueryImpl schemaLogQueryImpl, final Page page) {
        if (this.isAuthorized()) {
            return (List<SchemaLogEntry>)this.getDbEntityManager().selectList("selectSchemaLogEntryByQueryCriteria", schemaLogQueryImpl, page);
        }
        return Collections.emptyList();
    }
    
    private boolean isAuthorized() {
        try {
            this.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkReadSchemaLog);
            return true;
        }
        catch (AuthorizationException e) {
            return false;
        }
    }
}
