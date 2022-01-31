package com.nw.sevbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nw.sevbanking.database.CreditcardDTO;

/**
 * Interface for the creditcard repository
 * 
 * @author nicowickersheim
 *
 */

@Repository
public interface ICreditcardRepository extends JpaRepository<CreditcardDTO, Long> {

}
