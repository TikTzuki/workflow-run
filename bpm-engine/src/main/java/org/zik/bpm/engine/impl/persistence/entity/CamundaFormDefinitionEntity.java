// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.repository.ResourceDefinition;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.repository.CamundaFormDefinition;

public class CamundaFormDefinitionEntity implements CamundaFormDefinition, ResourceDefinitionEntity<CamundaFormDefinitionEntity>, DbEntity, HasDbRevision, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int revision;
    protected String key;
    protected int version;
    protected String deploymentId;
    protected String resourceName;
    protected String tenantId;
    
    public CamundaFormDefinitionEntity(final String key, final String deploymentId, final String resourceName, final String tenantId) {
        this.revision = 1;
        this.key = key;
        this.deploymentId = deploymentId;
        this.resourceName = resourceName;
        this.tenantId = tenantId;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    @Override
    public String getKey() {
        return this.key;
    }
    
    @Override
    public void setKey(final String key) {
        this.key = key;
    }
    
    @Override
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public void setVersion(final int version) {
        this.version = version;
    }
    
    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    @Override
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    @Override
    public String getResourceName() {
        return this.resourceName;
    }
    
    @Override
    public void setResourceName(final String resourceName) {
        this.resourceName = resourceName;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    @Override
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getCategory() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public CamundaFormDefinitionEntity getPreviousDefinition() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setCategory(final String category) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getDiagramResourceName() {
        throw new UnsupportedOperationException("deployment of diagrams not supported for Camunda Forms");
    }
    
    @Override
    public void setDiagramResourceName(final String diagramResourceName) {
        throw new UnsupportedOperationException("deployment of diagrams not supported for Camunda Forms");
    }
    
    @Override
    public Integer getHistoryTimeToLive() {
        throw new UnsupportedOperationException("history time to live not supported for Camunda Forms");
    }
    
    @Override
    public void setHistoryTimeToLive(final Integer historyTimeToLive) {
        throw new UnsupportedOperationException("history time to live not supported for Camunda Forms");
    }
    
    @Override
    public Object getPersistentState() {
        return CamundaFormDefinitionEntity.class;
    }
    
    @Override
    public void updateModifiableFieldsFromEntity(final CamundaFormDefinitionEntity updatingDefinition) {
        throw new UnsupportedOperationException("properties of Camunda Form Definitions are immutable");
    }
    
    @Override
    public String getName() {
        throw new UnsupportedOperationException("name property not supported for Camunda Forms");
    }
    
    @Override
    public void setName(final String name) {
        throw new UnsupportedOperationException("name property not supported for Camunda Forms");
    }
}
