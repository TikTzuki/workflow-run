// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.identity.User;
import org.zik.bpm.engine.identity.UserQuery;

public abstract class UserQueryImpl extends AbstractQuery<UserQuery, User> implements UserQuery
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String[] ids;
    protected String firstName;
    protected String firstNameLike;
    protected String lastName;
    protected String lastNameLike;
    protected String email;
    protected String emailLike;
    protected String groupId;
    protected String procDefId;
    protected String tenantId;
    
    public UserQueryImpl() {
    }
    
    public UserQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public UserQuery userId(final String id) {
        EnsureUtil.ensureNotNull("Provided id", (Object)id);
        this.id = id;
        return this;
    }
    
    @Override
    public UserQuery userIdIn(final String... ids) {
        EnsureUtil.ensureNotNull("Provided ids", (Object[])ids);
        this.ids = ids;
        return this;
    }
    
    @Override
    public UserQuery userFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }
    
    @Override
    public UserQuery userFirstNameLike(final String firstNameLike) {
        EnsureUtil.ensureNotNull("Provided firstNameLike", (Object)firstNameLike);
        this.firstNameLike = firstNameLike;
        return this;
    }
    
    @Override
    public UserQuery userLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }
    
    @Override
    public UserQuery userLastNameLike(final String lastNameLike) {
        EnsureUtil.ensureNotNull("Provided lastNameLike", (Object)lastNameLike);
        this.lastNameLike = lastNameLike;
        return this;
    }
    
    @Override
    public UserQuery userEmail(final String email) {
        this.email = email;
        return this;
    }
    
    @Override
    public UserQuery userEmailLike(final String emailLike) {
        EnsureUtil.ensureNotNull("Provided emailLike", (Object)emailLike);
        this.emailLike = emailLike;
        return this;
    }
    
    @Override
    public UserQuery memberOfGroup(final String groupId) {
        EnsureUtil.ensureNotNull("Provided groupId", (Object)groupId);
        this.groupId = groupId;
        return this;
    }
    
    @Override
    public UserQuery potentialStarter(final String procDefId) {
        EnsureUtil.ensureNotNull("Provided processDefinitionId", (Object)procDefId);
        this.procDefId = procDefId;
        return this;
    }
    
    @Override
    public UserQuery memberOfTenant(final String tenantId) {
        EnsureUtil.ensureNotNull("Provided tenantId", (Object)tenantId);
        this.tenantId = tenantId;
        return this;
    }
    
    @Override
    public UserQuery orderByUserId() {
        return ((AbstractQuery<UserQuery, U>)this).orderBy(UserQueryProperty.USER_ID);
    }
    
    @Override
    public UserQuery orderByUserEmail() {
        return ((AbstractQuery<UserQuery, U>)this).orderBy(UserQueryProperty.EMAIL);
    }
    
    @Override
    public UserQuery orderByUserFirstName() {
        return ((AbstractQuery<UserQuery, U>)this).orderBy(UserQueryProperty.FIRST_NAME);
    }
    
    @Override
    public UserQuery orderByUserLastName() {
        return ((AbstractQuery<UserQuery, U>)this).orderBy(UserQueryProperty.LAST_NAME);
    }
    
    public String getId() {
        return this.id;
    }
    
    public String[] getIds() {
        return this.ids;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public String getFirstNameLike() {
        return this.firstNameLike;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public String getLastNameLike() {
        return this.lastNameLike;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getEmailLike() {
        return this.emailLike;
    }
    
    public String getGroupId() {
        return this.groupId;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
}
