// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm;

import org.zik.bpm.application.ProcessApplicationInfo;

import java.util.Set;

public interface ProcessApplicationService {
    Set<String> getProcessApplicationNames();

    ProcessApplicationInfo getProcessApplicationInfo(final String p0);
}
