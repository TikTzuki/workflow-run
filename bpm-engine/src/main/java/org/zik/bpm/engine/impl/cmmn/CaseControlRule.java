// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn;

import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public interface CaseControlRule
{
    boolean evaluate(final CmmnActivityExecution p0);
}
