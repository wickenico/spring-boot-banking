package com.nw.sevbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nw.sevbanking.database.CreditcardDTO;

/**
 * 
 * @author nicowickersheim
 * @date 29.01.2022
 *
 */

@Repository
public interface ICreditcardRepository extends JpaRepository<CreditcardDTO, Long>  {

}
