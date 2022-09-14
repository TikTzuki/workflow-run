// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricJobLogQueryProperty
{
    public static final QueryProperty JOB_ID = new QueryPropertyImpl("JOB_ID_");
    public static final QueryProperty JOB_DEFINITION_ID = new QueryPropertyImpl("JOB_DEF_ID_");
    public static final QueryProperty TIMESTAMP = new QueryPropertyImpl("TIMESTAMP_");
    public static final QueryProperty ACTIVITY_ID = new QueryPropertyImpl("ACT_ID_");
    public static final QueryProperty EXECUTION_ID = new QueryPropertyImpl("EXECUTION_ID_");
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROCESS_INSTANCE_ID_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROCESS_DEF_ID_");
    public static final QueryProperty PROCESS_DEFINITION_KEY = new QueryPropertyImpl("PROCESS_DEF_KEY_");
    public static final QueryProperty DEPLOYMENT_ID = new QueryPropertyImpl("DEPLOYMENT_ID_");
    public static final QueryProperty DUEDATE = new QueryPropertyImpl("JOB_DUEDATE_");
    public static final QueryProperty RETRIES = new QueryPropertyImpl("JOB_RETRIES_");
    public static final QueryProperty PRIORITY = new QueryPropertyImpl("JOB_PRIORITY_");
    public static final QueryProperty SEQUENCE_COUNTER = new QueryPropertyImpl("SEQUENCE_COUNTER_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
    public static final QueryProperty HOSTNAME = new QueryPropertyImpl("HOSTNAME_");
}
