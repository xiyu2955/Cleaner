package com.sysco.config;

import com.sysco.processores.TaskletProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {


    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;


    @Bean
    public BatchConfigurer batchConfigurer(@Qualifier("batchDataSource") DataSource batchDataSource) {
        return new DefaultBatchConfigurer(batchDataSource) {
            @Override
            public PlatformTransactionManager getTransactionManager() {
                return new DataSourceTransactionManager(batchDataSource);
            }
        };
    }

    // tag::jobstep[]
    @Bean
    public Job job(@Qualifier("step1") Step step1,
                   @Autowired JobExecutionListener jobCompletionNotificationListener) {
        return jobs.get("myJob")
                .listener(jobCompletionNotificationListener)
                .start(step1)
                .build();
    }

    @Bean
    protected Step step1(@Qualifier("excelTaskletProcessor") TaskletProcessor processor) {
        return steps.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    processor.process(contribution, chunkContext);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    // end::jobstep[]


}
