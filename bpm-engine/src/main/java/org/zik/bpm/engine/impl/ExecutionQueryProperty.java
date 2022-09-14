// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface ExecutionQueryProperty
{
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROC_INST_ID_");
    public static final QueryProperty PROCESS_DEFINITION_KEY = new QueryPropertyImpl("KEY_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty SEQUENCE_COUNTER = new QueryPropertyImpl("SEQUENCE_COUNTER_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
