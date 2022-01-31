package com.nw.sevbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nw.sevbanking.database.GiroAccountDTO;

/**
 * Interface for the Giro Account Repository
 * 
 * @author nicowickersheim
 * 
 */

public interface IGiroAccountRepository extends JpaRepository<GiroAccountDTO, Long> {

	boolean existsByAccountIdAndCustomers_customerId(long accountId, long customerid);

}
