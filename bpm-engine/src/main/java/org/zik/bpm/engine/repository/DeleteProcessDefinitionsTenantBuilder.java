// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public interface DeleteProcessDefinitionsTenantBuilder extends DeleteProcessDefinitionsBuilder
{
    DeleteProcessDefinitionsBuilder withoutTenantId();
    
    DeleteProcessDefinitionsBuilder withTenantId(final String p0);
}
