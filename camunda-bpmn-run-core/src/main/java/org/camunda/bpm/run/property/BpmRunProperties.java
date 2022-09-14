package org.camunda.bpm.run.property;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("camunda.bpm.run")
public class BpmRunProperties {
    public static final String PREFIX = "camunda.bpm.run";

    @NestedConfigurationProperty
    protected BpmRunAuthenticationProperties auth = new BpmRunAuthenticationProperties();

    @NestedConfigurationProperty
    protected BpmRunCorsProperty cors = new BpmRunCorsProperty();

    @NestedConfigurationProperty
    protected BpmRunLdapProperties ldap = new BpmRunLdapProperties();

    @NestedConfigurationProperty
    protected List<BpmRunProcessEnginePluginProperty> processEnginePlugins = new ArrayList<>();

    protected BpmRunAdministratorAuthorizationProperties adminAuth = new BpmRunAdministratorAuthorizationProperties();

    public BpmRunAuthenticationProperties getAuth() {
        return this.auth;
    }

    public void setAuth(BpmRunAuthenticationProperties auth) {
        this.auth = auth;
    }

    public BpmRunCorsProperty getCors() {
        return this.cors;
    }

    public void setCors(BpmRunCorsProperty cors) {
        this.cors = cors;
    }

    public BpmRunLdapProperties getLdap() {
        return this.ldap;
    }

    public void setLdap(BpmRunLdapProperties ldap) {
        this.ldap = ldap;
    }

    public BpmRunAdministratorAuthorizationProperties getAdminAuth() {
        return this.adminAuth;
    }

    public void setAdminAuth(BpmRunAdministratorAuthorizationProperties adminAuth) {
        this.adminAuth = adminAuth;
    }

    public List<BpmRunProcessEnginePluginProperty> getProcessEnginePlugins() {
        return this.processEnginePlugins;
    }

    public void setProcessEnginePlugins(List<BpmRunProcessEnginePluginProperty> processEnginePlugins) {
        this.processEnginePlugins = processEnginePlugins;
    }

    public String toString() {
        return "BpmRunProperties [auth=" + this.auth + ", cors=" + this.cors + ", ldap=" + this.ldap + ", adminAuth=" + this.adminAuth + ", plugins=" + this.processEnginePlugins + "]";
    }
}
