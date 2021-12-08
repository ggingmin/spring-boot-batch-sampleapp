package com.ggingmin.springbatch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.Date;

public class PaymentJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution je) {
        System.out.println("Starting at " + new Date());
        System.out.println("Start Status : " + je.getStatus());
    }

    @Override
    public void afterJob(JobExecution je) {
        System.out.println("Ended at " + new Date());
        System.out.println("End Status : " + je.getStatus());
    }
}
