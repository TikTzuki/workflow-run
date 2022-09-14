// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.security.NoSuchAlgorithmException;
import org.zik.bpm.engine.ProcessEngineException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.DbEntity;

public class TaskMeterLogEntity implements DbEntity, HasDbReferences, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected Date timestamp;
    protected long assigneeHash;
    
    public TaskMeterLogEntity(final String assignee, final Date timestamp) {
        this.assigneeHash = this.createHashAsLong(assignee);
        this.timestamp = timestamp;
    }
    
    protected long createHashAsLong(final String assignee) {
        final String algorithm = "MD5";
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(assignee.getBytes(StandardCharsets.UTF_8));
            return ByteBuffer.wrap(digest.digest(), 0, 8).getLong();
        }
        catch (NoSuchAlgorithmException e) {
            throw new ProcessEngineException("Cannot lookup hash algorithm '" + algorithm + "'");
        }
    }
    
    public TaskMeterLogEntity() {
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public long getAssigneeHash() {
        return this.assigneeHash;
    }
    
    public void setAssigneeHash(final long assigneeHash) {
        this.assigneeHash = assigneeHash;
    }
    
    @Override
    public Object getPersistentState() {
        return TaskMeterLogEntity.class;
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        return referenceIdAndClass;
    }
}
