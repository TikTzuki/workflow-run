// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.batch.Batch;
import java.util.List;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;

public interface RestartProcessInstanceBuilder extends InstantiationBuilder<RestartProcessInstanceBuilder>
{
    RestartProcessInstanceBuilder historicProcessInstanceQuery(final HistoricProcessInstanceQuery p0);
    
    RestartProcessInstanceBuilder processInstanceIds(final String... p0);
    
    RestartProcessInstanceBuilder processInstanceIds(final List<String> p0);
    
    RestartProcessInstanceBuilder initialSetOfVariables();
    
    RestartProcessInstanceBuilder withoutBusinessKey();
    
    RestartProcessInstanceBuilder skipCustomListeners();
    
    RestartProcessInstanceBuilder skipIoMappings();
    
    void execute();
    
    Batch executeAsync();
}
