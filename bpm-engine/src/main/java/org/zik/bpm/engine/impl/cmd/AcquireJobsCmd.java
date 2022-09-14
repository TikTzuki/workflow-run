// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbEntityOperation;
import org.zik.bpm.engine.impl.db.entitymanager.OptimisticLockingResult;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.persistence.entity.AcquirableJobEntity;
import java.util.List;
import java.util.HashMap;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutor;
import org.zik.bpm.engine.impl.db.entitymanager.OptimisticLockingListener;
import org.zik.bpm.engine.impl.jobexecutor.AcquiredJobs;
import org.zik.bpm.engine.impl.interceptor.Command;

public class AcquireJobsCmd implements Command<AcquiredJobs>, OptimisticLockingListener
{
    private final JobExecutor jobExecutor;
    protected AcquiredJobs acquiredJobs;
    protected int numJobsToAcquire;
    
    public AcquireJobsCmd(final JobExecutor jobExecutor) {
        this(jobExecutor, jobExecutor.getMaxJobsPerAcquisition());
    }
    
    public AcquireJobsCmd(final JobExecutor jobExecutor, final int numJobsToAcquire) {
        this.jobExecutor = jobExecutor;
        this.numJobsToAcquire = numJobsToAcquire;
    }
    
    @Override
    public AcquiredJobs execute(final CommandContext commandContext) {
        this.acquiredJobs = new AcquiredJobs(this.numJobsToAcquire);
        final List<AcquirableJobEntity> jobs = commandContext.getJobManager().findNextJobsToExecute(new Page(0, this.numJobsToAcquire));
        final Map<String, List<String>> exclusiveJobsByProcessInstance = new HashMap<String, List<String>>();
        for (final AcquirableJobEntity job : jobs) {
            this.lockJob(job);
            if (job.isExclusive()) {
                List<String> list = exclusiveJobsByProcessInstance.get(job.getProcessInstanceId());
                if (list == null) {
                    list = new ArrayList<String>();
                    exclusiveJobsByProcessInstance.put(job.getProcessInstanceId(), list);
                }
                list.add(job.getId());
            }
            else {
                this.acquiredJobs.addJobIdBatch(job.getId());
            }
        }
        for (final List<String> jobIds : exclusiveJobsByProcessInstance.values()) {
            this.acquiredJobs.addJobIdBatch(jobIds);
        }
        commandContext.getDbEntityManager().registerOptimisticLockingListener(this);
        return this.acquiredJobs;
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
    
    protected void lockJob(final AcquirableJobEntity job) {
        final String lockOwner = this.jobExecutor.getLockOwner();
        job.setLockOwner(lockOwner);
        final int lockTimeInMillis = this.jobExecutor.getLockTimeInMillis();
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(ClockUtil.getCurrentTime());
        gregorianCalendar.add(14, lockTimeInMillis);
        job.setLockExpirationTime(gregorianCalendar.getTime());
    }
    
    @Override
    public Class<? extends DbEntity> getEntityType() {
        return AcquirableJobEntity.class;
    }
    
    @Override
    public OptimisticLockingResult failedOperation(final DbOperation operation) {
        if (operation instanceof DbEntityOperation) {
            final DbEntityOperation entityOperation = (DbEntityOperation)operation;
            this.acquiredJobs.removeJobId(entityOperation.getEntity().getId());
            return OptimisticLockingResult.IGNORE;
        }
        return OptimisticLockingResult.THROW;
    }
}
