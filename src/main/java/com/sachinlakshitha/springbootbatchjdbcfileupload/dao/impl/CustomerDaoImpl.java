package com.sachinlakshitha.springbootbatchjdbcfileupload.dao.impl;

import com.sachinlakshitha.springbootbatchjdbcfileupload.dao.CustomerDao;
import com.sachinlakshitha.springbootbatchjdbcfileupload.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerDaoImpl implements CustomerDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDatasource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT * FROM CUSTOMER";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getCustomer(rs));
    }

    private Customer getCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getString("ID"));
        customer.setName(rs.getString("NAME"));
        customer.setEmail(rs.getString("EMAIL"));
        customer.setCreatedTime(rs.getTimestamp("CREATED_TIME"));
        return customer;
    }
}
