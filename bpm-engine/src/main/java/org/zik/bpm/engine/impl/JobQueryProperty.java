// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface JobQueryProperty
{
    public static final QueryProperty JOB_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROCESS_INSTANCE_ID_");
    public static final QueryProperty EXECUTION_ID = new QueryPropertyImpl("EXECUTION_ID_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROCESS_DEF_ID_");
    public static final QueryProperty PROCESS_DEFINITION_KEY = new QueryPropertyImpl("PROCESS_DEF_KEY_");
    public static final QueryProperty DUEDATE = new QueryPropertyImpl("DUEDATE_");
    public static final QueryProperty RETRIES = new QueryPropertyImpl("RETRIES_");
    public static final QueryProperty TYPE = new QueryPropertyImpl("TYPE_");
    public static final QueryProperty PRIORITY = new QueryPropertyImpl("PRIORITY_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
