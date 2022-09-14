// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.authorization.MissingAuthorization;
import java.util.List;

public class AuthorizationException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    protected final String userId;
    protected final List<MissingAuthorization> missingAuthorizations;
    @Deprecated
    protected String resourceType;
    @Deprecated
    protected String permissionName;
    @Deprecated
    protected String resourceId;
    
    public AuthorizationException(final String message) {
        super(message);
        this.userId = null;
        this.missingAuthorizations = new ArrayList<MissingAuthorization>();
    }
    
    public AuthorizationException(final String userId, final String permissionName, final String resourceType, final String resourceId) {
        this(userId, new MissingAuthorization(permissionName, resourceType, resourceId));
    }
    
    public AuthorizationException(final String userId, final MissingAuthorization exceptionInfo) {
        super("The user with id '" + userId + "' does not have " + generateMissingAuthorizationMessage(exceptionInfo) + ".");
        this.userId = userId;
        (this.missingAuthorizations = new ArrayList<MissingAuthorization>()).add(exceptionInfo);
        this.resourceType = exceptionInfo.getResourceType();
        this.permissionName = exceptionInfo.getViolatedPermissionName();
        this.resourceId = exceptionInfo.getResourceId();
    }
    
    public AuthorizationException(final String userId, final List<MissingAuthorization> info) {
        super(generateExceptionMessage(userId, info));
        this.userId = userId;
        this.missingAuthorizations = info;
    }
    
    @Deprecated
    public String getResourceType() {
        String resourceType = null;
        if (this.missingAuthorizations.size() == 1) {
            resourceType = this.missingAuthorizations.get(0).getResourceType();
        }
        return resourceType;
    }
    
    @Deprecated
    public String getViolatedPermissionName() {
        if (this.missingAuthorizations.size() == 1) {
            return this.missingAuthorizations.get(0).getViolatedPermissionName();
        }
        return null;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    @Deprecated
    public String getResourceId() {
        if (this.missingAuthorizations.size() == 1) {
            return this.missingAuthorizations.get(0).getResourceId();
        }
        return null;
    }
    
    public List<MissingAuthorization> getMissingAuthorizations() {
        return Collections.unmodifiableList((List<? extends MissingAuthorization>)this.missingAuthorizations);
    }
    
    private static String generateExceptionMessage(final String userId, final List<MissingAuthorization> missingAuthorizations) {
        final StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("The user with id '");
        sBuilder.append(userId);
        sBuilder.append("' does not have one of the following permissions: ");
        sBuilder.append(generateMissingAuthorizationsList(missingAuthorizations));
        return sBuilder.toString();
    }
    
    public static String generateMissingAuthorizationsList(final List<MissingAuthorization> missingAuthorizations) {
        final StringBuilder sBuilder = new StringBuilder();
        boolean first = true;
        for (final MissingAuthorization missingAuthorization : missingAuthorizations) {
            if (!first) {
                sBuilder.append(" or ");
            }
            else {
                first = false;
            }
            sBuilder.append(generateMissingAuthorizationMessage(missingAuthorization));
        }
        return sBuilder.toString();
    }
    
    private static String generateMissingAuthorizationMessage(final MissingAuthorization exceptionInfo) {
        final StringBuilder builder = new StringBuilder();
        final String permissionName = exceptionInfo.getViolatedPermissionName();
        final String resourceType = exceptionInfo.getResourceType();
        final String resourceId = exceptionInfo.getResourceId();
        builder.append("'");
        builder.append(permissionName);
        builder.append("' permission on resource '");
        builder.append((resourceId != null) ? (resourceId + "' of type '") : "");
        builder.append(resourceType);
        builder.append("'");
        return builder.toString();
    }
}
