// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.db.PersistenceSession;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.SchemaOperationsCommand;

public class SchemaOperationsProcessEngineBuild implements SchemaOperationsCommand
{
    private static final EnginePersistenceLogger LOG;
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final String databaseSchemaUpdate = Context.getProcessEngineConfiguration().getDatabaseSchemaUpdate();
        final PersistenceSession persistenceSession = commandContext.getSession(PersistenceSession.class);
        if ("drop-create".equals(databaseSchemaUpdate)) {
            try {
                persistenceSession.dbSchemaDrop();
            }
            catch (RuntimeException ex) {}
        }
        if ("create-drop".equals(databaseSchemaUpdate) || "drop-create".equals(databaseSchemaUpdate) || "create".equals(databaseSchemaUpdate)) {
            persistenceSession.dbSchemaCreate();
        }
        else if ("false".equals(databaseSchemaUpdate)) {
            persistenceSession.dbSchemaCheckVersion();
        }
        else if ("true".equals(databaseSchemaUpdate)) {
            persistenceSession.dbSchemaUpdate();
        }
        return null;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
