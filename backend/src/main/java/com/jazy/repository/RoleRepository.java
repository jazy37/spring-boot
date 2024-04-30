package com.jazy.repository;

import com.jazy.customer.ERole;
import com.jazy.customer.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(ERole role);
}
