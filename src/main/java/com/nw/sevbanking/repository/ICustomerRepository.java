package com.nw.sevbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nw.sevbanking.database.CustomerDTO;

/**
 * Interface for the customer repository
 * 
 * @author nicowickersheim
 *
 */

@Repository
public interface ICustomerRepository extends JpaRepository<CustomerDTO, Long> {

}
