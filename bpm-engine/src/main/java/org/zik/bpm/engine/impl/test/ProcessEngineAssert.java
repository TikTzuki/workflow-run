// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.test;

import org.zik.bpm.engine.query.Query;
import junit.framework.AssertionFailedError;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.ProcessEngine;

public class ProcessEngineAssert
{
    public static void assertProcessEnded(final ProcessEngine processEngine, final String processInstanceId) {
        final ProcessInstance processInstance = ((Query<T, ProcessInstance>)processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId)).singleResult();
        if (processInstance != null) {
            throw new AssertionFailedError("expected finished process instance '" + processInstanceId + "' but it was still in the db");
        }
    }
}
