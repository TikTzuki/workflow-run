// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.query.NativeQuery;

public interface NativeHistoricVariableInstanceQuery extends NativeQuery<NativeHistoricVariableInstanceQuery, HistoricVariableInstance>
{
    NativeHistoricVariableInstanceQuery disableCustomObjectDeserialization();
}
