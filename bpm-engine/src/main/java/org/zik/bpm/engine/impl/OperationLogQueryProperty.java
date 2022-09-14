// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public class OperationLogQueryProperty
{
    public static final QueryProperty TIMESTAMP;
    
    static {
        TIMESTAMP = new QueryPropertyImpl("TIMESTAMP_");
    }
}
