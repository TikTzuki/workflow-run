// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.engine;

import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.form.StartFormData;

public interface FormEngine
{
    String getName();
    
    Object renderStartForm(final StartFormData p0);
    
    Object renderTaskForm(final TaskFormData p0);
}
