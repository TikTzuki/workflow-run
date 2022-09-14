// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.incident;

import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Iterator;
import java.util.Arrays;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.ArrayList;
import java.util.List;

public class CompositeIncidentHandler implements IncidentHandler
{
    protected IncidentHandler mainIncidentHandler;
    protected final List<IncidentHandler> incidentHandlers;
    
    public CompositeIncidentHandler(final IncidentHandler mainIncidentHandler, final List<IncidentHandler> incidentHandlers) {
        this.incidentHandlers = new ArrayList<IncidentHandler>();
        this.initializeIncidentsHandlers(mainIncidentHandler, incidentHandlers);
    }
    
    public CompositeIncidentHandler(final IncidentHandler mainIncidentHandler, final IncidentHandler... incidentHandlers) {
        this.incidentHandlers = new ArrayList<IncidentHandler>();
        EnsureUtil.ensureNotNull("Incident handlers", (Object[])incidentHandlers);
        this.initializeIncidentsHandlers(mainIncidentHandler, Arrays.asList(incidentHandlers));
    }
    
    protected void initializeIncidentsHandlers(final IncidentHandler mainIncidentHandler, final List<IncidentHandler> incidentHandlers) {
        EnsureUtil.ensureNotNull("Incident handler", mainIncidentHandler);
        this.mainIncidentHandler = mainIncidentHandler;
        EnsureUtil.ensureNotNull("Incident handlers", incidentHandlers);
        for (final IncidentHandler incidentHandler : incidentHandlers) {
            this.add(incidentHandler);
        }
    }
    
    public void add(final IncidentHandler incidentHandler) {
        EnsureUtil.ensureNotNull("Incident handler", incidentHandler);
        final String incidentHandlerType = this.getIncidentHandlerType();
        if (!incidentHandlerType.equals(incidentHandler.getIncidentHandlerType())) {
            throw new ProcessEngineException("Incorrect incident type handler in composite handler with type: " + incidentHandlerType);
        }
        this.incidentHandlers.add(incidentHandler);
    }
    
    @Override
    public String getIncidentHandlerType() {
        return this.mainIncidentHandler.getIncidentHandlerType();
    }
    
    @Override
    public Incident handleIncident(final IncidentContext context, final String message) {
        final Incident incident = this.mainIncidentHandler.handleIncident(context, message);
        for (final IncidentHandler incidentHandler : this.incidentHandlers) {
            incidentHandler.handleIncident(context, message);
        }
        return incident;
    }
    
    @Override
    public void resolveIncident(final IncidentContext context) {
        this.mainIncidentHandler.resolveIncident(context);
        for (final IncidentHandler incidentHandler : this.incidentHandlers) {
            incidentHandler.resolveIncident(context);
        }
    }
    
    @Override
    public void deleteIncident(final IncidentContext context) {
        this.mainIncidentHandler.deleteIncident(context);
        for (final IncidentHandler incidentHandler : this.incidentHandlers) {
            incidentHandler.deleteIncident(context);
        }
    }
}
