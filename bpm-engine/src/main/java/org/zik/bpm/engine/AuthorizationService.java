// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import java.util.List;
import org.zik.bpm.engine.authorization.AuthorizationQuery;
import org.zik.bpm.engine.authorization.Authorization;

public interface AuthorizationService
{
    Authorization createNewAuthorization(final int p0);
    
    Authorization saveAuthorization(final Authorization p0);
    
    void deleteAuthorization(final String p0);
    
    AuthorizationQuery createAuthorizationQuery();
    
    boolean isUserAuthorized(final String p0, final List<String> p1, final Permission p2, final Resource p3);
    
    boolean isUserAuthorized(final String p0, final List<String> p1, final Permission p2, final Resource p3, final String p4);
}
