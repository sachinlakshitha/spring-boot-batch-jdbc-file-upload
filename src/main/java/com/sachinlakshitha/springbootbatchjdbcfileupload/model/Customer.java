package com.sachinlakshitha.springbootbatchjdbcfileupload.model;

import lombok.Data;

import java.util.Date;

@Data
public class Customer {
    private String id;
    private String name;
    private String email;
    private Date createdTime;
}
