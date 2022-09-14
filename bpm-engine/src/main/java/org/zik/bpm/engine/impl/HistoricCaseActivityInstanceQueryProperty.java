// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricCaseActivityInstanceQueryProperty
{
    public static final QueryProperty HISTORIC_CASE_ACTIVITY_INSTANCE_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty CASE_INSTANCE_ID = new QueryPropertyImpl("CASE_INST_ID_");
    public static final QueryProperty CASE_ACTIVITY_ID = new QueryPropertyImpl("CASE_ACT_ID_");
    public static final QueryProperty CASE_ACTIVITY_NAME = new QueryPropertyImpl("CASE_ACT_NAME_");
    public static final QueryProperty CASE_ACTIVITY_TYPE = new QueryPropertyImpl("CASE_ACT_TYPE_");
    public static final QueryProperty CASE_DEFINITION_ID = new QueryPropertyImpl("CASE_DEF_ID_");
    public static final QueryProperty CREATE = new QueryPropertyImpl("CREATE_TIME_");
    public static final QueryProperty END = new QueryPropertyImpl("END_TIME_");
    public static final QueryProperty DURATION = new QueryPropertyImpl("DURATION_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
