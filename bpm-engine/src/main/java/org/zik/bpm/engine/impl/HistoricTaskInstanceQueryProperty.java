// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricTaskInstanceQueryProperty
{
    public static final QueryProperty HISTORIC_TASK_INSTANCE_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROC_DEF_ID_");
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROC_INST_ID_");
    public static final QueryProperty EXECUTION_ID = new QueryPropertyImpl("EXECUTION_ID_");
    public static final QueryProperty ACTIVITY_INSTANCE_ID = new QueryPropertyImpl("ACT_INST_ID_");
    public static final QueryProperty TASK_NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty TASK_DESCRIPTION = new QueryPropertyImpl("DESCRIPTION_");
    public static final QueryProperty TASK_ASSIGNEE = new QueryPropertyImpl("ASSIGNEE_");
    public static final QueryProperty TASK_OWNER = new QueryPropertyImpl("OWNER_");
    public static final QueryProperty TASK_DEFINITION_KEY = new QueryPropertyImpl("TASK_DEF_KEY_");
    public static final QueryProperty DELETE_REASON = new QueryPropertyImpl("DELETE_REASON_");
    public static final QueryProperty START = new QueryPropertyImpl("START_TIME_");
    public static final QueryProperty END = new QueryPropertyImpl("END_TIME_");
    public static final QueryProperty DURATION = new QueryPropertyImpl("DURATION_");
    public static final QueryProperty TASK_PRIORITY = new QueryPropertyImpl("PRIORITY_");
    public static final QueryProperty TASK_DUE_DATE = new QueryPropertyImpl("DUE_DATE_");
    public static final QueryProperty TASK_FOLLOW_UP_DATE = new QueryPropertyImpl("FOLLOW_UP_DATE_");
    public static final QueryProperty CASE_DEFINITION_ID = new QueryPropertyImpl("CASE_DEFINITION_ID_");
    public static final QueryProperty CASE_INSTANCE_ID = new QueryPropertyImpl("CASE_INSTANCE_ID_");
    public static final QueryProperty CASE_EXECUTION_ID = new QueryPropertyImpl("CASE_EXECUTION_ID_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
