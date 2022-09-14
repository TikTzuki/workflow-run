// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;

public interface TaskFormHandler extends FormHandler
{
    TaskFormData createTaskForm(final TaskEntity p0);
}
