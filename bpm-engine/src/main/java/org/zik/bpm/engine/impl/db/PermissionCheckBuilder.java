// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resource;
import java.util.ArrayList;
import java.util.List;

public class PermissionCheckBuilder
{
    protected List<PermissionCheck> atomicChecks;
    protected List<CompositePermissionCheck> compositeChecks;
    protected boolean disjunctive;
    protected PermissionCheckBuilder parent;
    
    public PermissionCheckBuilder() {
        this.atomicChecks = new ArrayList<PermissionCheck>();
        this.compositeChecks = new ArrayList<CompositePermissionCheck>();
        this.disjunctive = true;
    }
    
    public PermissionCheckBuilder(final PermissionCheckBuilder parent) {
        this.atomicChecks = new ArrayList<PermissionCheck>();
        this.compositeChecks = new ArrayList<CompositePermissionCheck>();
        this.disjunctive = true;
        this.parent = parent;
    }
    
    public PermissionCheckBuilder disjunctive() {
        this.disjunctive = true;
        return this;
    }
    
    public PermissionCheckBuilder conjunctive() {
        this.disjunctive = false;
        return this;
    }
    
    public PermissionCheckBuilder atomicCheck(final Resource resource, final String queryParam, final Permission permission) {
        if (!this.isPermissionDisabled(permission)) {
            final PermissionCheck permCheck = new PermissionCheck();
            permCheck.setResource(resource);
            permCheck.setResourceIdQueryParam(queryParam);
            permCheck.setPermission(permission);
            this.atomicChecks.add(permCheck);
        }
        return this;
    }
    
    public PermissionCheckBuilder atomicCheckForResourceId(final Resource resource, final String resourceId, final Permission permission) {
        if (!this.isPermissionDisabled(permission)) {
            final PermissionCheck permCheck = new PermissionCheck();
            permCheck.setResource(resource);
            permCheck.setResourceId(resourceId);
            permCheck.setPermission(permission);
            this.atomicChecks.add(permCheck);
        }
        return this;
    }
    
    public PermissionCheckBuilder composite() {
        return new PermissionCheckBuilder(this);
    }
    
    public PermissionCheckBuilder done() {
        this.parent.compositeChecks.add(this.build());
        return this.parent;
    }
    
    public CompositePermissionCheck build() {
        this.validate();
        final CompositePermissionCheck permissionCheck = new CompositePermissionCheck(this.disjunctive);
        permissionCheck.setAtomicChecks(this.atomicChecks);
        permissionCheck.setCompositeChecks(this.compositeChecks);
        return permissionCheck;
    }
    
    public List<PermissionCheck> getAtomicChecks() {
        return this.atomicChecks;
    }
    
    protected void validate() {
        if (!this.atomicChecks.isEmpty() && !this.compositeChecks.isEmpty()) {
            throw new ProcessEngineException("Mixed authorization checks of atomic and composite permissions are not supported");
        }
    }
    
    public boolean isPermissionDisabled(final Permission permission) {
        final AuthorizationManager authorizationManager = Context.getCommandContext().getAuthorizationManager();
        return authorizationManager.isPermissionDisabled(permission);
    }
}
