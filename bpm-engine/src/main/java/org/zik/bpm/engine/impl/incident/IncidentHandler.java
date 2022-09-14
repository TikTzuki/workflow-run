// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.incident;

import org.zik.bpm.engine.runtime.Incident;

public interface IncidentHandler
{
    String getIncidentHandlerType();
    
    Incident handleIncident(final IncidentContext p0, final String p1);
    
    void resolveIncident(final IncidentContext p0);
    
    void deleteIncident(final IncidentContext p0);
}
