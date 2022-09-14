// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import org.zik.bpm.engine.impl.cmd.TransitionInstanceCancellationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityInstanceCancellationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityCancellationCmd;
import org.zik.bpm.engine.impl.cmd.TransitionInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityBeforeInstantiationCmd;
import org.zik.bpm.engine.impl.cmd.ActivityAfterInstantiationCmd;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.cmd.AbstractProcessInstanceModificationCommand;

public class ModificationCmdJsonConverter extends JsonObjectConverter<AbstractProcessInstanceModificationCommand>
{
    public static final ModificationCmdJsonConverter INSTANCE;
    public static final String START_BEFORE = "startBeforeActivity";
    public static final String START_AFTER = "startAfterActivity";
    public static final String START_TRANSITION = "startTransition";
    public static final String CANCEL_ALL = "cancelAllForActivity";
    public static final String CANCEL_CURRENT = "cancelCurrentActiveActivityInstances";
    public static final String CANCEL_ACTIVITY_INSTANCES = "cancelActivityInstances";
    public static final String PROCESS_INSTANCE = "processInstances";
    public static final String CANCEL_TRANSITION_INSTANCES = "cancelTransitionInstances";
    
    @Override
    public JsonObject toJsonObject(final AbstractProcessInstanceModificationCommand command) {
        final JsonObject json = JsonUtil.createObject();
        if (command instanceof ActivityAfterInstantiationCmd) {
            JsonUtil.addField(json, "startAfterActivity", ((ActivityAfterInstantiationCmd)command).getTargetElementId());
        }
        else if (command instanceof ActivityBeforeInstantiationCmd) {
            JsonUtil.addField(json, "startBeforeActivity", ((ActivityBeforeInstantiationCmd)command).getTargetElementId());
        }
        else if (command instanceof TransitionInstantiationCmd) {
            JsonUtil.addField(json, "startTransition", ((TransitionInstantiationCmd)command).getTargetElementId());
        }
        else if (command instanceof ActivityCancellationCmd) {
            JsonUtil.addField(json, "cancelAllForActivity", ((ActivityCancellationCmd)command).getActivityId());
            JsonUtil.addField(json, "cancelCurrentActiveActivityInstances", ((ActivityCancellationCmd)command).isCancelCurrentActiveActivityInstances());
        }
        else if (command instanceof ActivityInstanceCancellationCmd) {
            JsonUtil.addField(json, "cancelActivityInstances", ((ActivityInstanceCancellationCmd)command).getActivityInstanceId());
            JsonUtil.addField(json, "processInstances", ((ActivityInstanceCancellationCmd)command).getProcessInstanceId());
        }
        else if (command instanceof TransitionInstanceCancellationCmd) {
            JsonUtil.addField(json, "cancelTransitionInstances", ((TransitionInstanceCancellationCmd)command).getTransitionInstanceId());
            JsonUtil.addField(json, "processInstances", ((TransitionInstanceCancellationCmd)command).getProcessInstanceId());
        }
        return json;
    }
    
    @Override
    public AbstractProcessInstanceModificationCommand toObject(final JsonObject json) {
        AbstractProcessInstanceModificationCommand cmd = null;
        if (json.has("startBeforeActivity")) {
            cmd = new ActivityBeforeInstantiationCmd(JsonUtil.getString(json, "startBeforeActivity"));
        }
        else if (json.has("startAfterActivity")) {
            cmd = new ActivityAfterInstantiationCmd(JsonUtil.getString(json, "startAfterActivity"));
        }
        else if (json.has("startTransition")) {
            cmd = new TransitionInstantiationCmd(JsonUtil.getString(json, "startTransition"));
        }
        else if (json.has("cancelAllForActivity")) {
            cmd = new ActivityCancellationCmd(JsonUtil.getString(json, "cancelAllForActivity"));
            final boolean cancelCurrentActiveActivityInstances = JsonUtil.getBoolean(json, "cancelCurrentActiveActivityInstances");
            ((ActivityCancellationCmd)cmd).setCancelCurrentActiveActivityInstances(cancelCurrentActiveActivityInstances);
        }
        else if (json.has("cancelActivityInstances")) {
            cmd = new ActivityInstanceCancellationCmd(JsonUtil.getString(json, "processInstances"), JsonUtil.getString(json, "cancelActivityInstances"));
        }
        else if (json.has("cancelTransitionInstances")) {
            cmd = new TransitionInstanceCancellationCmd(JsonUtil.getString(json, "processInstances"), JsonUtil.getString(json, "cancelTransitionInstances"));
        }
        return cmd;
    }
    
    static {
        INSTANCE = new ModificationCmdJsonConverter();
    }
}
