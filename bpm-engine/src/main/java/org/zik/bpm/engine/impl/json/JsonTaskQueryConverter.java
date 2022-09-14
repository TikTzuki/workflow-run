// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import org.zik.bpm.engine.impl.QueryOperator;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import java.util.Date;
import org.zik.bpm.engine.task.DelegationState;
import org.zik.bpm.engine.impl.TaskQueryVariableValue;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.Iterator;
import com.google.gson.JsonArray;
import java.util.Map;
import com.google.gson.JsonElement;
import org.zik.bpm.engine.impl.TaskQueryImpl;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.task.TaskQuery;

public class JsonTaskQueryConverter extends JsonObjectConverter<TaskQuery>
{
    public static final String ID = "id";
    public static final String TASK_ID = "taskId";
    public static final String TASK_ID_IN = "taskIdIn";
    public static final String NAME = "name";
    public static final String NAME_NOT_EQUAL = "nameNotEqual";
    public static final String NAME_LIKE = "nameLike";
    public static final String NAME_NOT_LIKE = "nameNotLike";
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_LIKE = "descriptionLike";
    public static final String PRIORITY = "priority";
    public static final String MIN_PRIORITY = "minPriority";
    public static final String MAX_PRIORITY = "maxPriority";
    public static final String ASSIGNEE = "assignee";
    public static final String ASSIGNEE_LIKE = "assigneeLike";
    public static final String ASSIGNEE_IN = "assigneeIn";
    public static final String ASSIGNEE_NOT_IN = "assigneeNotIn";
    public static final String INVOLVED_USER = "involvedUser";
    public static final String OWNER = "owner";
    public static final String UNASSIGNED = "unassigned";
    public static final String ASSIGNED = "assigned";
    public static final String DELEGATION_STATE = "delegationState";
    public static final String CANDIDATE_USER = "candidateUser";
    public static final String CANDIDATE_GROUP = "candidateGroup";
    public static final String CANDIDATE_GROUPS = "candidateGroups";
    public static final String WITH_CANDIDATE_GROUPS = "withCandidateGroups";
    public static final String WITHOUT_CANDIDATE_GROUPS = "withoutCandidateGroups";
    public static final String WITH_CANDIDATE_USERS = "withCandidateUsers";
    public static final String WITHOUT_CANDIDATE_USERS = "withoutCandidateUsers";
    public static final String INCLUDE_ASSIGNED_TASKS = "includeAssignedTasks";
    public static final String INSTANCE_ID = "instanceId";
    public static final String PROCESS_INSTANCE_ID = "processInstanceId";
    public static final String PROCESS_INSTANCE_ID_IN = "processInstanceIdIn";
    public static final String EXECUTION_ID = "executionId";
    public static final String ACTIVITY_INSTANCE_ID_IN = "activityInstanceIdIn";
    public static final String CREATED = "created";
    public static final String CREATED_BEFORE = "createdBefore";
    public static final String CREATED_AFTER = "createdAfter";
    public static final String KEY = "key";
    public static final String KEYS = "keys";
    public static final String KEY_LIKE = "keyLike";
    public static final String PARENT_TASK_ID = "parentTaskId";
    public static final String PROCESS_DEFINITION_KEY = "processDefinitionKey";
    public static final String PROCESS_DEFINITION_KEYS = "processDefinitionKeys";
    public static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    public static final String PROCESS_DEFINITION_NAME = "processDefinitionName";
    public static final String PROCESS_DEFINITION_NAME_LIKE = "processDefinitionNameLike";
    public static final String PROCESS_INSTANCE_BUSINESS_KEY = "processInstanceBusinessKey";
    public static final String PROCESS_INSTANCE_BUSINESS_KEYS = "processInstanceBusinessKeys";
    public static final String PROCESS_INSTANCE_BUSINESS_KEY_LIKE = "processInstanceBusinessKeyLike";
    public static final String DUE = "due";
    public static final String DUE_DATE = "dueDate";
    public static final String DUE_BEFORE = "dueBefore";
    public static final String DUE_AFTER = "dueAfter";
    public static final String WITHOUT_DUE_DATE = "withoutDueDate";
    public static final String FOLLOW_UP = "followUp";
    public static final String FOLLOW_UP_DATE = "followUpDate";
    public static final String FOLLOW_UP_BEFORE = "followUpBefore";
    public static final String FOLLOW_UP_NULL_ACCEPTED = "followUpNullAccepted";
    public static final String FOLLOW_UP_AFTER = "followUpAfter";
    public static final String EXCLUDE_SUBTASKS = "excludeSubtasks";
    public static final String CASE_DEFINITION_KEY = "caseDefinitionKey";
    public static final String CASE_DEFINITION_ID = "caseDefinitionId";
    public static final String CASE_DEFINITION_NAME = "caseDefinitionName";
    public static final String CASE_DEFINITION_NAME_LIKE = "caseDefinitionNameLike";
    public static final String CASE_INSTANCE_ID = "caseInstanceId";
    public static final String CASE_INSTANCE_BUSINESS_KEY = "caseInstanceBusinessKey";
    public static final String CASE_INSTANCE_BUSINESS_KEY_LIKE = "caseInstanceBusinessKeyLike";
    public static final String CASE_EXECUTION_ID = "caseExecutionId";
    public static final String ACTIVE = "active";
    public static final String SUSPENDED = "suspended";
    public static final String PROCESS_VARIABLES = "processVariables";
    public static final String TASK_VARIABLES = "taskVariables";
    public static final String CASE_INSTANCE_VARIABLES = "caseInstanceVariables";
    public static final String TENANT_IDS = "tenantIds";
    public static final String WITHOUT_TENANT_ID = "withoutTenantId";
    public static final String ORDERING_PROPERTIES = "orderingProperties";
    public static final String OR_QUERIES = "orQueries";
    @Deprecated
    public static final String ORDER_BY = "orderBy";
    protected static JsonTaskQueryVariableValueConverter variableValueConverter;
    
