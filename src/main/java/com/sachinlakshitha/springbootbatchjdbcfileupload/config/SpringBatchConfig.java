package com.sachinlakshitha.springbootbatchjdbcfileupload.config;

import com.sachinlakshitha.springbootbatchjdbcfileupload.listener.StepSkipListener;
import com.sachinlakshitha.springbootbatchjdbcfileupload.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> itemReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFIle) {
        FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(new File(pathToFIle)));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("name", "email");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public CustomerProcessor processor() {
        return new CustomerProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> writer(DataSource dataSource) {
        JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("INSERT INTO CUSTOMER (ID,NAME,EMAIL) VALUES (UUID(), :name, :email)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public Job importCustomerJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importCustomer", jobRepository)
                //.incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager , FlatFileItemReader<Customer> itemReader, JdbcBatchItemWriter<Customer> writer) {
        return new StepBuilder("slaveStep", jobRepository)
                .<Customer, Customer> chunk(10, transactionManager)
                .reader(itemReader)
                .processor(processor())
                .writer(writer)
                .faultTolerant()
                .listener(skipListener())
                .skipPolicy(skipPolicy())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public SkipPolicy skipPolicy() {
        return new ExceptionSkipPolicy();
    }

    @Bean
    public SkipListener skipListener() {
        return new StepSkipListener();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }
}
