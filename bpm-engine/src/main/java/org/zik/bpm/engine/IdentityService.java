// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.impl.identity.Account;
import java.util.Map;
import org.zik.bpm.engine.identity.Picture;
import org.zik.bpm.engine.impl.identity.Authentication;
import java.util.List;
import org.zik.bpm.engine.identity.PasswordPolicy;
import org.zik.bpm.engine.identity.PasswordPolicyResult;
import org.zik.bpm.engine.identity.TenantQuery;
import org.zik.bpm.engine.identity.Tenant;
import org.zik.bpm.engine.identity.GroupQuery;
import org.zik.bpm.engine.identity.NativeUserQuery;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.identity.UserQuery;
import org.zik.bpm.engine.identity.User;

public interface IdentityService
{
    boolean isReadOnly();
    
    User newUser(final String p0);
    
    void saveUser(final User p0);
    
    UserQuery createUserQuery();
    
    void deleteUser(final String p0);
    
    void unlockUser(final String p0);
    
    Group newGroup(final String p0);
    
    NativeUserQuery createNativeUserQuery();
    
    GroupQuery createGroupQuery();
    
    void saveGroup(final Group p0);
    
    void deleteGroup(final String p0);
    
    void createMembership(final String p0, final String p1);
    
    void deleteMembership(final String p0, final String p1);
    
    Tenant newTenant(final String p0);
    
    TenantQuery createTenantQuery();
    
    void saveTenant(final Tenant p0);
    
    void deleteTenant(final String p0);
    
    void createTenantUserMembership(final String p0, final String p1);
    
    void createTenantGroupMembership(final String p0, final String p1);
    
    void deleteTenantUserMembership(final String p0, final String p1);
    
    void deleteTenantGroupMembership(final String p0, final String p1);
    
    boolean checkPassword(final String p0, final String p1);
    
    PasswordPolicyResult checkPasswordAgainstPolicy(final String p0);
    
    PasswordPolicyResult checkPasswordAgainstPolicy(final String p0, final User p1);
    
    PasswordPolicyResult checkPasswordAgainstPolicy(final PasswordPolicy p0, final String p1);
    
    PasswordPolicyResult checkPasswordAgainstPolicy(final PasswordPolicy p0, final String p1, final User p2);
    
    PasswordPolicy getPasswordPolicy();
    
    void setAuthenticatedUserId(final String p0);
    
    void setAuthentication(final String p0, final List<String> p1);
    
    void setAuthentication(final String p0, final List<String> p1, final List<String> p2);
    
    void setAuthentication(final Authentication p0);
    
    Authentication getCurrentAuthentication();
    
    void clearAuthentication();
    
    void setUserPicture(final String p0, final Picture p1);
    
    Picture getUserPicture(final String p0);
    
    void deleteUserPicture(final String p0);
    
    void setUserInfo(final String p0, final String p1, final String p2);
    
    String getUserInfo(final String p0, final String p1);
    
    List<String> getUserInfoKeys(final String p0);
    
    void deleteUserInfo(final String p0, final String p1);
    
    @Deprecated
    void setUserAccount(final String p0, final String p1, final String p2, final String p3, final String p4, final Map<String, String> p5);
    
    @Deprecated
    List<String> getUserAccountNames(final String p0);
    
    @Deprecated
    Account getUserAccount(final String p0, final String p1, final String p2);
    
    @Deprecated
    void deleteUserAccount(final String p0, final String p1);
}
