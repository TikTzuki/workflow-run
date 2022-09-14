// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public interface CmmnCompositeActivityBehavior extends CmmnActivityBehavior
{
    void handleChildDisabled(final CmmnActivityExecution p0, final CmmnActivityExecution p1);
    
    void handleChildTermination(final CmmnActivityExecution p0, final CmmnActivityExecution p1);
    
    void handleChildSuspension(final CmmnActivityExecution p0, final CmmnActivityExecution p1);
    
    void handleChildCompletion(final CmmnActivityExecution p0, final CmmnActivityExecution p1);
}
