// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public enum ResumePreviousBy {
    RESUME_BY_PROCESS_DEFINITION_KEY("process-definition-key"),
    RESUME_BY_DEPLOYMENT_NAME("deployment-name");
//    public static String RESUME_BY_PROCESS_DEFINITION_KEY;// = "process-definition-key";
//    public static String RESUME_BY_DEPLOYMENT_NAME = "deployment-name";

    protected String value;

    private ResumePreviousBy(String value) {
        this.value = value;
    }

}
