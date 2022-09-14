// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics.util;

public class MetricsUtil
{
    public static String resolveInternalName(final String publicName) {
        if (publicName == null) {
            return null;
        }
        switch (publicName) {
            case "task-users": {
                return "unique-task-workers";
            }
            case "process-instances": {
                return "root-process-instance-start";
            }
            case "decision-instances": {
                return "executed-decision-instances";
            }
            case "flow-node-instances": {
                return "activity-instance-start";
            }
            default: {
                return publicName;
            }
        }
    }
    
    public static String resolvePublicName(final String internalName) {
        if (internalName == null) {
            return null;
        }
        switch (internalName) {
            case "unique-task-workers": {
                return "task-users";
            }
            case "root-process-instance-start": {
                return "process-instances";
            }
            case "executed-decision-instances": {
                return "decision-instances";
            }
            case "activity-instance-start": {
                return "flow-node-instances";
            }
            default: {
                return internalName;
            }
        }
    }
}
