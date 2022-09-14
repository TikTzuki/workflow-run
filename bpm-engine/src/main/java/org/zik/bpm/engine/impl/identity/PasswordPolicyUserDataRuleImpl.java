// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import org.zik.bpm.engine.identity.User;
import java.util.Map;
import org.zik.bpm.engine.identity.PasswordPolicyRule;

public class PasswordPolicyUserDataRuleImpl implements PasswordPolicyRule
{
    public static final String PLACEHOLDER = "PASSWORD_POLICY_USER_DATA";
    
    @Override
    public String getPlaceholder() {
        return "PASSWORD_POLICY_USER_DATA";
    }
    
    @Override
    public Map<String, String> getParameters() {
        return null;
    }
    
    @Override
    public boolean execute(final String password) {
        return false;
    }
    
    @Override
    public boolean execute(String candidatePassword, final User user) {
        if (candidatePassword.isEmpty() || user == null) {
            return true;
        }
        candidatePassword = this.upperCase(candidatePassword);
        final String id = this.upperCase(user.getId());
        final String firstName = this.upperCase(user.getFirstName());
        final String lastName = this.upperCase(user.getLastName());
        final String email = this.upperCase(user.getEmail());
        return (!this.isNotBlank(id) || !candidatePassword.contains(id)) && (!this.isNotBlank(firstName) || !candidatePassword.contains(firstName)) && (!this.isNotBlank(lastName) || !candidatePassword.contains(lastName)) && (!this.isNotBlank(email) || !candidatePassword.contains(email));
    }
    
    public String upperCase(final String string) {
        return (string == null) ? null : string.toUpperCase();
    }
    
    public boolean isNotBlank(final String value) {
        return value != null && !value.isEmpty();
    }
}
