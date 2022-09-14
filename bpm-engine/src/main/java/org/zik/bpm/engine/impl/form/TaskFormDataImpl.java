// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.form.TaskFormData;

public class TaskFormDataImpl extends FormDataImpl implements TaskFormData
{
    private static final long serialVersionUID = 1L;
    protected Task task;
    
    @Override
    public Task getTask() {
        return this.task;
    }
    
    public void setTask(final Task task) {
        this.task = task;
    }
}
