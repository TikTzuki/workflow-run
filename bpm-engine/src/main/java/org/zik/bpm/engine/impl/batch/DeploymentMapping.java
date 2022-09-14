// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.List;

public class DeploymentMapping
{
    protected static String NULL_ID;
    protected String deploymentId;
    protected int count;
    
    public DeploymentMapping(final String deploymentId, final int count) {
        this.deploymentId = ((deploymentId == null) ? DeploymentMapping.NULL_ID : deploymentId);
        this.count = count;
    }
    
    public String getDeploymentId() {
        return DeploymentMapping.NULL_ID.equals(this.deploymentId) ? null : this.deploymentId;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public List<String> getIds(final List<String> ids) {
        return ids.subList(0, this.count);
    }
    
    public void removeIds(final int numberOfIds) {
        this.count -= numberOfIds;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(";").add(this.deploymentId).add(String.valueOf(this.count)).toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.count, this.deploymentId);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DeploymentMapping)) {
            return false;
        }
        final DeploymentMapping other = (DeploymentMapping)obj;
        return this.count == other.count && Objects.equals(this.deploymentId, other.deploymentId);
    }
    
    static {
        DeploymentMapping.NULL_ID = "$NULL";
    }
}
