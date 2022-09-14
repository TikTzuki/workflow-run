// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.query.QueryProperty;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import org.zik.bpm.engine.impl.Direction;
import org.zik.bpm.engine.impl.QueryPropertyImpl;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.task.Event;
import org.zik.bpm.engine.task.Comment;
import java.util.List;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class CommentManager extends AbstractHistoricManager
{
    @Override
    public void delete(final DbEntity dbEntity) {
        this.checkHistoryEnabled();
        super.delete(dbEntity);
    }
    
    @Override
    public void insert(final DbEntity dbEntity) {
        this.checkHistoryEnabled();
        super.insert(dbEntity);
    }
    
    public List<Comment> findCommentsByTaskId(final String taskId) {
        this.checkHistoryEnabled();
        return (List<Comment>)this.getDbEntityManager().selectList("selectCommentsByTaskId", taskId);
    }
    
    public List<Event> findEventsByTaskId(final String taskId) {
        this.checkHistoryEnabled();
        final ListQueryParameterObject query = new ListQueryParameterObject();
        query.setParameter(taskId);
        query.getOrderingProperties().add(new QueryOrderingProperty(new QueryPropertyImpl("TIME_"), Direction.DESCENDING));
        return (List<Event>)this.getDbEntityManager().selectList("selectEventsByTaskId", query);
    }
    
    public void deleteCommentsByTaskId(final String taskId) {
        this.checkHistoryEnabled();
        this.getDbEntityManager().delete(CommentEntity.class, "deleteCommentsByTaskId", taskId);
    }
    
    public void deleteCommentsByProcessInstanceIds(final List<String> processInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceIds", processInstanceIds);
        this.deleteComments(parameters);
    }
    
    public void deleteCommentsByTaskProcessInstanceIds(final List<String> processInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("taskProcessInstanceIds", processInstanceIds);
        this.deleteComments(parameters);
    }
    
    public void deleteCommentsByTaskCaseInstanceIds(final List<String> caseInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("taskCaseInstanceIds", caseInstanceIds);
        this.deleteComments(parameters);
    }
    
    protected void deleteComments(final Map<String, Object> parameters) {
        this.getDbEntityManager().deletePreserveOrder(CommentEntity.class, "deleteCommentsByIds", parameters);
    }
    
    public List<Comment> findCommentsByProcessInstanceId(final String processInstanceId) {
        this.checkHistoryEnabled();
        return (List<Comment>)this.getDbEntityManager().selectList("selectCommentsByProcessInstanceId", processInstanceId);
    }
    
    public CommentEntity findCommentByTaskIdAndCommentId(final String taskId, final String commentId) {
        this.checkHistoryEnabled();
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("taskId", taskId);
        parameters.put("id", commentId);
        return (CommentEntity)this.getDbEntityManager().selectOne("selectCommentByTaskIdAndCommentId", parameters);
    }
    
    public void addRemovalTimeToCommentsByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(CommentEntity.class, "updateCommentsByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToCommentsByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(CommentEntity.class, "updateCommentsByProcessInstanceId", parameters);
    }
    
    public DbOperation deleteCommentsByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(CommentEntity.class, "deleteCommentsByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
}
