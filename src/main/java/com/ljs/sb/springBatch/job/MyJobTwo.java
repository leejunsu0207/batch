package com.ljs.sb.springBatch.job;

import com.ljs.sb.springBatch.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration  // Spring batch의 모든 Job은 Configuration으로 등록해야 한다.
public class MyJobTwo {

    private final Logger logger = LoggerFactory.getLogger(MyJobTwo.class);

    JobBuilderFactory jobBuilderFactory;
    StepBuilderFactory stepBuilderFactory;
    EntityManagerFactory entityManagerFactory;

    public MyJobTwo(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean(name = "myJobTwo_job1")
    Job myJobTwo_job1(){
        return jobBuilderFactory.get("myJobTwo_job1")
                .start(myJobTwo_job1_step1())
                .build();
    }

    @Bean
    @JobScope
    Step myJobTwo_job1_step1(){
        return stepBuilderFactory.get("myJobTwo_job1_step1")
                .<Product, Product>chunk(10)
                .reader(reader(null))
                .processor(processor(null))
                .writer(writer(null))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Product> reader(@Value("#{jobParameters[requestDate]}") String requestDate){
        logger.info("[Product Reader start >>>>>>>>>>>>>>>>>>>>>>>>>>>>] : {}", requestDate);
        Map<String, Object> map = new HashMap<>();
        map.put("price", 1000);
        return new JpaPagingItemReaderBuilder<Product>()
                .pageSize(10)
                .parameterValues(map)
                .queryString("SELECT m FROM Product m WHERE m.price >= : price")
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();

    }

    @Bean
    @JobScope
    public ItemProcessor <Product, Product> processor(@Value("#{jobParameters[requestDate]}") String requestDate){
        return item -> {
            logger.info("[Product Process start >>>>>>>>>>>>>>>>>>>>>>>>>>>>] : {}", item);
            item.setName(String.format("%s_%s_%s", "pooney", requestDate, item.getName()));
            return item;
        };

    }

    @Bean
    @JobScope
    public JpaItemWriter<Product> writer(@Value("#{jobParameters[requestDate]}") String requestDate){
            logger.info("[Product Writer start >>>>>>>>>>>>>>>>>>>>>>>>>>>>] : {}", requestDate);
            return new JpaItemWriterBuilder<Product>()
                    .entityManagerFactory(entityManagerFactory)
                    .build();

    }



}
