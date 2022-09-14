package org.camunda.bpm.run.property;
import org.camunda.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin;

public class BpmRunAdministratorAuthorizationProperties extends AdministratorAuthorizationPlugin {
    public static final String PREFIX = "camunda.bpm.run.admin-auth";

    boolean enabled = true;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String toString() {
        return "CamundaBpmRunAdministratorAuthorizationProperty [enabled=" + this.enabled + ", administratorGroupName=" + this.administratorGroupName + ", administratorUserName=" + this.administratorUserName + ']';
    }
}
