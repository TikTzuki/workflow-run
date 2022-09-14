// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class ByteArrayManager extends AbstractManager
{
    public void deleteByteArrayById(final String byteArrayEntityId) {
        this.getDbEntityManager().delete(ByteArrayEntity.class, "deleteByteArrayNoRevisionCheck", byteArrayEntityId);
    }
    
    public void insertByteArray(final ByteArrayEntity arr) {
        arr.setCreateTime(ClockUtil.getCurrentTime());
        this.getDbEntityManager().insert(arr);
    }
    
    public void addRemovalTimeToByteArraysByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateByteArraysByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToByteArraysByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateVariableByteArraysByProcessInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateDecisionInputsByteArraysByProcessInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateDecisionOutputsByteArraysByProcessInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateJobLogByteArraysByProcessInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateExternalTaskLogByteArraysByProcessInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateAttachmentByteArraysByProcessInstanceId", parameters);
    }
    
    public DbOperation deleteByteArraysByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(ByteArrayEntity.class, "deleteByteArraysByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
}
