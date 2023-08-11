package com.sysco;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class CleanerApplicationTests {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;


	@Test
	void contextLoads() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {


		JobParameter jobParameter = new JobParameter("D:\\data\\data1\\data1.xlsx");

		jobLauncher.run(job, new JobParameters(Map.of("fileName", jobParameter)));
	}

}
