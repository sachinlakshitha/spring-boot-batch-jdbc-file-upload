package com.sachinlakshitha.springbootbatchjdbcfileupload.config;

import com.sachinlakshitha.springbootbatchjdbcfileupload.model.Customer;
import com.sachinlakshitha.springbootbatchjdbcfileupload.util.Utility;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) {
        boolean validEmail = Utility.isValidEmail(customer.getEmail());
        if (validEmail) {
            return customer;
        }
        return null;
    }
}
