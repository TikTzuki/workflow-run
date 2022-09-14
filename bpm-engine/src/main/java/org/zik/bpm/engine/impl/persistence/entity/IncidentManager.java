// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.Page;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.impl.IncidentQueryImpl;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class IncidentManager extends AbstractManager
{
    public List<IncidentEntity> findIncidentsByExecution(final String id) {
        return (List<IncidentEntity>)this.getDbEntityManager().selectList("selectIncidentsByExecutionId", id);
    }
    
    public List<IncidentEntity> findIncidentsByProcessInstance(final String id) {
        return (List<IncidentEntity>)this.getDbEntityManager().selectList("selectIncidentsByProcessInstanceId", id);
    }
    
    public long findIncidentCountByQueryCriteria(final IncidentQueryImpl incidentQuery) {
        this.configureQuery(incidentQuery);
        return (long)this.getDbEntityManager().selectOne("selectIncidentCountByQueryCriteria", incidentQuery);
    }
    
    public Incident findIncidentById(final String id) {
        return this.getDbEntityManager().selectById(IncidentEntity.class, id);
    }
    
    public List<Incident> findIncidentByConfiguration(final String configuration) {
        return this.findIncidentByConfigurationAndIncidentType(configuration, null);
    }
    
    public List<Incident> findIncidentByConfigurationAndIncidentType(final String configuration, final String incidentType) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("configuration", configuration);
        params.put("incidentType", incidentType);
        return (List<Incident>)this.getDbEntityManager().selectList("selectIncidentsByConfiguration", params);
    }
    
    public List<Incident> findIncidentByQueryCriteria(final IncidentQueryImpl incidentQuery, final Page page) {
        this.configureQuery(incidentQuery);
        return (List<Incident>)this.getDbEntityManager().selectList("selectIncidentByQueryCriteria", incidentQuery, page);
    }
    
    protected void configureQuery(final IncidentQueryImpl query) {
        this.getAuthorizationManager().configureIncidentQuery(query);
        this.getTenantManager().configureQuery(query);
    }
}
