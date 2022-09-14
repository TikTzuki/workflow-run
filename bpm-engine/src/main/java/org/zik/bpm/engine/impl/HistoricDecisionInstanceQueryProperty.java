// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface HistoricDecisionInstanceQueryProperty
{
    public static final QueryProperty EVALUATION_TIME = new QueryPropertyImpl("EVAL_TIME_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