    @Override
    public JsonObject toJsonObject(final TaskQuery taskQuery) {
        return this.toJsonObject(taskQuery, false);
    }
    
    public JsonObject toJsonObject(final TaskQuery taskQuery, final boolean isOrQueryActive) {
        final JsonObject json = JsonUtil.createObject();
        final TaskQueryImpl query = (TaskQueryImpl)taskQuery;
        JsonUtil.addField(json, "taskId", query.getTaskId());
        JsonUtil.addArrayField(json, "taskIdIn", query.getTaskIdIn());
        JsonUtil.addField(json, "name", query.getName());
        JsonUtil.addField(json, "nameNotEqual", query.getNameNotEqual());
        JsonUtil.addField(json, "nameLike", query.getNameLike());
        JsonUtil.addField(json, "nameNotLike", query.getNameNotLike());
        JsonUtil.addField(json, "description", query.getDescription());
        JsonUtil.addField(json, "descriptionLike", query.getDescriptionLike());
        JsonUtil.addField(json, "priority", query.getPriority());
        JsonUtil.addField(json, "minPriority", query.getMinPriority());
        JsonUtil.addField(json, "maxPriority", query.getMaxPriority());
        JsonUtil.addField(json, "assignee", query.getAssignee());
        if (query.getAssigneeIn() != null) {
            JsonUtil.addArrayField(json, "assigneeIn", query.getAssigneeIn().toArray(new String[query.getAssigneeIn().size()]));
        }
        if (query.getAssigneeNotIn() != null) {
            JsonUtil.addArrayField(json, "assigneeNotIn", query.getAssigneeNotIn().toArray(new String[query.getAssigneeNotIn().size()]));
        }
        JsonUtil.addField(json, "assigneeLike", query.getAssigneeLike());
        JsonUtil.addField(json, "involvedUser", query.getInvolvedUser());
        JsonUtil.addField(json, "owner", query.getOwner());
        JsonUtil.addDefaultField(json, "unassigned", false, query.isUnassigned());
        JsonUtil.addDefaultField(json, "assigned", false, query.isAssigned());
        JsonUtil.addField(json, "delegationState", query.getDelegationStateString());
        JsonUtil.addField(json, "candidateUser", query.getCandidateUser());
        JsonUtil.addField(json, "candidateGroup", query.getCandidateGroup());
        JsonUtil.addListField(json, "candidateGroups", query.getCandidateGroupsInternal());
        JsonUtil.addDefaultField(json, "withCandidateGroups", false, query.isWithCandidateGroups());
        JsonUtil.addDefaultField(json, "withoutCandidateGroups", false, query.isWithoutCandidateGroups());
        JsonUtil.addDefaultField(json, "withCandidateUsers", false, query.isWithCandidateUsers());
        JsonUtil.addDefaultField(json, "withoutCandidateUsers", false, query.isWithoutCandidateUsers());
        JsonUtil.addField(json, "includeAssignedTasks", query.isIncludeAssignedTasksInternal());
        JsonUtil.addField(json, "processInstanceId", query.getProcessInstanceId());
        if (query.getProcessInstanceIdIn() != null) {
            JsonUtil.addArrayField(json, "processInstanceIdIn", query.getProcessInstanceIdIn());
        }
        JsonUtil.addField(json, "executionId", query.getExecutionId());
        JsonUtil.addArrayField(json, "activityInstanceIdIn", query.getActivityInstanceIdIn());
        JsonUtil.addDateField(json, "created", query.getCreateTime());
        JsonUtil.addDateField(json, "createdBefore", query.getCreateTimeBefore());
        JsonUtil.addDateField(json, "createdAfter", query.getCreateTimeAfter());
        JsonUtil.addField(json, "key", query.getKey());
        JsonUtil.addArrayField(json, "keys", query.getKeys());
        JsonUtil.addField(json, "keyLike", query.getKeyLike());
        JsonUtil.addField(json, "parentTaskId", query.getParentTaskId());
        JsonUtil.addField(json, "processDefinitionKey", query.getProcessDefinitionKey());
        JsonUtil.addArrayField(json, "processDefinitionKeys", query.getProcessDefinitionKeys());
        JsonUtil.addField(json, "processDefinitionId", query.getProcessDefinitionId());
        JsonUtil.addField(json, "processDefinitionName", query.getProcessDefinitionName());
        JsonUtil.addField(json, "processDefinitionNameLike", query.getProcessDefinitionNameLike());
        JsonUtil.addField(json, "processInstanceBusinessKey", query.getProcessInstanceBusinessKey());
        JsonUtil.addArrayField(json, "processInstanceBusinessKeys", query.getProcessInstanceBusinessKeys());
        JsonUtil.addField(json, "processInstanceBusinessKeyLike", query.getProcessInstanceBusinessKeyLike());
        this.addVariablesFields(json, query.getVariables());
        JsonUtil.addDateField(json, "due", query.getDueDate());
        JsonUtil.addDateField(json, "dueBefore", query.getDueBefore());
        JsonUtil.addDateField(json, "dueAfter", query.getDueAfter());
        JsonUtil.addDefaultField(json, "withoutDueDate", false, query.isWithoutDueDate());
        JsonUtil.addDateField(json, "followUp", query.getFollowUpDate());
        JsonUtil.addDateField(json, "followUpBefore", query.getFollowUpBefore());
        JsonUtil.addDefaultField(json, "followUpNullAccepted", false, query.isFollowUpNullAccepted());
        JsonUtil.addDateField(json, "followUpAfter", query.getFollowUpAfter());
        JsonUtil.addDefaultField(json, "excludeSubtasks", false, query.isExcludeSubtasks());
        this.addSuspensionStateField(json, query.getSuspensionState());
        JsonUtil.addField(json, "caseDefinitionKey", query.getCaseDefinitionKey());
        JsonUtil.addField(json, "caseDefinitionId", query.getCaseDefinitionId());
        JsonUtil.addField(json, "caseDefinitionName", query.getCaseDefinitionName());
        JsonUtil.addField(json, "caseDefinitionNameLike", query.getCaseDefinitionNameLike());
        JsonUtil.addField(json, "caseInstanceId", query.getCaseInstanceId());
        JsonUtil.addField(json, "caseInstanceBusinessKey", query.getCaseInstanceBusinessKey());
        JsonUtil.addField(json, "caseInstanceBusinessKeyLike", query.getCaseInstanceBusinessKeyLike());
        JsonUtil.addField(json, "caseExecutionId", query.getCaseExecutionId());
        this.addTenantIdFields(json, query);
        if (query.getQueries().size() > 1 && !isOrQueryActive) {
            final JsonArray orQueries = JsonUtil.createArray();
            for (final TaskQueryImpl orQuery : query.getQueries()) {
                if (orQuery != null && orQuery.isOrQueryActive()) {
                    orQueries.add(this.toJsonObject(orQuery, true));
                }
            }
            JsonUtil.addField(json, "orQueries", orQueries);
        }
        if (query.getOrderingProperties() != null && !query.getOrderingProperties().isEmpty()) {
            JsonUtil.addField(json, "orderingProperties", JsonQueryOrderingPropertyConverter.ARRAY_CONVERTER.toJsonArray(query.getOrderingProperties()));
        }
        for (final Map.Entry<String, String> expressionEntry : query.getExpressions().entrySet()) {
            JsonUtil.addField(json, expressionEntry.getKey() + "Expression", expressionEntry.getValue());
        }
        return json;
    }
    
