package com.sysco.readers;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
public class FileItemReader implements ItemReader<Void>, StepExecutionListener {

    private JobParameters jobParameters;

    @Override
    public Void read() throws Exception {


        System.err.println("it is reading data.........");
        return null;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.jobParameters = stepExecution.getJobParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
