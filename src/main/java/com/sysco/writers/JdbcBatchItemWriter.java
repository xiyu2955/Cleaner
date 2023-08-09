package com.sysco.writers;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcBatchItemWriter implements ItemWriter<Void> {

    @Override
    public void write(List<? extends Void> items) throws Exception {
        System.err.println("it is writing.........");

    }
}
