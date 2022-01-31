package com.nw.sevbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nw.sevbanking.database.FixedDepositAccountDTO;

/**
 * Interface for the Fixed Deposit Account Repository
 * 
 * @author nicowickersheim
 * 
 */

public interface IFixedDepositAccountRepository extends JpaRepository<FixedDepositAccountDTO, Long> {

}
