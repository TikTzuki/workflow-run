package org.camunda.bpm.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Date;

@SpringBootApplication
public class CamundaBpmRun {
    public static void main(String... args) {
        SpringApplication.run(org.camunda.bpm.run.CamundaBpmRun.class, args);
    }

    class JobClient {
        public JobClient startProcess() {
            return null;
        }

        public JobClient processId(String id) {
            return null;
        }

        public JobClient completeTask() {
            return null;
        }


        public JobClient variables(Object vars) {
            return null;
        }

        public void commit() {
        }
    }

    class ActivatedJob {

    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface WorkflowWorker {
    }


    @PutMapping("/approve/employee")
    public ResponseEntity<Void> approveEmployee(JobClient client) {
        // Write code approve employee
        // database.save(employee)

        // Workflow
        client.startProcess()
                .processId("approve-employee")
                .commit();

        return ResponseEntity.noContent().build();
    }

    @WorkflowWorker()
    public void sendEmploymentOfferMail(JobClient client, ActivatedJob job) {
        // Write code SEND MAIL, do st ...
        // sendMail(employeeName, employeeMail);

        // Workflow
        client.completeTask()
                .variables(Collections.singletonMap("JOIN_DATE", new Date(2022, 10, 10)))
                .commit();

    }
}
