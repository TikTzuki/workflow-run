// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface TaskQueryProperty
{
    public static final QueryProperty TASK_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty NAME_CASE_INSENSITIVE = new QueryPropertyImpl("NAME_", "LOWER");
    public static final QueryProperty DESCRIPTION = new QueryPropertyImpl("DESCRIPTION_");
    public static final QueryProperty PRIORITY = new QueryPropertyImpl("PRIORITY_");
    public static final QueryProperty ASSIGNEE = new QueryPropertyImpl("ASSIGNEE_");
    public static final QueryProperty CREATE_TIME = new QueryPropertyImpl("CREATE_TIME_");
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROC_INST_ID_");
    public static final QueryProperty CASE_INSTANCE_ID = new QueryPropertyImpl("CASE_INST_ID_");
    public static final QueryProperty EXECUTION_ID = new QueryPropertyImpl("EXECUTION_ID_");
    public static final QueryProperty CASE_EXECUTION_ID = new QueryPropertyImpl("CASE_EXECUTION_ID_");
    public static final QueryProperty DUE_DATE = new QueryPropertyImpl("DUE_DATE_");
    public static final QueryProperty FOLLOW_UP_DATE = new QueryPropertyImpl("FOLLOW_UP_DATE_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
