// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricExternalTaskLogQueryProperty
{
    public static final QueryProperty EXTERNAL_TASK_ID = new QueryPropertyImpl("EXT_TASK_ID_");
    public static final QueryProperty TIMESTAMP = new QueryPropertyImpl("TIMESTAMP_");
    public static final QueryProperty TOPIC_NAME = new QueryPropertyImpl("TOPIC_NAME_");
    public static final QueryProperty WORKER_ID = new QueryPropertyImpl("WORKER_ID_");
    public static final QueryProperty ACTIVITY_ID = new QueryPropertyImpl("ACT_ID_");
    public static final QueryProperty ACTIVITY_INSTANCE_ID = new QueryPropertyImpl("ACT_INST_ID_");
    public static final QueryProperty EXECUTION_ID = new QueryPropertyImpl("EXECUTION_ID_");
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROC_INST_ID_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROC_DEF_ID_");
    public static final QueryProperty PROCESS_DEFINITION_KEY = new QueryPropertyImpl("PROC_DEF_KEY_");
    public static final QueryProperty RETRIES = new QueryPropertyImpl("RETRIES_");
    public static final QueryProperty PRIORITY = new QueryPropertyImpl("PRIORITY_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
