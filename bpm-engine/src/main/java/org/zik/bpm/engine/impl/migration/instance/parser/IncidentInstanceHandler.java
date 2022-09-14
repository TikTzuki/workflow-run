// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import org.zik.bpm.engine.impl.migration.instance.MigratingExternalTaskInstance;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.migration.instance.MigratingJobInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingIncident;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;

public class IncidentInstanceHandler implements MigratingInstanceParseHandler<IncidentEntity>
{
    @Override
    public void handle(final MigratingInstanceParseContext parseContext, final IncidentEntity incident) {
        if (incident.getConfiguration() != null && this.isFailedJobIncident(incident)) {
            this.handleFailedJobIncident(parseContext, incident);
        }
        else if (incident.getConfiguration() != null && this.isExternalTaskIncident(incident)) {
            this.handleExternalTaskIncident(parseContext, incident);
        }
        else {
            this.handleIncident(parseContext, incident);
        }
    }
    
    protected void handleIncident(final MigratingInstanceParseContext parseContext, final IncidentEntity incident) {
        final MigratingActivityInstance owningInstance = parseContext.getMigratingActivityInstanceById(incident.getExecution().getActivityInstanceId());
        if (owningInstance != null) {
            parseContext.consume(incident);
            final MigratingIncident migratingIncident = new MigratingIncident(incident, owningInstance.getTargetScope());
            owningInstance.addMigratingDependentInstance(migratingIncident);
        }
    }
    
    protected boolean isFailedJobIncident(final IncidentEntity incident) {
        return "failedJob".equals(incident.getIncidentType());
    }
    
    protected void handleFailedJobIncident(final MigratingInstanceParseContext parseContext, final IncidentEntity incident) {
        final MigratingJobInstance owningInstance = parseContext.getMigratingJobInstanceById(incident.getConfiguration());
        if (owningInstance != null) {
            parseContext.consume(incident);
            if (owningInstance.migrates()) {
                final MigratingIncident migratingIncident = new MigratingIncident(incident, owningInstance.getTargetScope());
                final JobDefinitionEntity targetJobDefinitionEntity = owningInstance.getTargetJobDefinitionEntity();
                if (targetJobDefinitionEntity != null) {
                    migratingIncident.setTargetJobDefinitionId(targetJobDefinitionEntity.getId());
                }
                owningInstance.addMigratingDependentInstance(migratingIncident);
            }
        }
    }
    
    protected boolean isExternalTaskIncident(final IncidentEntity incident) {
        return "failedExternalTask".equals(incident.getIncidentType());
    }
    
    protected void handleExternalTaskIncident(final MigratingInstanceParseContext parseContext, final IncidentEntity incident) {
        final MigratingExternalTaskInstance owningInstance = parseContext.getMigratingExternalTaskInstanceById(incident.getConfiguration());
        if (owningInstance != null) {
            parseContext.consume(incident);
            final MigratingIncident migratingIncident = new MigratingIncident(incident, owningInstance.getTargetScope());
            owningInstance.addMigratingDependentInstance(migratingIncident);
        }
    }
}
