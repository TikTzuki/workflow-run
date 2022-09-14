// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.test;

import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.cmmn.behavior.CaseControlRuleImpl;
import org.zik.bpm.engine.impl.el.FixedValue;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class PvmTestCase extends TestCase
{
    public void assertTextPresent(final String expected, final String actual) {
        if (actual == null || actual.indexOf(expected) == -1) {
            throw new AssertionFailedError("expected presence of [" + expected + "], but was [" + actual + "]");
        }
    }
    
    public void assertTextPresentIgnoreCase(final String expected, final String actual) {
        this.assertTextPresent(expected.toLowerCase(), actual.toLowerCase());
    }
    
    public Object defaultManualActivation() {
        final Expression expression = new FixedValue(true);
        final CaseControlRuleImpl caseControlRule = new CaseControlRuleImpl(expression);
        return caseControlRule;
    }
}
