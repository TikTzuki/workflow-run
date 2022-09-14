// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricProcessInstanceQueryProperty
{
    public static final QueryProperty PROCESS_INSTANCE_ID_ = new QueryPropertyImpl("PROC_INST_ID_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROC_DEF_ID_");
    public static final QueryProperty PROCESS_DEFINITION_KEY = new QueryPropertyImpl("PROC_DEF_KEY_");
    public static final QueryProperty PROCESS_DEFINITION_NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty PROCESS_DEFINITION_VERSION = new QueryPropertyImpl("VERSION_");
    public static final QueryProperty BUSINESS_KEY = new QueryPropertyImpl("BUSINESS_KEY_");
    public static final QueryProperty START_TIME = new QueryPropertyImpl("START_TIME_");
    public static final QueryProperty END_TIME = new QueryPropertyImpl("END_TIME_");
    public static final QueryProperty DURATION = new QueryPropertyImpl("DURATION_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
