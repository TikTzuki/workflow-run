// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.core.delegate.CoreActivityBehavior;

public interface CmmnActivityBehavior extends CoreActivityBehavior<CmmnActivityExecution>
{
    void onCreate(final CmmnActivityExecution p0);
    
    void created(final CmmnActivityExecution p0);
    
    void onEnable(final CmmnActivityExecution p0);
    
    void onReenable(final CmmnActivityExecution p0);
    
    void onDisable(final CmmnActivityExecution p0);
    
    void onStart(final CmmnActivityExecution p0);
    
    void onManualStart(final CmmnActivityExecution p0);
    
    void started(final CmmnActivityExecution p0);
    
    void onCompletion(final CmmnActivityExecution p0);
    
    void onManualCompletion(final CmmnActivityExecution p0);
    
    void onTermination(final CmmnActivityExecution p0);
    
    void onParentTermination(final CmmnActivityExecution p0);
    
    void onExit(final CmmnActivityExecution p0);
    
    void onOccur(final CmmnActivityExecution p0);
    
    void onSuspension(final CmmnActivityExecution p0);
    
    void onParentSuspension(final CmmnActivityExecution p0);
    
    void onResume(final CmmnActivityExecution p0);
    
    void onParentResume(final CmmnActivityExecution p0);
    
    void resumed(final CmmnActivityExecution p0);
    
    void onReactivation(final CmmnActivityExecution p0);
    
    void reactivated(final CmmnActivityExecution p0);
    
    void onClose(final CmmnActivityExecution p0);
    
    void fireEntryCriteria(final CmmnActivityExecution p0);
    
    void fireExitCriteria(final CmmnActivityExecution p0);
    
    void repeat(final CmmnActivityExecution p0, final String p1);
}
