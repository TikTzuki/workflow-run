// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.runtime;

import org.zik.bpm.engine.impl.QueryPropertyImpl;
import org.zik.bpm.engine.query.QueryProperty;

public interface CaseSentryPartQueryProperty
{
    public static final QueryProperty CASE_SENTRY_PART_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty CASE_INSTANCE_ID = new QueryPropertyImpl("CASE_INST_ID_");
    public static final QueryProperty CASE_EXECUTION_ID = new QueryPropertyImpl("CASE_EXEC_ID");
    public static final QueryProperty SENTRY_ID = new QueryPropertyImpl("SENTRY_ID_");
    public static final QueryProperty SOURCE = new QueryPropertyImpl("SOURCE");
}
