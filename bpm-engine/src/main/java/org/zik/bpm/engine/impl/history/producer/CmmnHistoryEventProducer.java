// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.producer;

import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;

public interface CmmnHistoryEventProducer
{
    HistoryEvent createCaseInstanceCreateEvt(final DelegateCaseExecution p0);
    
    HistoryEvent createCaseInstanceUpdateEvt(final DelegateCaseExecution p0);
    
    HistoryEvent createCaseInstanceCloseEvt(final DelegateCaseExecution p0);
    
    HistoryEvent createCaseActivityInstanceCreateEvt(final DelegateCaseExecution p0);
    
    HistoryEvent createCaseActivityInstanceUpdateEvt(final DelegateCaseExecution p0);
    
    HistoryEvent createCaseActivityInstanceEndEvt(final DelegateCaseExecution p0);
}
