// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface JobDefinitionQueryProperty
{
    public static final QueryProperty JOB_DEFINITION_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty ACTIVITY_ID = new QueryPropertyImpl("ACT_ID_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROC_DEF_ID_");
    public static final QueryProperty PROCESS_DEFINITION_KEY = new QueryPropertyImpl("PROC_DEF_KEY_");
    public static final QueryProperty JOB_TYPE = new QueryPropertyImpl("JOB_TYPE_");
    public static final QueryProperty JOB_CONFIGURATION = new QueryPropertyImpl("JOB_CONFIGURATION_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
