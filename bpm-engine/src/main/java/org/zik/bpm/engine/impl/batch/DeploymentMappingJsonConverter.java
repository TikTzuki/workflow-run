// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;

public class DeploymentMappingJsonConverter extends JsonObjectConverter<DeploymentMapping>
{
    public static final DeploymentMappingJsonConverter INSTANCE;
    protected static final String COUNT = "count";
    protected static final String DEPLOYMENT_ID = "deploymentId";
    
    @Override
    public JsonObject toJsonObject(final DeploymentMapping mapping) {
        final JsonObject json = JsonUtil.createObject();
        json.addProperty("deploymentId", mapping.getDeploymentId());
        json.addProperty("count", mapping.getCount());
        return json;
    }
    
    @Override
    public DeploymentMapping toObject(final JsonObject json) {
        final String deploymentId = JsonUtil.isNull(json, "deploymentId") ? null : JsonUtil.getString(json, "deploymentId");
        final int count = JsonUtil.getInt(json, "count");
        return new DeploymentMapping(deploymentId, count);
    }
    
    static {
        INSTANCE = new DeploymentMappingJsonConverter();
    }
}
