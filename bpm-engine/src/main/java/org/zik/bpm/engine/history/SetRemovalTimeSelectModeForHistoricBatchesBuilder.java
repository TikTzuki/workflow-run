// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface SetRemovalTimeSelectModeForHistoricBatchesBuilder extends SetRemovalTimeToHistoricBatchesBuilder
{
    SetRemovalTimeToHistoricBatchesBuilder absoluteRemovalTime(final Date p0);
    
    SetRemovalTimeToHistoricBatchesBuilder calculatedRemovalTime();
    
    SetRemovalTimeToHistoricBatchesBuilder clearedRemovalTime();
}
