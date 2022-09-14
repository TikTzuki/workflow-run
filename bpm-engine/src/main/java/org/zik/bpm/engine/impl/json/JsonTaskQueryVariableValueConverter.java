// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import org.zik.bpm.engine.impl.QueryOperator;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.TaskQueryVariableValue;

public class JsonTaskQueryVariableValueConverter extends JsonObjectConverter<TaskQueryVariableValue>
{
    @Override
    public JsonObject toJsonObject(final TaskQueryVariableValue variable) {
        final JsonObject jsonObject = JsonUtil.createObject();
        JsonUtil.addField(jsonObject, "name", variable.getName());
        JsonUtil.addFieldRawValue(jsonObject, "value", variable.getValue());
        JsonUtil.addField(jsonObject, "operator", variable.getOperator().name());
        return jsonObject;
    }
    
    @Override
    public TaskQueryVariableValue toObject(final JsonObject json) {
        final String name = JsonUtil.getString(json, "name");
        final Object value = JsonUtil.getRawObject(json, "value");
        final QueryOperator operator = QueryOperator.valueOf(JsonUtil.getString(json, "operator"));
        final boolean isTaskVariable = JsonUtil.getBoolean(json, "taskVariable");
        final boolean isProcessVariable = JsonUtil.getBoolean(json, "processVariable");
        return new TaskQueryVariableValue(name, value, operator, isTaskVariable, isProcessVariable);
    }
}
