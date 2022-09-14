// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.DeleteTenantGroupMembershipCmd;
import org.zik.bpm.engine.impl.cmd.DeleteTenantUserMembershipCmd;
import org.zik.bpm.engine.impl.cmd.CreateTenantGroupMembershipCmd;
import org.zik.bpm.engine.impl.cmd.CreateTenantUserMembershipCmd;
import java.util.Map;
import org.zik.bpm.engine.impl.cmd.GetUserAccountCmd;
import org.zik.bpm.engine.impl.identity.Account;
import org.zik.bpm.engine.impl.cmd.DeleteUserInfoCmd;
import org.zik.bpm.engine.impl.cmd.SetUserInfoCmd;
import org.zik.bpm.engine.impl.cmd.GetUserInfoKeysCmd;
import org.zik.bpm.engine.impl.cmd.GetUserInfoCmd;
import java.util.Collection;
import org.zik.bpm.engine.impl.cmd.DeleteUserPictureCmd;
import org.zik.bpm.engine.impl.cmd.GetUserPictureCmd;
import org.zik.bpm.engine.impl.cmd.SetUserPictureCmd;
import org.zik.bpm.engine.identity.Picture;
import org.zik.bpm.engine.impl.cmd.DeleteTenantCmd;
import org.zik.bpm.engine.impl.cmd.DeleteUserCmd;
import org.zik.bpm.engine.impl.cmd.UnlockUserCmd;
import org.zik.bpm.engine.impl.cmd.GetPasswordPolicyCmd;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.identity.PasswordPolicyResultImpl;
import org.zik.bpm.engine.identity.PasswordPolicyRule;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.identity.PasswordPolicy;
import org.zik.bpm.engine.identity.PasswordPolicyResult;
import org.zik.bpm.engine.impl.cmd.CheckPassword;
import org.zik.bpm.engine.impl.cmd.DeleteMembershipCmd;
import org.zik.bpm.engine.impl.cmd.DeleteGroupCmd;
import org.zik.bpm.engine.impl.cmd.CreateMembershipCmd;
import org.zik.bpm.engine.impl.cmd.CreateTenantQueryCmd;
import org.zik.bpm.engine.identity.TenantQuery;
import org.zik.bpm.engine.impl.cmd.CreateGroupQueryCmd;
import org.zik.bpm.engine.identity.GroupQuery;
import org.zik.bpm.engine.impl.cmd.CreateNativeUserQueryCmd;
import org.zik.bpm.engine.identity.NativeUserQuery;
import org.zik.bpm.engine.impl.cmd.CreateUserQueryCmd;
import org.zik.bpm.engine.identity.UserQuery;
import org.zik.bpm.engine.impl.cmd.SaveTenantCmd;
import org.zik.bpm.engine.impl.cmd.SaveUserCmd;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import org.zik.bpm.engine.impl.cmd.SaveGroupCmd;
import org.zik.bpm.engine.impl.cmd.CreateTenantCmd;
import org.zik.bpm.engine.identity.Tenant;
import org.zik.bpm.engine.impl.cmd.CreateUserCmd;
import org.zik.bpm.engine.identity.User;
import org.zik.bpm.engine.impl.cmd.CreateGroupCmd;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.IsIdentityServiceReadOnlyCmd;
import org.zik.bpm.engine.impl.identity.Authentication;
import org.zik.bpm.engine.IdentityService;

public class IdentityServiceImpl extends ServiceImpl implements IdentityService
{
    private ThreadLocal<Authentication> currentAuthentication;
    
    public IdentityServiceImpl() {
        this.currentAuthentication = new ThreadLocal<Authentication>();
    }
    
    @Override
    public boolean isReadOnly() {
        return this.commandExecutor.execute((Command<Boolean>)new IsIdentityServiceReadOnlyCmd());
    }
    
    @Override
    public Group newGroup(final String groupId) {
        return this.commandExecutor.execute((Command<Group>)new CreateGroupCmd(groupId));
    }
    
    @Override
    public User newUser(final String userId) {
        return this.commandExecutor.execute((Command<User>)new CreateUserCmd(userId));
    }
    
    @Override
    public Tenant newTenant(final String tenantId) {
        return this.commandExecutor.execute((Command<Tenant>)new CreateTenantCmd(tenantId));
    }
    
    @Override
    public void saveGroup(final Group group) {
        try {
            this.commandExecutor.execute((Command<Object>)new SaveGroupCmd(group));
        }
        catch (ProcessEngineException ex) {
            if (ExceptionUtil.checkConstraintViolationException(ex)) {
                throw new BadUserRequestException("The group already exists", ex);
            }
            throw ex;
        }
    }
    
