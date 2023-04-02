package com.sachinlakshitha.springbootbatchjdbcfileupload.controller;

import com.sachinlakshitha.springbootbatchjdbcfileupload.dao.CustomerDao;
import com.sachinlakshitha.springbootbatchjdbcfileupload.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final JobLauncher jobLauncher;
    private final Job job;
    private final CustomerDao customerDao;
    @Value("${temp.upload.path}")
    private String tempUploadPath;

    @PostMapping(path = "/upload")
    public void startBatch(@RequestParam MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            File fileToImport = new File(tempUploadPath + originalFileName);
            file.transferTo(fileToImport);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", tempUploadPath + originalFileName)
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);

//            if(execution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED)){
//                //delete the file from the temporary upload path
//                Files.deleteIfExists(Paths.get(tempUploadPath + originalFileName));
//            }

        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerDao.findAll();
    }
}
