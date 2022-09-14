// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.incident;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.runtime.Incident;

public class IncidentHandling
{
    public static Incident createIncident(final String incidentType, final IncidentContext context, final String message) {
        IncidentHandler handler = Context.getProcessEngineConfiguration().getIncidentHandler(incidentType);
        if (handler == null) {
            handler = new DefaultIncidentHandler(incidentType);
        }
        return handler.handleIncident(context, message);
    }
    
    public static void removeIncidents(final String incidentType, final IncidentContext context, final boolean incidentsResolved) {
        IncidentHandler handler = Context.getProcessEngineConfiguration().getIncidentHandler(incidentType);
        if (handler == null) {
            handler = new DefaultIncidentHandler(incidentType);
        }
        if (incidentsResolved) {
            handler.resolveIncident(context);
        }
        else {
            handler.deleteIncident(context);
        }
    }
}
