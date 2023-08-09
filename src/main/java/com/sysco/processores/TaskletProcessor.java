package com.sysco.processores;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;

public interface TaskletProcessor {
    void process(StepContribution contribution, ChunkContext chunkContext);
}
