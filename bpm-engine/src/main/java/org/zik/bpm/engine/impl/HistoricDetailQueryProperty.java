// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricDetailQueryProperty
{
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROC_INST_ID_");
    public static final QueryProperty VARIABLE_NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty VARIABLE_TYPE = new QueryPropertyImpl("TYPE_");
    public static final QueryProperty VARIABLE_REVISION = new QueryPropertyImpl("REV_");
    public static final QueryProperty TIME = new QueryPropertyImpl("TIME_");
    public static final QueryProperty SEQUENCE_COUNTER = new QueryPropertyImpl("SEQUENCE_COUNTER_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