    @Override
    public void saveUser(final User user) {
        this.saveUser(user, false);
    }
    
    public void saveUser(final User user, final boolean skipPasswordPolicy) {
        try {
            this.commandExecutor.execute((Command<Object>)new SaveUserCmd(user, skipPasswordPolicy));
        }
        catch (ProcessEngineException ex) {
            if (ExceptionUtil.checkConstraintViolationException(ex)) {
                throw new BadUserRequestException("The user already exists", ex);
            }
            throw ex;
        }
    }
    
    @Override
    public void saveTenant(final Tenant tenant) {
        try {
            this.commandExecutor.execute((Command<Object>)new SaveTenantCmd(tenant));
        }
        catch (ProcessEngineException ex) {
            if (ExceptionUtil.checkConstraintViolationException(ex)) {
                throw new BadUserRequestException("The tenant already exists", ex);
            }
            throw ex;
        }
    }
    
    @Override
    public UserQuery createUserQuery() {
        return this.commandExecutor.execute((Command<UserQuery>)new CreateUserQueryCmd());
    }
    
    @Override
    public NativeUserQuery createNativeUserQuery() {
        return this.commandExecutor.execute((Command<NativeUserQuery>)new CreateNativeUserQueryCmd());
    }
    
    @Override
    public GroupQuery createGroupQuery() {
        return this.commandExecutor.execute((Command<GroupQuery>)new CreateGroupQueryCmd());
    }
    
    @Override
    public TenantQuery createTenantQuery() {
        return this.commandExecutor.execute((Command<TenantQuery>)new CreateTenantQueryCmd());
    }
    
    @Override
    public void createMembership(final String userId, final String groupId) {
        this.commandExecutor.execute((Command<Object>)new CreateMembershipCmd(userId, groupId));
    }
    
    @Override
    public void deleteGroup(final String groupId) {
        this.commandExecutor.execute((Command<Object>)new DeleteGroupCmd(groupId));
    }
    
    @Override
    public void deleteMembership(final String userId, final String groupId) {
        this.commandExecutor.execute((Command<Object>)new DeleteMembershipCmd(userId, groupId));
    }
    
    @Override
    public boolean checkPassword(final String userId, final String password) {
        return this.commandExecutor.execute((Command<Boolean>)new CheckPassword(userId, password));
    }
    
    @Override
    public PasswordPolicyResult checkPasswordAgainstPolicy(final String candidatePassword, final User user) {
        return this.checkPasswordAgainstPolicy(this.getPasswordPolicy(), candidatePassword, user);
    }
    
    @Override
    public PasswordPolicyResult checkPasswordAgainstPolicy(final String password) {
        return this.checkPasswordAgainstPolicy(this.getPasswordPolicy(), password, null);
    }
    
    @Override
    public PasswordPolicyResult checkPasswordAgainstPolicy(final PasswordPolicy policy, final String candidatePassword, final User user) {
        EnsureUtil.ensureNotNull("policy", policy);
        EnsureUtil.ensureNotNull("password", (Object)candidatePassword);
        final List<PasswordPolicyRule> violatedRules = new ArrayList<PasswordPolicyRule>();
        final List<PasswordPolicyRule> fulfilledRules = new ArrayList<PasswordPolicyRule>();
        for (final PasswordPolicyRule rule : policy.getRules()) {
            if (rule.execute(candidatePassword, user)) {
                fulfilledRules.add(rule);
            }
            else {
                violatedRules.add(rule);
            }
        }
        return new PasswordPolicyResultImpl(violatedRules, fulfilledRules);
    }
    
    @Override
    public PasswordPolicyResult checkPasswordAgainstPolicy(final PasswordPolicy policy, final String password) {
        return this.checkPasswordAgainstPolicy(policy, password, null);
    }
    
    @Override
    public PasswordPolicy getPasswordPolicy() {
        return this.commandExecutor.execute((Command<PasswordPolicy>)new GetPasswordPolicyCmd());
    }
    
    @Override
    public void unlockUser(final String userId) {
        this.commandExecutor.execute((Command<Object>)new UnlockUserCmd(userId));
    }
    
    @Override
    public void deleteUser(final String userId) {
        this.commandExecutor.execute((Command<Object>)new DeleteUserCmd(userId));
    }
    
