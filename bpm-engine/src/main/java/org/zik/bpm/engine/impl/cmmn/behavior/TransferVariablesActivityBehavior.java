// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.delegate.VariableScope;

public interface TransferVariablesActivityBehavior extends CmmnActivityBehavior
{
    void transferVariables(final VariableScope p0, final CmmnActivityExecution p1);
}
