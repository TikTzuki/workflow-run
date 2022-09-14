// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface SetRemovalTimeSelectModeForHistoricDecisionInstancesBuilder extends SetRemovalTimeToHistoricDecisionInstancesBuilder
{
    SetRemovalTimeToHistoricDecisionInstancesBuilder absoluteRemovalTime(final Date p0);
    
    SetRemovalTimeToHistoricDecisionInstancesBuilder calculatedRemovalTime();
    
    SetRemovalTimeToHistoricDecisionInstancesBuilder clearedRemovalTime();
}
