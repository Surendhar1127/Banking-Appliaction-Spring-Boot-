package com.springboot.Banking_app.Respository;

import com.springboot.Banking_app.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByAccountIdOrderByTimeStampDesc(Long id);
}
