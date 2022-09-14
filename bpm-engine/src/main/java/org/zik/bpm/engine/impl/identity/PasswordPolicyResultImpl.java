// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import org.zik.bpm.engine.identity.PasswordPolicyRule;
import java.util.List;
import org.zik.bpm.engine.identity.PasswordPolicyResult;

public class PasswordPolicyResultImpl implements PasswordPolicyResult
{
    protected List<PasswordPolicyRule> violatedRules;
    protected List<PasswordPolicyRule> fulfilledRules;
    
    public PasswordPolicyResultImpl(final List<PasswordPolicyRule> violatedRules, final List<PasswordPolicyRule> fulfilledRules) {
        this.violatedRules = violatedRules;
        this.fulfilledRules = fulfilledRules;
    }
    
    @Override
    public boolean isValid() {
        return this.violatedRules == null || this.violatedRules.size() == 0;
    }
    
    @Override
    public List<PasswordPolicyRule> getViolatedRules() {
        return this.violatedRules;
    }
    
    public void setViolatedRules(final List<PasswordPolicyRule> violatedRules) {
        this.violatedRules = violatedRules;
    }
    
    @Override
    public List<PasswordPolicyRule> getFulfilledRules() {
        return this.fulfilledRules;
    }
    
    public void setFulfilledRules(final List<PasswordPolicyRule> fulfilledRules) {
        this.fulfilledRules = fulfilledRules;
    }
}
