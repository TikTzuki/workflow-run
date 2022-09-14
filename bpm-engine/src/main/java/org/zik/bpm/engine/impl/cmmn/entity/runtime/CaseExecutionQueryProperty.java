// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.runtime;

import org.zik.bpm.engine.impl.QueryPropertyImpl;
import org.zik.bpm.engine.query.QueryProperty;

public interface CaseExecutionQueryProperty
{
    public static final QueryProperty CASE_EXECUTION_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty CASE_DEFINITION_ID = new QueryPropertyImpl("CASE_DEF_ID_");
    public static final QueryProperty CASE_DEFINITION_KEY = new QueryPropertyImpl("KEY_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
