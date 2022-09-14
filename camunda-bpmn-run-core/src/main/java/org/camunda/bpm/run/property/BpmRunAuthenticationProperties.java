package org.camunda.bpm.run.property;
import java.util.Arrays;
import java.util.List;

public class BpmRunAuthenticationProperties {
    public static final String PREFIX = "camunda.bpm.run.auth";

    public static final String DEFAULT_AUTH = "basic";

    public static final List<String> AUTH_METHODS = Arrays.asList(new String[]{"basic"});

    boolean enabled;

    String authentication = "basic";

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAuthentication() {
        return this.authentication;
    }

    public void setAuthentication(String authentication) {
        if (authentication != null && !AUTH_METHODS.contains(authentication))
            throw new RuntimeException("Please provide a valid authentication method. The available ones are: " + AUTH_METHODS.toString());
        this.authentication = authentication;
    }

    public String toString() {
        return "CamundaBpmRunAuthenticationProperties [enabled=" + this.enabled + ", authentication=" + this.authentication + "]";
    }
}
