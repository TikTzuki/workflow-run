// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public interface DeleteProcessDefinitionsSelectBuilder
{
    DeleteProcessDefinitionsBuilder byIds(final String... p0);
    
    DeleteProcessDefinitionsTenantBuilder byKey(final String p0);
}