    @Override
    public void deleteTenant(final String tenantId) {
        this.commandExecutor.execute((Command<Object>)new DeleteTenantCmd(tenantId));
    }
    
    @Override
    public void setUserPicture(final String userId, final Picture picture) {
        this.commandExecutor.execute((Command<Object>)new SetUserPictureCmd(userId, picture));
    }
    
    @Override
    public Picture getUserPicture(final String userId) {
        return this.commandExecutor.execute((Command<Picture>)new GetUserPictureCmd(userId));
    }
    
    @Override
    public void deleteUserPicture(final String userId) {
        this.commandExecutor.execute((Command<Object>)new DeleteUserPictureCmd(userId));
    }
    
    @Override
    public void setAuthenticatedUserId(final String authenticatedUserId) {
        this.setAuthentication(new Authentication(authenticatedUserId, null));
    }
    
    @Override
    public void setAuthentication(final Authentication auth) {
        if (auth == null) {
            this.clearAuthentication();
        }
        else {
            if (auth.getUserId() != null) {
                EnsureUtil.ensureValidIndividualResourceId("Invalid user id provided", auth.getUserId());
            }
            if (auth.getGroupIds() != null) {
                EnsureUtil.ensureValidIndividualResourceIds("At least one invalid group id provided", auth.getGroupIds());
            }
            if (auth.getTenantIds() != null) {
                EnsureUtil.ensureValidIndividualResourceIds("At least one invalid tenant id provided", auth.getTenantIds());
            }
            this.currentAuthentication.set(auth);
        }
    }
    
    @Override
    public void setAuthentication(final String userId, final List<String> groups) {
        this.setAuthentication(new Authentication(userId, groups));
    }
    
    @Override
    public void setAuthentication(final String userId, final List<String> groups, final List<String> tenantIds) {
        this.setAuthentication(new Authentication(userId, groups, tenantIds));
    }
    
    @Override
    public void clearAuthentication() {
        this.currentAuthentication.remove();
    }
    
    @Override
    public Authentication getCurrentAuthentication() {
        return this.currentAuthentication.get();
    }
    
    @Override
    public String getUserInfo(final String userId, final String key) {
        return this.commandExecutor.execute((Command<String>)new GetUserInfoCmd(userId, key));
    }
    
    @Override
    public List<String> getUserInfoKeys(final String userId) {
        return this.commandExecutor.execute((Command<List<String>>)new GetUserInfoKeysCmd(userId, "userinfo"));
    }
    
    @Override
    public List<String> getUserAccountNames(final String userId) {
        return this.commandExecutor.execute((Command<List<String>>)new GetUserInfoKeysCmd(userId, "account"));
    }
    
    @Override
    public void setUserInfo(final String userId, final String key, final String value) {
        this.commandExecutor.execute((Command<Object>)new SetUserInfoCmd(userId, key, value));
    }
    
    @Override
    public void deleteUserInfo(final String userId, final String key) {
        this.commandExecutor.execute((Command<Object>)new DeleteUserInfoCmd(userId, key));
    }
    
    @Override
    public void deleteUserAccount(final String userId, final String accountName) {
        this.commandExecutor.execute((Command<Object>)new DeleteUserInfoCmd(userId, accountName));
    }
    
    @Override
    public Account getUserAccount(final String userId, final String userPassword, final String accountName) {
        return this.commandExecutor.execute((Command<Account>)new GetUserAccountCmd(userId, userPassword, accountName));
    }
    
    @Override
    public void setUserAccount(final String userId, final String userPassword, final String accountName, final String accountUsername, final String accountPassword, final Map<String, String> accountDetails) {
        this.commandExecutor.execute((Command<Object>)new SetUserInfoCmd(userId, userPassword, accountName, accountUsername, accountPassword, accountDetails));
    }
    
    @Override
    public void createTenantUserMembership(final String tenantId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new CreateTenantUserMembershipCmd(tenantId, userId));
    }
    
    @Override
    public void createTenantGroupMembership(final String tenantId, final String groupId) {
        this.commandExecutor.execute((Command<Object>)new CreateTenantGroupMembershipCmd(tenantId, groupId));
    }
    
    @Override
    public void deleteTenantUserMembership(final String tenantId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new DeleteTenantUserMembershipCmd(tenantId, userId));
    }
    
    @Override
    public void deleteTenantGroupMembership(final String tenantId, final String groupId) {
        this.commandExecutor.execute((Command<Object>)new DeleteTenantGroupMembershipCmd(tenantId, groupId));
    }
}
