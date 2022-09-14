// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;

public interface CmmnModelExecutionContext
{
    CmmnModelInstance getCmmnModelInstance();
    
    CmmnElement getCmmnModelElementInstance();
}
