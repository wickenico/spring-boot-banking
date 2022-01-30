package com.nw.sevbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nw.sevbanking.database.GiroAccountDTO;


/**
 * Interface for the Giro Account Repository
 * @author nicowickersheim
 * @date 29.01.2022
 */
public interface IGiroAccountRepository extends JpaRepository<GiroAccountDTO, Long> {

	boolean existsByAccountIdAndCustomers_customerId(long accountId, long customerid); 
	
}
