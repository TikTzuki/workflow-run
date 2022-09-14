// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface ExternalTaskQueryProperty
{
    public static final QueryProperty ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty LOCK_EXPIRATION_TIME = new QueryPropertyImpl("LOCK_EXP_TIME_");
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROC_INST_ID_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROC_DEF_ID_");
    public static final QueryProperty PROCESS_DEFINITION_KEY = new QueryPropertyImpl("PROC_DEF_KEY_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
    public static final QueryProperty PRIORITY = new QueryPropertyImpl("PRIORITY_");
}
