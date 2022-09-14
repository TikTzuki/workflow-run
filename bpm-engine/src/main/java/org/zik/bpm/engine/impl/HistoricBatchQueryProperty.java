// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricBatchQueryProperty
{
    public static final QueryProperty ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
    public static final QueryProperty START_TIME = new QueryPropertyImpl("START_TIME_");
    public static final QueryProperty END_TIME = new QueryPropertyImpl("END_TIME_");
}
