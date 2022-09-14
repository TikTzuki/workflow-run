// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.db.PersistenceSession;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

final class SchemaOperationProcessEngineClose implements Command<Object>
{
    @Override
    public Object execute(final CommandContext commandContext) {
        final String databaseSchemaUpdate = Context.getProcessEngineConfiguration().getDatabaseSchemaUpdate();
        if ("create-drop".equals(databaseSchemaUpdate)) {
            commandContext.getSession(PersistenceSession.class).dbSchemaDrop();
        }
        return null;
    }
}
