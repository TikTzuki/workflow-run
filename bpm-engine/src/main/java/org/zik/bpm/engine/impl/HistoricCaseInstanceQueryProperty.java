// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricCaseInstanceQueryProperty
{
    public static final QueryProperty PROCESS_INSTANCE_ID_ = new QueryPropertyImpl("CASE_INST_ID_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("CASE_DEF_ID_");
    public static final QueryProperty BUSINESS_KEY = new QueryPropertyImpl("BUSINESS_KEY_");
    public static final QueryProperty CREATE_TIME = new QueryPropertyImpl("CREATE_TIME_");
    public static final QueryProperty CLOSE_TIME = new QueryPropertyImpl("CLOSE_TIME_");
    public static final QueryProperty DURATION = new QueryPropertyImpl("DURATION_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
