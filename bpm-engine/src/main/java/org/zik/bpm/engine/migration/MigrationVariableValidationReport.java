// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.migration;

import java.util.List;
import org.camunda.bpm.engine.variable.value.TypedValue;

public interface MigrationVariableValidationReport
{
     <T extends TypedValue> T getTypedValue();
    
    boolean hasFailures();
    
    List<String> getFailures();
}
