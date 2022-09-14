// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.repository;

import java.util.ArrayList;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.List;
import org.zik.bpm.engine.repository.CalledProcessDefinition;

public class CalledProcessDefinitionImpl implements CalledProcessDefinition
{
    protected String id;
    protected String key;
    protected String category;
    protected String description;
    protected String name;
    protected int version;
    protected String deploymentId;
    protected boolean suspended;
    protected String tenantId;
    protected String versionTag;
    protected Integer historyTimeToLive;
    protected boolean isStartableInTasklist;
    protected boolean hasStartFormKey;
    protected String diagramResourceName;
    protected String resourceName;
    protected List<String> calledFromActivityIds;
    protected String callingProcessDefinitionId;
    
    public CalledProcessDefinitionImpl(final ProcessDefinition definition, final String callingProcessDefinitionId) {
        this.calledFromActivityIds = new ArrayList<String>();
        this.callingProcessDefinitionId = callingProcessDefinitionId;
        this.id = definition.getId();
        this.key = definition.getKey();
        this.category = definition.getCategory();
        this.description = definition.getDescription();
        this.name = definition.getName();
        this.version = definition.getVersion();
        this.deploymentId = definition.getDeploymentId();
        this.suspended = definition.isSuspended();
        this.tenantId = definition.getTenantId();
        this.versionTag = definition.getVersionTag();
        this.historyTimeToLive = definition.getHistoryTimeToLive();
        this.isStartableInTasklist = definition.isStartableInTasklist();
        this.hasStartFormKey = definition.hasStartFormKey();
        this.diagramResourceName = definition.getDiagramResourceName();
        this.resourceName = definition.getResourceName();
    }
    
    @Override
    public String getCallingProcessDefinitionId() {
        return this.callingProcessDefinitionId;
    }
    
    @Override
    public List<String> getCalledFromActivityIds() {
        return this.calledFromActivityIds;
    }
    
    public void addCallingCallActivity(final String activityId) {
        this.calledFromActivityIds.add(activityId);
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public String getKey() {
        return this.key;
    }
    
    @Override
    public String getCategory() {
        return this.category;
    }
    
    @Override
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public boolean hasStartFormKey() {
        return this.hasStartFormKey;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public String getResourceName() {
        return this.resourceName;
    }
    
    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    @Override
    public String getDiagramResourceName() {
        return this.diagramResourceName;
    }
    
    @Override
    public boolean isSuspended() {
        return this.suspended;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    @Override
    public String getVersionTag() {
        return this.versionTag;
    }
    
    @Override
    public Integer getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }
    
    @Override
    public boolean isStartableInTasklist() {
        return this.isStartableInTasklist;
    }
}
