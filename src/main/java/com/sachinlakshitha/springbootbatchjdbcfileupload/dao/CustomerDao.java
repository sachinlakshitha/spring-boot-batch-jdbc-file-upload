package com.sachinlakshitha.springbootbatchjdbcfileupload.dao;

import com.sachinlakshitha.springbootbatchjdbcfileupload.model.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> findAll();
}
