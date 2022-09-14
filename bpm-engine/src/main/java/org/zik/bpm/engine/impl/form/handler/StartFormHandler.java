// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.form.StartFormData;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

public interface StartFormHandler extends FormHandler
{
    StartFormData createStartFormData(final ProcessDefinitionEntity p0);
}
