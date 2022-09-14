// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import org.zik.bpm.engine.impl.identity.Account;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;

public class IdentityInfoEntity implements DbEntity, HasDbRevision, Account, Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String TYPE_USERACCOUNT = "account";
    public static final String TYPE_USERINFO = "userinfo";
    protected String id;
    protected int revision;
    protected String type;
    protected String userId;
    protected String key;
    protected String value;
    protected String password;
    protected byte[] passwordBytes;
    protected String parentId;
    protected Map<String, String> details;
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("value", this.value);
        persistentState.put("password", this.passwordBytes);
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
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public byte[] getPasswordBytes() {
        return this.passwordBytes;
    }
    
    public void setPasswordBytes(final byte[] passwordBytes) {
        this.passwordBytes = passwordBytes;
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    @Override
    public String getName() {
        return this.key;
    }
    
    @Override
    public String getUsername() {
        return this.value;
    }
    
    public String getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final String parentId) {
        this.parentId = parentId;
    }
    
    @Override
    public Map<String, String> getDetails() {
        return this.details;
    }
    
    public void setDetails(final Map<String, String> details) {
        this.details = details;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", revision=" + this.revision + ", type=" + this.type + ", userId=" + this.userId + ", key=" + this.key + ", value=" + this.value + ", password=" + this.password + ", parentId=" + this.parentId + ", details=" + this.details + "]";
    }
}
