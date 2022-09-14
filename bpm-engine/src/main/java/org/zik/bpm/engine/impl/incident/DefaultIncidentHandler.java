// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.incident;

import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.zik.bpm.engine.runtime.Incident;

public class DefaultIncidentHandler implements IncidentHandler
{
    protected String type;
    
    public DefaultIncidentHandler(final String type) {
        this.type = type;
    }
    
    @Override
    public String getIncidentHandlerType() {
        return this.type;
    }
    
    @Override
    public Incident handleIncident(final IncidentContext context, final String message) {
        return this.createIncident(context, message);
    }
    
    public Incident createIncident(final IncidentContext context, final String message) {
        final IncidentEntity newIncident = IncidentEntity.createAndInsertIncident(this.type, context, message);
        if (context.getExecutionId() != null) {
            newIncident.createRecursiveIncidents();
        }
        return newIncident;
    }
    
    @Override
    public void resolveIncident(final IncidentContext context) {
        this.removeIncident(context, true);
    }
    
    @Override
    public void deleteIncident(final IncidentContext context) {
        this.removeIncident(context, false);
    }
    
    protected void removeIncident(final IncidentContext context, final boolean incidentResolved) {
        final List<Incident> incidents = Context.getCommandContext().getIncidentManager().findIncidentByConfiguration(context.getConfiguration());
        for (final Incident currentIncident : incidents) {
            final IncidentEntity incident = (IncidentEntity)currentIncident;
            if (incidentResolved) {
                incident.resolve();
            }
            else {
                incident.delete();
            }
        }
    }
}
