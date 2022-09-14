package org.zik.bpm;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.container.RuntimeContainerDelegate;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.delegate.DelegateListener;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.VariableStore;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntityReferencer;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.runtime.VariableInstance;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class BpmPlatform {
    public static final String JNDI_NAME_PREFIX = "java:global";
    public static final String APP_JNDI_NAME = "camunda-bpm-platform";
    public static final String MODULE_JNDI_NAME = "process-engine";
    public static final String PROCESS_ENGINE_SERVICE_NAME = "ProcessEngineService!org.camunda.bpm.ProcessEngineService";
    public static final String PROCESS_APPLICATION_SERVICE_NAME = "ProcessApplicationService!org.camunda.bpm.ProcessApplicationService";
    public static final String PROCESS_ENGINE_SERVICE_JNDI_NAME = "java:global/camunda-bpm-platform/process-engine/ProcessEngineService!org.camunda.bpm.ProcessEngineService";
    public static final String PROCESS_APPLICATION_SERVICE_JNDI_NAME = "java:global/camunda-bpm-platform/process-engine/ProcessApplicationService!org.camunda.bpm.ProcessApplicationService";

    public static ProcessEngineService getProcessEngineService() {
        return RuntimeContainerDelegate.INSTANCE.get().getProcessEngineService();
    }

    public static ProcessApplicationService getProcessApplicationService() {
        return RuntimeContainerDelegate.INSTANCE.get().getProcessApplicationService();
    }

    public static ProcessEngine getDefaultProcessEngine() {
        return getProcessEngineService().getDefaultProcessEngine();
    }

    // TEST

    public static void main(String[] args) {
    }
}
