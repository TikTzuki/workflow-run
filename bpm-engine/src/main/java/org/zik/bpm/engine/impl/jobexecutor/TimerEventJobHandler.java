// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;

public abstract class TimerEventJobHandler implements JobHandler<TimerEventJobHandler.TimerJobConfiguration> {
    public static final String JOB_HANDLER_CONFIG_PROPERTY_DELIMITER = "$";
    public static final String JOB_HANDLER_CONFIG_PROPERTY_FOLLOW_UP_JOB_CREATED = "followUpJobCreated";
    public static final String JOB_HANDLER_CONFIG_TASK_LISTENER_PREFIX = "taskListener~";

    @Override
    public TimerJobConfiguration newConfiguration(final String canonicalString) {
        final String[] configParts = canonicalString.split("\\$");
        if (configParts.length > 3) {
            throw new ProcessEngineException("Illegal timer job handler configuration: '" + canonicalString + "': exprecting a one, two or three part configuration seperated by '" + "$" + "'.");
        }
        final TimerJobConfiguration configuration = new TimerJobConfiguration();
        configuration.timerElementKey = configParts[0];
        for (int i = 1; i < configParts.length; ++i) {
            this.adjustConfiguration(configuration, configParts[i]);
        }
        return configuration;
    }

    protected void adjustConfiguration(final TimerJobConfiguration configuration, final String configPart) {
        if (configPart.startsWith("taskListener~")) {
            configuration.setTimerElementSecondaryKey(configPart.substring("taskListener~".length()));
        } else {
            configuration.followUpJobCreated = "followUpJobCreated".equals(configPart);
        }
    }

    @Override
    public void onDelete(final TimerJobConfiguration configuration, final JobEntity jobEntity) {
    }

    public static class TimerJobConfiguration implements JobHandlerConfiguration {
        protected String timerElementKey;
        protected String timerElementSecondaryKey;
        protected boolean followUpJobCreated;

        public String getTimerElementKey() {
            return this.timerElementKey;
        }

        public void setTimerElementKey(final String timerElementKey) {
            this.timerElementKey = timerElementKey;
        }

        public boolean isFollowUpJobCreated() {
            return this.followUpJobCreated;
        }

        public void setFollowUpJobCreated(final boolean followUpJobCreated) {
            this.followUpJobCreated = followUpJobCreated;
        }

        public String getTimerElementSecondaryKey() {
            return this.timerElementSecondaryKey;
        }

        public void setTimerElementSecondaryKey(final String timerElementSecondaryKey) {
            this.timerElementSecondaryKey = timerElementSecondaryKey;
        }

        @Override
        public String toCanonicalString() {
            String canonicalString = this.timerElementKey;
            if (this.timerElementSecondaryKey != null) {
                canonicalString = canonicalString + "$taskListener~" + this.timerElementSecondaryKey;
            }
            if (this.followUpJobCreated) {
                canonicalString += "$followUpJobCreated";
            }
            return canonicalString;
        }
    }
}
