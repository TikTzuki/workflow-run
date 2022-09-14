// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface SetRemovalTimeSelectModeForHistoricProcessInstancesBuilder extends SetRemovalTimeToHistoricProcessInstancesBuilder
{
    SetRemovalTimeToHistoricProcessInstancesBuilder absoluteRemovalTime(final Date p0);
    
    SetRemovalTimeToHistoricProcessInstancesBuilder calculatedRemovalTime();
    
    SetRemovalTimeToHistoricProcessInstancesBuilder clearedRemovalTime();
}
