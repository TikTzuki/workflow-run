// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl.event;

import java.util.List;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;

public class ProcessApplicationEventListenerPlugin extends AbstractProcessEnginePlugin
{
    @Override
    public void preInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> preParseListeners = processEngineConfiguration.getCustomPreBPMNParseListeners();
        if (preParseListeners == null) {
            preParseListeners = new ArrayList<BpmnParseListener>();
            processEngineConfiguration.setCustomPreBPMNParseListeners(preParseListeners);
        }
        preParseListeners.add(new ProcessApplicationEventParseListener());
    }
}
