// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import org.zik.bpm.engine.impl.migration.MigrationPlanImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.migration.MigrationPlan;

public class MigrationPlanJsonConverter extends JsonObjectConverter<MigrationPlan>
{
    public static final MigrationPlanJsonConverter INSTANCE;
    public static final String SOURCE_PROCESS_DEFINITION_ID = "sourceProcessDefinitionId";
    public static final String TARGET_PROCESS_DEFINITION_ID = "targetProcessDefinitionId";
    public static final String INSTRUCTIONS = "instructions";
    
    @Override
    public JsonObject toJsonObject(final MigrationPlan migrationPlan) {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addField(json, "sourceProcessDefinitionId", migrationPlan.getSourceProcessDefinitionId());
        JsonUtil.addField(json, "targetProcessDefinitionId", migrationPlan.getTargetProcessDefinitionId());
        JsonUtil.addListField(json, "instructions", MigrationInstructionJsonConverter.INSTANCE, migrationPlan.getInstructions());
        return json;
    }
    
    @Override
    public MigrationPlan toObject(final JsonObject json) {
        final MigrationPlanImpl migrationPlan = new MigrationPlanImpl(JsonUtil.getString(json, "sourceProcessDefinitionId"), JsonUtil.getString(json, "targetProcessDefinitionId"));
        migrationPlan.setInstructions(JsonUtil.asList(JsonUtil.getArray(json, "instructions"), (JsonObjectConverter<MigrationInstruction>)MigrationInstructionJsonConverter.INSTANCE));
        return migrationPlan;
    }
    
    static {
        INSTANCE = new MigrationPlanJsonConverter();
    }
}
