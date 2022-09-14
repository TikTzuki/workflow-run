// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import com.google.gson.JsonElement;
import org.zik.bpm.engine.impl.migration.MigrationInstructionImpl;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.migration.MigrationInstruction;

public class MigrationInstructionJsonConverter extends JsonObjectConverter<MigrationInstruction>
{
    public static final MigrationInstructionJsonConverter INSTANCE;
    public static final String SOURCE_ACTIVITY_IDS = "sourceActivityIds";
    public static final String TARGET_ACTIVITY_IDS = "targetActivityIds";
    public static final String UPDATE_EVENT_TRIGGER = "updateEventTrigger";
    
    @Override
    public JsonObject toJsonObject(final MigrationInstruction instruction) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addArrayField(json, "sourceActivityIds", new String[] { instruction.getSourceActivityId() });
        JsonUtil.addArrayField(json, "targetActivityIds", new String[] { instruction.getTargetActivityId() });
        JsonUtil.addField(json, "updateEventTrigger", instruction.isUpdateEventTrigger());
        return json;
    }
    
    @Override
    public MigrationInstruction toObject(final JsonObject json) {
        return new MigrationInstructionImpl(this.readSourceActivityId(json), this.readTargetActivityId(json), JsonUtil.getBoolean(json, "updateEventTrigger"));
    }
    
    protected String readSourceActivityId(final JsonObject json) {
        return JsonUtil.getString(JsonUtil.getArray(json, "sourceActivityIds"));
    }
    
    protected String readTargetActivityId(final JsonObject json) {
        return JsonUtil.getString(JsonUtil.getArray(json, "targetActivityIds"));
    }
    
    static {
        INSTANCE = new MigrationInstructionJsonConverter();
    }
}
