package org.camunda.bpm.run.property;

import java.util.Collections;
import java.util.Map;

public class BpmRunProcessEnginePluginProperty {
    protected String pluginClass;

    protected Map<String, Object> pluginParameters = Collections.EMPTY_MAP;

    public String getPluginClass() {
        return this.pluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.pluginClass = pluginClass;
    }

    public Map<String, Object> getPluginParameters() {
        return this.pluginParameters;
    }

    public void setPluginParameters(Map<String, Object> pluginParameters) {
        this.pluginParameters = pluginParameters;
    }

    public String toString() {
        return "CamundaBpmRunProcessEnginePluginProperty [pluginClass=" + this.pluginClass + ", pluginParameters=" + this.pluginParameters + "]";
    }
}
