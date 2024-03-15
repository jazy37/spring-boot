package com.jazy.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = "SELECT id, name, email, age, gender FROM customer";
        return jdbcTemplate.query(sql, customerRowMapper);
    }
    @Override
    public Optional<Customer> findCustomerById(long id) {
        var sql = "SELECT id, name, email, age, gender FROM customer WHERE id = ?";
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void saveCustomer(Customer customer) {
        var sql = "INSERT INTO customer(name, email, age, gender) VALUES (?, ?, ?, ?)";
        int results = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge(), String.valueOf(customer.getGender()));
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        String sql = "SELECT count(id) FROM customer WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerById(long id) {
        String sql = "SELECT count(id) FROM customer WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomer(long id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() != null) {
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            jdbcTemplate.update(sql, customer.getName(), customer.getId());
        }
        if(customer.getEmail() != null) {
            String sql = "UPDATE customer SET email = ? WHERE id = ?";
            jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
        }
        if(customer.getAge() != null) {
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            jdbcTemplate.update(sql, customer.getAge(), customer.getId());
        }
    }
}
