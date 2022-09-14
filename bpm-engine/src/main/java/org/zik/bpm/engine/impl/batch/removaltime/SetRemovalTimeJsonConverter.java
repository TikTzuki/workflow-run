// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.removaltime;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import com.google.gson.JsonElement;
import java.util.Date;
import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappingJsonConverter;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class SetRemovalTimeJsonConverter extends JsonObjectConverter<SetRemovalTimeBatchConfiguration>
{
    public static final SetRemovalTimeJsonConverter INSTANCE;
    protected static final String IDS = "ids";
    protected static final String ID_MAPPINGS = "idMappings";
    protected static final String REMOVAL_TIME = "removalTime";
    protected static final String HAS_REMOVAL_TIME = "hasRemovalTime";
    protected static final String IS_HIERARCHICAL = "isHierarchical";
    
    @Override
    public JsonObject toJsonObject(final SetRemovalTimeBatchConfiguration configuration) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addListField(json, "ids", configuration.getIds());
        JsonUtil.addListField(json, "idMappings", (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, (List<Object>)configuration.getIdMappings());
        JsonUtil.addDateField(json, "removalTime", configuration.getRemovalTime());
        JsonUtil.addField(json, "hasRemovalTime", configuration.hasRemovalTime());
        JsonUtil.addField(json, "isHierarchical", configuration.isHierarchical());
        return json;
    }
    
    @Override
    public SetRemovalTimeBatchConfiguration toObject(final JsonObject jsonObject) {
        final long removalTimeMills = JsonUtil.getLong(jsonObject, "removalTime");
        final Date removalTime = (removalTimeMills > 0L) ? new Date(removalTimeMills) : null;
        final List<String> instanceIds = JsonUtil.asStringList(JsonUtil.getArray(jsonObject, "ids"));
        final DeploymentMappings mappings = JsonUtil.asList(JsonUtil.getArray(jsonObject, "idMappings"), (JsonObjectConverter<Object>)DeploymentMappingJsonConverter.INSTANCE, DeploymentMappings::new);
        final boolean hasRemovalTime = JsonUtil.getBoolean(jsonObject, "hasRemovalTime");
        final boolean isHierarchical = JsonUtil.getBoolean(jsonObject, "isHierarchical");
        return new SetRemovalTimeBatchConfiguration(instanceIds, mappings).setRemovalTime(removalTime).setHasRemovalTime(hasRemovalTime).setHierarchical(isHierarchical);
    }
    
    static {
        INSTANCE = new SetRemovalTimeJsonConverter();
    }
}
