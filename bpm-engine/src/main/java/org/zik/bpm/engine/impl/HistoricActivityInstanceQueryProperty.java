// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricActivityInstanceQueryProperty
{
    public static final QueryProperty HISTORIC_ACTIVITY_INSTANCE_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROC_INST_ID_");
    public static final QueryProperty EXECUTION_ID = new QueryPropertyImpl("EXECUTION_ID_");
    public static final QueryProperty ACTIVITY_ID = new QueryPropertyImpl("ACT_ID_");
    public static final QueryProperty ACTIVITY_NAME = new QueryPropertyImpl("ACT_NAME_");
    public static final QueryProperty ACTIVITY_TYPE = new QueryPropertyImpl("ACT_TYPE_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROC_DEF_ID_");
    public static final QueryProperty START = new QueryPropertyImpl("START_TIME_");
    public static final QueryProperty END = new QueryPropertyImpl("END_TIME_");
    public static final QueryProperty DURATION = new QueryPropertyImpl("DURATION_");
    public static final QueryProperty SEQUENCE_COUNTER = new QueryPropertyImpl("SEQUENCE_COUNTER_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
