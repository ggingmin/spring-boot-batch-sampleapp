package com.ggingmin.springbatch.config;

import com.ggingmin.springbatch.entity.Payment;
import com.ggingmin.springbatch.listener.PaymentJobListener;
import com.ggingmin.springbatch.processor.PaymentProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    /* 1. 파일에서 데이터 읽어오기 */
    public FlatFileItemReader<Payment> reader() {
        FlatFileItemReader<Payment> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("payments.csv"));
//        reader.setResource(new FileSystemResource("/ggigmin/Documents/payments.csv"));
//        reader.setResource(new UrlResource("https://ggingmin.com/files/payments.csv"));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter(DELIMITER_COMMA);
                setNames("paymentId","customerId","customerName","prodName","prodPrice");
            }});

            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(Payment.class);
            }});
        }});

        return reader;
    }

    /* 2.  */
    public ItemProcessor<Payment, Payment> processor() {
//        return item -> {
//            double price = item.getProdPrice();
//            item.setPaymentDisc(price * 20/100.0);
//            item.setPaymentGst(price * 22/100.0);
//            return item;
//        };
        return new PaymentProcessor();
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<Payment> writer() {
        JdbcBatchItemWriter<Payment> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO PAYMENTS (PAYMENT_ID, CUSTOMER_ID, CUSTOMER_NAME, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DISCOUNT) VALUES (:paymentId, :customerId, :customerName, :prodName, :prodPrice, :paymentDisc)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        return writer;
    }

    @Bean
    public JobExecutionListener listener() {
        return new PaymentJobListener();
    }

    @Autowired
    private StepBuilderFactory sf;

    @Bean
    public Step stepA() {
        return sf.get("stepA")
                .<Payment, Payment>chunk(2)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build()
                ;
    }

    @Autowired
    private JobBuilderFactory jf;

    @Bean
    public Job jobA() {
        return jf.get("jobA")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .start(stepA())
                .build();
    }
}
