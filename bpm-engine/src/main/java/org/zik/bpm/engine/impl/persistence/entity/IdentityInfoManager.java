// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Date;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class IdentityInfoManager extends AbstractManager
{
    public void deleteUserInfoByUserIdAndKey(final String userId, final String key) {
        final IdentityInfoEntity identityInfoEntity = this.findUserInfoByUserIdAndKey(userId, key);
        if (identityInfoEntity != null) {
            this.deleteIdentityInfo(identityInfoEntity);
        }
    }
    
    public void deleteIdentityInfo(final IdentityInfoEntity identityInfo) {
        this.getDbEntityManager().delete(identityInfo);
        if ("account".equals(identityInfo.getType())) {
            for (final IdentityInfoEntity identityInfoDetail : this.findIdentityInfoDetails(identityInfo.getId())) {
                this.getDbEntityManager().delete(identityInfoDetail);
            }
        }
    }
    
    public IdentityInfoEntity findUserAccountByUserIdAndKey(final String userId, final String userPassword, final String key) {
        final IdentityInfoEntity identityInfoEntity = this.findUserInfoByUserIdAndKey(userId, key);
        if (identityInfoEntity == null) {
            return null;
        }
        final Map<String, String> details = new HashMap<String, String>();
        final String identityInfoId = identityInfoEntity.getId();
        final List<IdentityInfoEntity> identityInfoDetails = this.findIdentityInfoDetails(identityInfoId);
        for (final IdentityInfoEntity identityInfoDetail : identityInfoDetails) {
            details.put(identityInfoDetail.getKey(), identityInfoDetail.getValue());
        }
        identityInfoEntity.setDetails(details);
        if (identityInfoEntity.getPasswordBytes() != null) {
            final String password = this.decryptPassword(identityInfoEntity.getPasswordBytes(), userPassword);
            identityInfoEntity.setPassword(password);
        }
        return identityInfoEntity;
    }
    
    protected List<IdentityInfoEntity> findIdentityInfoDetails(final String identityInfoId) {
        return (List<IdentityInfoEntity>)this.getDbEntityManager().selectList("selectIdentityInfoDetails", identityInfoId);
    }
    
    public void setUserInfo(final String userId, final String userPassword, final String type, final String key, final String value, final String accountPassword, Map<String, String> accountDetails) {
        byte[] storedPassword = null;
        if (accountPassword != null) {
            storedPassword = this.encryptPassword(accountPassword, userPassword);
        }
        IdentityInfoEntity identityInfoEntity = this.findUserInfoByUserIdAndKey(userId, key);
        if (identityInfoEntity != null) {
            identityInfoEntity.setValue(value);
            identityInfoEntity.setPasswordBytes(storedPassword);
            if (accountDetails == null) {
                accountDetails = new HashMap<String, String>();
            }
            final Set<String> newKeys = new HashSet<String>(accountDetails.keySet());
            final List<IdentityInfoEntity> identityInfoDetails = this.findIdentityInfoDetails(identityInfoEntity.getId());
            for (final IdentityInfoEntity identityInfoDetail : identityInfoDetails) {
                final String detailKey = identityInfoDetail.getKey();
                newKeys.remove(detailKey);
                final String newDetailValue = accountDetails.get(detailKey);
                if (newDetailValue == null) {
                    this.deleteIdentityInfo(identityInfoDetail);
                }
                else {
                    identityInfoDetail.setValue(newDetailValue);
                }
            }
            this.insertAccountDetails(identityInfoEntity, accountDetails, newKeys);
        }
        else {
            identityInfoEntity = new IdentityInfoEntity();
            identityInfoEntity.setUserId(userId);
            identityInfoEntity.setType(type);
            identityInfoEntity.setKey(key);
            identityInfoEntity.setValue(value);
            identityInfoEntity.setPasswordBytes(storedPassword);
            this.getDbEntityManager().insert(identityInfoEntity);
            if (accountDetails != null) {
                this.insertAccountDetails(identityInfoEntity, accountDetails, accountDetails.keySet());
            }
        }
    }
    
    private void insertAccountDetails(final IdentityInfoEntity identityInfoEntity, final Map<String, String> accountDetails, final Set<String> keys) {
        for (final String newKey : keys) {
            final IdentityInfoEntity identityInfoDetail = new IdentityInfoEntity();
            identityInfoDetail.setParentId(identityInfoEntity.getId());
            identityInfoDetail.setKey(newKey);
            identityInfoDetail.setValue(accountDetails.get(newKey));
            this.getDbEntityManager().insert(identityInfoDetail);
        }
    }
    
    public byte[] encryptPassword(final String accountPassword, final String userPassword) {
        return accountPassword.getBytes();
    }
    
    public String decryptPassword(final byte[] storedPassword, final String userPassword) {
        return new String(storedPassword);
    }
    
    public IdentityInfoEntity findUserInfoByUserIdAndKey(final String userId, final String key) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("userId", userId);
        parameters.put("key", key);
        return (IdentityInfoEntity)this.getDbEntityManager().selectOne("selectIdentityInfoByUserIdAndKey", parameters);
    }
    
    public List<String> findUserInfoKeysByUserIdAndType(final String userId, final String type) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("userId", userId);
        parameters.put("type", type);
        return (List<String>)this.getDbEntityManager().selectList("selectIdentityInfoKeysByUserIdAndType", parameters);
    }
    
    public void deleteUserInfoByUserId(final String userId) {
        final List<IdentityInfoEntity> identityInfos = (List<IdentityInfoEntity>)this.getDbEntityManager().selectList("selectIdentityInfoByUserId", userId);
        for (final IdentityInfoEntity identityInfo : identityInfos) {
            this.getIdentityInfoManager().deleteIdentityInfo(identityInfo);
        }
    }
    
    public void updateUserLock(final UserEntity user, final int attempts, final Date lockExpirationTime) {
        user.setAttempts(attempts);
        user.setLockExpirationTime(lockExpirationTime);
        this.getDbEntityManager().update(UserEntity.class, "updateUserLock", user);
    }
}
