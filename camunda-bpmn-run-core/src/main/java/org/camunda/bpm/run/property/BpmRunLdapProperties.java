package org.camunda.bpm.run.property;
import org.camunda.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin;

public class BpmRunLdapProperties extends LdapIdentityProviderPlugin{
    public static final String PREFIX = "camunda.bpm.run.ldap";

    boolean enabled = true;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String toString() {
        return "CamundaBpmRunLdapProperty [enabled=" + this.enabled + ", initialContextFactory=" + this.initialContextFactory + ", securityAuthentication=" + this.securityAuthentication + ", contextProperties=" + this.contextProperties + ", serverUrl=******, managerDn=******, managerPassword=******, baseDn=" + this.baseDn + ", userDnPattern=" + this.userDnPattern + ", userSearchBase=" + this.userSearchBase + ", userSearchFilter=" + this.userSearchFilter + ", groupSearchBase=" + this.groupSearchBase + ", groupSearchFilter=" + this.groupSearchFilter + ", userIdAttribute=" + this.userIdAttribute + ", userFirstnameAttribute=" + this.userFirstnameAttribute + ", userLastnameAttribute=" + this.userLastnameAttribute + ", userEmailAttribute=" + this.userEmailAttribute + ", userPasswordAttribute=" + this.userPasswordAttribute + ", groupIdAttribute=" + this.groupIdAttribute + ", groupNameAttribute=" + this.groupNameAttribute + ", groupTypeAttribute=" + this.groupTypeAttribute + ", groupMemberAttribute=" + this.groupMemberAttribute + ", sortControlSupported=" + this.sortControlSupported + ", useSsl=" + this.useSsl + ", usePosixGroups=" + this.usePosixGroups + ", allowAnonymousLogin=" + this.allowAnonymousLogin + ", authorizationCheckEnabled=" + this.authorizationCheckEnabled + "]";
    }
}
