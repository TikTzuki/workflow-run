// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.externaltask;

import java.util.Map;
import java.util.List;

public interface ExternalTaskQueryTopicBuilder extends ExternalTaskQueryBuilder
{
    ExternalTaskQueryTopicBuilder variables(final String... p0);
    
    ExternalTaskQueryTopicBuilder variables(final List<String> p0);
    
    ExternalTaskQueryTopicBuilder processInstanceVariableEquals(final Map<String, Object> p0);
    
    ExternalTaskQueryTopicBuilder processInstanceVariableEquals(final String p0, final Object p1);
    
    ExternalTaskQueryTopicBuilder businessKey(final String p0);
    
    ExternalTaskQueryTopicBuilder processDefinitionId(final String p0);
    
    ExternalTaskQueryTopicBuilder processDefinitionIdIn(final String... p0);
    
    ExternalTaskQueryTopicBuilder processDefinitionKey(final String p0);
    
    ExternalTaskQueryTopicBuilder processDefinitionKeyIn(final String... p0);
    
    ExternalTaskQueryTopicBuilder processDefinitionVersionTag(final String p0);
    
    ExternalTaskQueryTopicBuilder withoutTenantId();
    
    ExternalTaskQueryTopicBuilder tenantIdIn(final String... p0);
    
    ExternalTaskQueryTopicBuilder enableCustomObjectDeserialization();
    
    ExternalTaskQueryTopicBuilder localVariables();
    
    ExternalTaskQueryTopicBuilder includeExtensionProperties();
}