    protected void addSuspensionStateField(final JsonObject jsonObject, final SuspensionState suspensionState) {
        if (suspensionState != null) {
            if (suspensionState.equals(SuspensionState.ACTIVE)) {
                JsonUtil.addField(jsonObject, "active", true);
            }
            else if (suspensionState.equals(SuspensionState.SUSPENDED)) {
                JsonUtil.addField(jsonObject, "suspended", true);
            }
        }
    }
    
    protected void addTenantIdFields(final JsonObject jsonObject, final TaskQueryImpl query) {
        if (query.getTenantIds() != null) {
            JsonUtil.addArrayField(jsonObject, "tenantIds", query.getTenantIds());
        }
        if (query.isWithoutTenantId()) {
            JsonUtil.addField(jsonObject, "withoutTenantId", true);
        }
    }
    
    protected void addVariablesFields(final JsonObject jsonObject, final List<TaskQueryVariableValue> variables) {
        for (final TaskQueryVariableValue variable : variables) {
            if (variable.isProcessInstanceVariable()) {
                this.addVariable(jsonObject, "processVariables", variable);
            }
            else if (variable.isLocal()) {
                this.addVariable(jsonObject, "taskVariables", variable);
            }
            else {
                this.addVariable(jsonObject, "caseInstanceVariables", variable);
            }
        }
    }
    
