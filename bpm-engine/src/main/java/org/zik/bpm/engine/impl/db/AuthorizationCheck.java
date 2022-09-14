// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.authorization.Permissions;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class AuthorizationCheck implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected boolean isAuthorizationCheckEnabled;
    protected boolean shouldPerformAuthorizatioCheck;
    protected boolean isRevokeAuthorizationCheckEnabled;
    protected String authUserId;
    protected List<String> authGroupIds;
    protected int authDefaultPerm;
    protected CompositePermissionCheck permissionChecks;
    protected boolean historicInstancePermissionsEnabled;
    protected boolean useLeftJoin;
    
    public AuthorizationCheck() {
        this.isAuthorizationCheckEnabled = false;
        this.shouldPerformAuthorizatioCheck = false;
        this.isRevokeAuthorizationCheckEnabled = false;
        this.authGroupIds = new ArrayList<String>();
        this.authDefaultPerm = Permissions.ALL.getValue();
        this.permissionChecks = new CompositePermissionCheck();
        this.historicInstancePermissionsEnabled = false;
        this.useLeftJoin = true;
    }
    
    public AuthorizationCheck(final String authUserId, final List<String> authGroupIds, final CompositePermissionCheck permissionCheck, final boolean isRevokeAuthorizationCheckEnabled) {
        this.isAuthorizationCheckEnabled = false;
        this.shouldPerformAuthorizatioCheck = false;
        this.isRevokeAuthorizationCheckEnabled = false;
        this.authGroupIds = new ArrayList<String>();
        this.authDefaultPerm = Permissions.ALL.getValue();
        this.permissionChecks = new CompositePermissionCheck();
        this.historicInstancePermissionsEnabled = false;
        this.useLeftJoin = true;
        this.authUserId = authUserId;
        this.authGroupIds = authGroupIds;
        this.permissionChecks = permissionCheck;
        this.isRevokeAuthorizationCheckEnabled = isRevokeAuthorizationCheckEnabled;
    }
    
    public boolean isAuthorizationCheckEnabled() {
        return this.isAuthorizationCheckEnabled;
    }
    
    public boolean getIsAuthorizationCheckEnabled() {
        return this.isAuthorizationCheckEnabled;
    }
    
    public void setAuthorizationCheckEnabled(final boolean isAuthorizationCheckPerformed) {
        this.isAuthorizationCheckEnabled = isAuthorizationCheckPerformed;
    }
    
    public boolean shouldPerformAuthorizatioCheck() {
        return this.shouldPerformAuthorizatioCheck;
    }
    
    public boolean getShouldPerformAuthorizatioCheck() {
        return this.isAuthorizationCheckEnabled && !this.isPermissionChecksEmpty();
    }
    
    public void setShouldPerformAuthorizatioCheck(final boolean shouldPerformAuthorizatioCheck) {
        this.shouldPerformAuthorizatioCheck = shouldPerformAuthorizatioCheck;
    }
    
    protected boolean isPermissionChecksEmpty() {
        return this.permissionChecks.getAtomicChecks().isEmpty() && this.permissionChecks.getCompositeChecks().isEmpty();
    }
    
    public String getAuthUserId() {
        return this.authUserId;
    }
    
    public void setAuthUserId(final String authUserId) {
        this.authUserId = authUserId;
    }
    
    public List<String> getAuthGroupIds() {
        return this.authGroupIds;
    }
    
    public void setAuthGroupIds(final List<String> authGroupIds) {
        this.authGroupIds = authGroupIds;
    }
    
    public int getAuthDefaultPerm() {
        return this.authDefaultPerm;
    }
    
    public void setAuthDefaultPerm(final int authDefaultPerm) {
        this.authDefaultPerm = authDefaultPerm;
    }
    
    public CompositePermissionCheck getPermissionChecks() {
        return this.permissionChecks;
    }
    
    public void setAtomicPermissionChecks(final List<PermissionCheck> permissionChecks) {
        this.permissionChecks.setAtomicChecks(permissionChecks);
    }
    
    public void addAtomicPermissionCheck(final PermissionCheck permissionCheck) {
        this.permissionChecks.addAtomicCheck(permissionCheck);
    }
    
    public void setPermissionChecks(final CompositePermissionCheck permissionChecks) {
        this.permissionChecks = permissionChecks;
    }
    
    public boolean isRevokeAuthorizationCheckEnabled() {
        return this.isRevokeAuthorizationCheckEnabled;
    }
    
    public void setRevokeAuthorizationCheckEnabled(final boolean isRevokeAuthorizationCheckEnabled) {
        this.isRevokeAuthorizationCheckEnabled = isRevokeAuthorizationCheckEnabled;
    }
    
    public void setHistoricInstancePermissionsEnabled(final boolean historicInstancePermissionsEnabled) {
        this.historicInstancePermissionsEnabled = historicInstancePermissionsEnabled;
    }
    
    public boolean isHistoricInstancePermissionsEnabled() {
        return this.historicInstancePermissionsEnabled;
    }
    
    public boolean isUseLeftJoin() {
        return this.useLeftJoin;
    }
    
    public void setUseLeftJoin(final boolean useLeftJoin) {
        this.useLeftJoin = useLeftJoin;
    }
}
