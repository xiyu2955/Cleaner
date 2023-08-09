package com.sysco.listeners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.err.println("it is listering before");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.err.println("it is listering after");
    }
}

