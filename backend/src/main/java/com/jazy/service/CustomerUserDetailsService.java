package com.jazy.service;

import com.jazy.customer.CustomerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerDao customerDao;

    private static final Logger logger = LoggerFactory.getLogger(CustomerUserDetailsService.class);

    public CustomerUserDetailsService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return customerDao.selectUserByEmail(username).orElseThrow(() -> {
            logger.error("Username not found: " + username);
            return new UsernameNotFoundException("Username %s not found".formatted(username));
        });
    }
}
