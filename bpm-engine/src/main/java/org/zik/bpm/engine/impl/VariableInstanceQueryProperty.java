// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface VariableInstanceQueryProperty
{
    public static final QueryProperty VARIABLE_NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty VARIABLE_TYPE = new QueryPropertyImpl("TYPE_");
    public static final QueryProperty ACTIVITY_INSTANCE_ID = new QueryPropertyImpl("ACT_INST_ID_");
    public static final QueryProperty EXECUTION_ID = new QueryPropertyImpl("EXECUTION_ID_");
    public static final QueryProperty TASK_ID = new QueryPropertyImpl("TASK_ID_");
    public static final QueryProperty CASE_EXECUTION_ID = new QueryPropertyImpl("CASE_EXECUTION_ID_");
    public static final QueryProperty CASE_INSTANCE_ID = new QueryPropertyImpl("CASE_INST_ID_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
    public static final QueryProperty TEXT = new QueryPropertyImpl("TEXT_");
    public static final QueryProperty TEXT_AS_LOWER = new QueryPropertyImpl("TEXT_", "LOWER");
    public static final QueryProperty DOUBLE = new QueryPropertyImpl("DOUBLE_");
    public static final QueryProperty LONG = new QueryPropertyImpl("LONG_");
}
