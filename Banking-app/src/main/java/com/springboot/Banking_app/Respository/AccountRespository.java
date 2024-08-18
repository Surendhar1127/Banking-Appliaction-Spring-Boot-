package com.springboot.Banking_app.Respository;

import com.springboot.Banking_app.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRespository extends JpaRepository<Account,Long> {
    public Optional<Account> findById(Long id);
}
