// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.query.QueryProperty;
import org.zik.bpm.engine.management.SchemaLogEntry;
import org.zik.bpm.engine.management.SchemaLogQuery;

public class SchemaLogQueryImpl extends AbstractQuery<SchemaLogQuery, SchemaLogEntry> implements SchemaLogQuery
{
    private static final long serialVersionUID = 1L;
    private static final QueryProperty TIMESTAMP_PROPERTY;
    protected String version;
    
    public SchemaLogQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public SchemaLogQuery version(final String version) {
        EnsureUtil.ensureNotNull("version", (Object)version);
        this.version = version;
        return this;
    }
    
    @Override
    public SchemaLogQuery orderByTimestamp() {
        this.orderBy(SchemaLogQueryImpl.TIMESTAMP_PROPERTY);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getSchemaLogManager().findSchemaLogEntryCountByQueryCriteria(this);
    }
    
    @Override
    public List<SchemaLogEntry> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getSchemaLogManager().findSchemaLogEntriesByQueryCriteria(this, page);
    }
    
    static {
        TIMESTAMP_PROPERTY = new QueryPropertyImpl("TIMESTAMP_");
    }
}
