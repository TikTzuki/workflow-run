// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.identity.PasswordPolicyResult;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EncryptionUtil;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.io.Serializable;
import org.zik.bpm.engine.identity.User;

public class UserEntity implements User, Serializable, DbEntity, HasDbRevision
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int revision;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String newPassword;
    protected String salt;
    protected Date lockExpirationTime;
    protected int attempts;
    
    public UserEntity() {
    }
    
    public UserEntity(final String id) {
        this.id = id;
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("firstName", this.firstName);
        persistentState.put("lastName", this.lastName);
        persistentState.put("email", this.email);
        persistentState.put("password", this.password);
        persistentState.put("salt", this.salt);
        return persistentState;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
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
    public String getFirstName() {
        return this.firstName;
    }
    
    @Override
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    @Override
    public String getLastName() {
        return this.lastName;
    }
    
    @Override
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    @Override
    public String getEmail() {
        return this.email;
    }
    
    @Override
    public void setEmail(final String email) {
        this.email = email;
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }
    
    @Override
    public void setPassword(final String password) {
        this.newPassword = password;
    }
    
    public String getSalt() {
        return this.salt;
    }
    
    public void setSalt(final String salt) {
        this.salt = salt;
    }
    
    public void setDbPassword(final String password) {
        this.password = password;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    public Date getLockExpirationTime() {
        return this.lockExpirationTime;
    }
    
    public void setLockExpirationTime(final Date lockExpirationTime) {
        this.lockExpirationTime = lockExpirationTime;
    }
    
    public int getAttempts() {
        return this.attempts;
    }
    
    public void setAttempts(final int attempts) {
        this.attempts = attempts;
    }
    
    public void encryptPassword() {
        if (this.newPassword != null) {
            this.salt = this.generateSalt();
            this.setDbPassword(this.encryptPassword(this.newPassword, this.salt));
        }
    }
    
    protected String encryptPassword(final String password, final String salt) {
        if (password == null) {
            return null;
        }
        final String saltedPassword = EncryptionUtil.saltPassword(password, salt);
        return Context.getProcessEngineConfiguration().getPasswordManager().encrypt(saltedPassword);
    }
    
    protected String generateSalt() {
        return Context.getProcessEngineConfiguration().getSaltGenerator().generateSalt();
    }
    
    public boolean checkPasswordAgainstPolicy() {
        final PasswordPolicyResult result = Context.getProcessEngineConfiguration().getIdentityService().checkPasswordAgainstPolicy(this.newPassword, this);
        return result.isValid();
    }
    
    public boolean hasNewPassword() {
        return this.newPassword != null;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", revision=" + this.revision + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", email=" + this.email + ", password=" + this.password + ", salt=" + this.salt + ", lockExpirationTime=" + this.lockExpirationTime + ", attempts=" + this.attempts + "]";
    }
}
