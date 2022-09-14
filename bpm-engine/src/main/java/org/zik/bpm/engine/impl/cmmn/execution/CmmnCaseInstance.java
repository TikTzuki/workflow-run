// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.execution;

import java.util.Map;

public interface CmmnCaseInstance extends CmmnActivityExecution
{
    void create();
    
    void create(final Map<String, Object> p0);
    
    CmmnActivityExecution findCaseExecution(final String p0);
}
