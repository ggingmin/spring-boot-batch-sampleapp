package com.ggingmin.springbatch.runner;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PaymentJobRunner implements CommandLineRunner {

    @Autowired
    private JobLauncher launcher;

    @Autowired
    private Job jobA;

    public void run(String... args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        launcher.run(jobA, jobParameters);
        System.out.println("JOB EXECUTION COMPLETED");
    }
}
