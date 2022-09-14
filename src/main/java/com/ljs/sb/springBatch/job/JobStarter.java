package com.ljs.sb.springBatch.job;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
public class JobStarter {

    JobLauncher jobLauncher;
    Job myJobTaskletOne_Job1;
    Job myJobTwo_job1;

    public JobStarter(JobLauncher jobLauncher, Job myJobTaskletOne_Job1, Job myJobTwo_job1) {
        this.jobLauncher = jobLauncher;
        this.myJobTaskletOne_Job1 = myJobTaskletOne_Job1;
        this.myJobTwo_job1 = myJobTwo_job1;
    }

    @Scheduled(fixedDelay=5000)
    public void TaskletStart() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters parameters = new JobParametersBuilder()
                .addString("requestDate", LocalDateTime.now().toString()).toJobParameters();
        JobExecution execution = jobLauncher.run(myJobTaskletOne_Job1, parameters);
    }

    @Scheduled(fixedDelay=3000)
    public void JobStart() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters parameters = new JobParametersBuilder()
                .addString("requestDate", LocalDateTime.now().toString()).toJobParameters();
        JobExecution execution = jobLauncher.run(myJobTwo_job1, parameters);
    }


}