    protected void addVariable(final JsonObject jsonObject, final String variableType, final TaskQueryVariableValue variable) {
        final JsonArray variables = JsonUtil.getArray(jsonObject, variableType);
        JsonUtil.addElement(variables, JsonTaskQueryConverter.variableValueConverter, variable);
        JsonUtil.addField(jsonObject, variableType, variables);
    }
    
    @Override
    public TaskQuery toObject(final JsonObject json) {
        return this.toObject(json, false);
    }
    
    protected TaskQuery toObject(final JsonObject json, final boolean isOrQuery) {
        final TaskQueryImpl query = new TaskQueryImpl();
        if (isOrQuery) {
            query.setOrQueryActive();
        }
        if (json.has("orQueries")) {
            for (final JsonElement jsonElement : JsonUtil.getArray(json, "orQueries")) {
                query.addOrQuery((TaskQueryImpl)this.toObject(JsonUtil.getObject(jsonElement), true));
            }
        }
        if (json.has("taskId")) {
            query.taskId(JsonUtil.getString(json, "taskId"));
        }
        if (json.has("taskIdIn")) {
            query.taskIdIn(this.getArray(JsonUtil.getArray(json, "taskIdIn")));
        }
        if (json.has("name")) {
            query.taskName(JsonUtil.getString(json, "name"));
        }
        if (json.has("nameNotEqual")) {
            query.taskNameNotEqual(JsonUtil.getString(json, "nameNotEqual"));
        }
        if (json.has("nameLike")) {
            query.taskNameLike(JsonUtil.getString(json, "nameLike"));
        }
        if (json.has("nameNotLike")) {
            query.taskNameNotLike(JsonUtil.getString(json, "nameNotLike"));
        }
        if (json.has("description")) {
            query.taskDescription(JsonUtil.getString(json, "description"));
        }
        if (json.has("descriptionLike")) {
            query.taskDescriptionLike(JsonUtil.getString(json, "descriptionLike"));
        }
        if (json.has("priority")) {
            query.taskPriority(JsonUtil.getInt(json, "priority"));
        }
        if (json.has("minPriority")) {
            query.taskMinPriority(JsonUtil.getInt(json, "minPriority"));
        }
        if (json.has("maxPriority")) {
            query.taskMaxPriority(JsonUtil.getInt(json, "maxPriority"));
        }
        if (json.has("assignee")) {
            query.taskAssignee(JsonUtil.getString(json, "assignee"));
        }
        if (json.has("assigneeLike")) {
            query.taskAssigneeLike(JsonUtil.getString(json, "assigneeLike"));
        }
        if (json.has("assigneeIn")) {
            query.taskAssigneeIn(this.getArray(JsonUtil.getArray(json, "assigneeIn")));
        }
        if (json.has("assigneeNotIn")) {
            query.taskAssigneeNotIn(this.getArray(JsonUtil.getArray(json, "assigneeNotIn")));
        }
        if (json.has("involvedUser")) {
            query.taskInvolvedUser(JsonUtil.getString(json, "involvedUser"));
        }
        if (json.has("owner")) {
            query.taskOwner(JsonUtil.getString(json, "owner"));
        }
        if (json.has("assigned") && JsonUtil.getBoolean(json, "assigned")) {
            query.taskAssigned();
        }
        if (json.has("unassigned") && JsonUtil.getBoolean(json, "unassigned")) {
            query.taskUnassigned();
        }
        if (json.has("delegationState")) {
            query.taskDelegationState(DelegationState.valueOf(JsonUtil.getString(json, "delegationState")));
        }
        if (json.has("candidateUser")) {
            query.taskCandidateUser(JsonUtil.getString(json, "candidateUser"));
        }
        if (json.has("candidateGroup")) {
            query.taskCandidateGroup(JsonUtil.getString(json, "candidateGroup"));
        }
        if (json.has("candidateGroups") && !json.has("candidateUser") && !json.has("candidateGroup")) {
            query.taskCandidateGroupIn(this.getList(JsonUtil.getArray(json, "candidateGroups")));
        }
        if (json.has("withCandidateGroups") && JsonUtil.getBoolean(json, "withCandidateGroups")) {
            query.withCandidateGroups();
        }
        if (json.has("withoutCandidateGroups") && JsonUtil.getBoolean(json, "withoutCandidateGroups")) {
            query.withoutCandidateGroups();
        }
        if (json.has("withCandidateUsers") && JsonUtil.getBoolean(json, "withCandidateUsers")) {
            query.withCandidateUsers();
        }
        if (json.has("withoutCandidateUsers") && JsonUtil.getBoolean(json, "withoutCandidateUsers")) {
            query.withoutCandidateUsers();
        }
        if (json.has("includeAssignedTasks") && JsonUtil.getBoolean(json, "includeAssignedTasks")) {
            query.includeAssignedTasksInternal();
        }
        if (json.has("processInstanceId")) {
            query.processInstanceId(JsonUtil.getString(json, "processInstanceId"));
        }
        if (json.has("processInstanceIdIn")) {
            query.processInstanceIdIn(this.getArray(JsonUtil.getArray(json, "processInstanceIdIn")));
        }
        if (json.has("executionId")) {
            query.executionId(JsonUtil.getString(json, "executionId"));
        }
        if (json.has("activityInstanceIdIn")) {
            query.activityInstanceIdIn(this.getArray(JsonUtil.getArray(json, "activityInstanceIdIn")));
        }
        if (json.has("created")) {
            query.taskCreatedOn(new Date(JsonUtil.getLong(json, "created")));
        }
        if (json.has("createdBefore")) {
            query.taskCreatedBefore(new Date(JsonUtil.getLong(json, "createdBefore")));
        }
        if (json.has("createdAfter")) {
            query.taskCreatedAfter(new Date(JsonUtil.getLong(json, "createdAfter")));
        }
        if (json.has("key")) {
            query.taskDefinitionKey(JsonUtil.getString(json, "key"));
        }
        if (json.has("keys")) {
            query.taskDefinitionKeyIn(this.getArray(JsonUtil.getArray(json, "keys")));
        }
        if (json.has("keyLike")) {
            query.taskDefinitionKeyLike(JsonUtil.getString(json, "keyLike"));
        }
        if (json.has("parentTaskId")) {
            query.taskParentTaskId(JsonUtil.getString(json, "parentTaskId"));
        }
        if (json.has("processDefinitionKey")) {
            query.processDefinitionKey(JsonUtil.getString(json, "processDefinitionKey"));
        }
        if (json.has("processDefinitionKeys")) {
            query.processDefinitionKeyIn(this.getArray(JsonUtil.getArray(json, "processDefinitionKeys")));
        }
        if (json.has("processDefinitionId")) {
            query.processDefinitionId(JsonUtil.getString(json, "processDefinitionId"));
        }
        if (json.has("processDefinitionName")) {
            query.processDefinitionName(JsonUtil.getString(json, "processDefinitionName"));
        }
        if (json.has("processDefinitionNameLike")) {
            query.processDefinitionNameLike(JsonUtil.getString(json, "processDefinitionNameLike"));
        }
        if (json.has("processInstanceBusinessKey")) {
            query.processInstanceBusinessKey(JsonUtil.getString(json, "processInstanceBusinessKey"));
        }
        if (json.has("processInstanceBusinessKeys")) {
            query.processInstanceBusinessKeyIn(this.getArray(JsonUtil.getArray(json, "processInstanceBusinessKeys")));
        }
        if (json.has("processInstanceBusinessKeyLike")) {
            query.processInstanceBusinessKeyLike(JsonUtil.getString(json, "processInstanceBusinessKeyLike"));
        }
        if (json.has("taskVariables")) {
            this.addVariables(query, JsonUtil.getArray(json, "taskVariables"), true, false);
        }
        if (json.has("processVariables")) {
            this.addVariables(query, JsonUtil.getArray(json, "processVariables"), false, true);
        }
        if (json.has("caseInstanceVariables")) {
            this.addVariables(query, JsonUtil.getArray(json, "caseInstanceVariables"), false, false);
        }
        if (json.has("due")) {
            query.dueDate(new Date(JsonUtil.getLong(json, "due")));
        }
        if (json.has("dueBefore")) {
            query.dueBefore(new Date(JsonUtil.getLong(json, "dueBefore")));
        }
        if (json.has("dueAfter")) {
            query.dueAfter(new Date(JsonUtil.getLong(json, "dueAfter")));
        }
        if (json.has("withoutDueDate")) {
            query.withoutDueDate();
        }
        if (json.has("followUp")) {
            query.followUpDate(new Date(JsonUtil.getLong(json, "followUp")));
        }
        if (json.has("followUpBefore")) {
            query.followUpBefore(new Date(JsonUtil.getLong(json, "followUpBefore")));
        }
        if (json.has("followUpAfter")) {
            query.followUpAfter(new Date(JsonUtil.getLong(json, "followUpAfter")));
        }
        if (json.has("followUpNullAccepted")) {
            query.setFollowUpNullAccepted(JsonUtil.getBoolean(json, "followUpNullAccepted"));
        }
        if (json.has("excludeSubtasks") && JsonUtil.getBoolean(json, "excludeSubtasks")) {
            query.excludeSubtasks();
        }
        if (json.has("suspended") && JsonUtil.getBoolean(json, "suspended")) {
            query.suspended();
        }
        if (json.has("active") && JsonUtil.getBoolean(json, "active")) {
            query.active();
        }
        if (json.has("caseDefinitionKey")) {
            query.caseDefinitionKey(JsonUtil.getString(json, "caseDefinitionKey"));
        }
        if (json.has("caseDefinitionId")) {
            query.caseDefinitionId(JsonUtil.getString(json, "caseDefinitionId"));
        }
        if (json.has("caseDefinitionName")) {
            query.caseDefinitionName(JsonUtil.getString(json, "caseDefinitionName"));
        }
        if (json.has("caseDefinitionNameLike")) {
            query.caseDefinitionNameLike(JsonUtil.getString(json, "caseDefinitionNameLike"));
        }
        if (json.has("caseInstanceId")) {
            query.caseInstanceId(JsonUtil.getString(json, "caseInstanceId"));
        }
        if (json.has("caseInstanceBusinessKey")) {
            query.caseInstanceBusinessKey(JsonUtil.getString(json, "caseInstanceBusinessKey"));
        }
        if (json.has("caseInstanceBusinessKeyLike")) {
            query.caseInstanceBusinessKeyLike(JsonUtil.getString(json, "caseInstanceBusinessKeyLike"));
        }
        if (json.has("caseExecutionId")) {
            query.caseExecutionId(JsonUtil.getString(json, "caseExecutionId"));
        }
        if (json.has("tenantIds")) {
            query.tenantIdIn(this.getArray(JsonUtil.getArray(json, "tenantIds")));
        }
        if (json.has("withoutTenantId")) {
            query.withoutTenantId();
        }
        if (json.has("orderBy")) {
            final List<QueryOrderingProperty> orderingProperties = JsonLegacyQueryOrderingPropertyConverter.INSTANCE.fromOrderByString(JsonUtil.getString(json, "orderBy"));
            query.setOrderingProperties(orderingProperties);
        }
        if (json.has("orderingProperties")) {
            final JsonArray jsonArray = JsonUtil.getArray(json, "orderingProperties");
            query.setOrderingProperties(JsonQueryOrderingPropertyConverter.ARRAY_CONVERTER.toObject(jsonArray));
        }
        for (final Map.Entry<String, JsonElement> entry : json.entrySet()) {
            final String key = entry.getKey();
            if (key.endsWith("Expression")) {
                final String expression = JsonUtil.getString(json, key);
                query.addExpression(key.substring(0, key.length() - "Expression".length()), expression);
            }
        }
        return query;
    }
    
    protected String[] getArray(final JsonArray array) {
        return this.getList(array).toArray(new String[array.size()]);
    }
    
    protected List<String> getList(final JsonArray array) {
        final List<String> list = new ArrayList<String>();
        for (final JsonElement entry : array) {
            list.add(JsonUtil.getString(entry));
        }
        return list;
    }
    
    protected void addVariables(final TaskQueryImpl query, final JsonArray variables, final boolean isTaskVariable, final boolean isProcessVariable) {
        for (final JsonElement variable : variables) {
            final JsonObject variableObj = JsonUtil.getObject(variable);
            final String name = JsonUtil.getString(variableObj, "name");
            final Object rawValue = JsonUtil.getRawObject(variableObj, "value");
            final QueryOperator operator = QueryOperator.valueOf(JsonUtil.getString(variableObj, "operator"));
            query.addVariable(name, rawValue, operator, isTaskVariable, isProcessVariable);
        }
    }
    
    static {
        JsonTaskQueryConverter.variableValueConverter = new JsonTaskQueryVariableValueConverter();
    }
}
