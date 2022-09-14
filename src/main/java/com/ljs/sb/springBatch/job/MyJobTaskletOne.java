package com.ljs.sb.springBatch.job;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyJobTaskletOne {

    private final Logger logger = LoggerFactory.getLogger(MyJobTaskletOne.class);

    JobBuilderFactory jobBuilderFactory;
    StepBuilderFactory stepBuilderFactory;

    public MyJobTaskletOne(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(name = "myJobTaskletOne_Job1")
    public Job myJobTaskletOne_Job1(){
        return this.jobBuilderFactory.get("myJobTaskletOne_Job1")
                .start(myJobTaskletOne_Job1_Step1())
                .next(myJobTaskletOne_Job1_Step2())
                .build();
    }

    @Bean
    public Step myJobTaskletOne_Job1_Step1(){
        return stepBuilderFactory.get("myJobTaskletOne_Job1_Step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("myJobTaskletOne_Job1_Step1 >>>>>>>>>>>>>>");
                    logger.info("myJobTaskletOne_Job1_Step1 >>>>>>>>>>>>>>");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step myJobTaskletOne_Job1_Step2(){
        return stepBuilderFactory.get("myJobTaskletOne_Job1_Step2")
                .tasklet((contribution, chunkContext) -> {
                    logger.info("myJobTaskletOne_Job1_Step2 >>>>>>>>>>>>>>");
                    System.out.println("myJobTaskletOne_Job1_Step2 >>>>>>>>>>>>>>");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }



}
