// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.test;

import org.slf4j.Logger;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class TestLogger extends ProcessEngineLogger
{
    public Logger getLogger() {
        return this.delegateLogger;
    }
}
