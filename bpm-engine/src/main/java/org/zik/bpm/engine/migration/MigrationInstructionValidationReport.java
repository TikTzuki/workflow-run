// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.migration;

import java.util.List;

public interface MigrationInstructionValidationReport
{
    MigrationInstruction getMigrationInstruction();
    
    boolean hasFailures();
    
    List<String> getFailures();
}
