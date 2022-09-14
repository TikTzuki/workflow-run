// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm;

import org.zik.bpm.engine.ProcessEngine;

import java.util.List;
import java.util.Set;

public interface ProcessEngineService {
    ProcessEngine getDefaultProcessEngine();

    List<ProcessEngine> getProcessEngines();

    Set<String> getProcessEngineNames();

    ProcessEngine getProcessEngine(final String p0);
}
