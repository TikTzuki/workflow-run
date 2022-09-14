package org.camunda.bpm.run.property;

public class BpmRunCorsProperty {
    public static final String PREFIX = "camunda.bpm.run.cors";

    public static final String DEFAULT_ORIGINS = "*";

    public static final String DEFAULT_HTTP_METHODS = "GET,POST,HEAD,OPTIONS,PUT,DELETE";

    public static final String DEFAULT_PREFLIGHT_MAXAGE = "1800";

    public static final String DEFAULT_ALLOWED_HTTP_HEADERS = "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers";

    public static final String DEFAULT_EXPOSED_HEADERS = "";

    public static final boolean DEFAULT_ALLOW_CREDENTIALS = false;

    boolean enabled;

    String allowedOrigins = "*";

    String allowedHeaders = "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers";

    String exposedHeaders = "";

    boolean allowCredentials = false;

    String preflightMaxAge = "1800";

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAllowedOrigins() {
        if (this.enabled)
            return (this.allowedOrigins == null) ? "*" : this.allowedOrigins;
        return null;
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public boolean getAllowCredentials() {
        return this.allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public String getAllowedHeaders() {
        return this.allowedHeaders;
    }

    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public String getExposedHeaders() {
        return this.exposedHeaders;
    }

    public void setExposedHeaders(String exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public String getPreflightMaxAge() {
        return this.preflightMaxAge;
    }

    public void setPreflightMaxAge(String preflightMaxAge) {
        this.preflightMaxAge = preflightMaxAge;
    }

    public String toString() {
        return "CamundaBpmRunCorsProperty [enabled=" + this.enabled + ", allowCredentials=" + this.allowCredentials + ", allowedOrigins=" + this.allowedOrigins + ", allowedHeaders=" + this.allowedHeaders + ", exposedHeaders=" + this.exposedHeaders + ", preflightMaxAge=" + this.preflightMaxAge + ']';
    }
}
