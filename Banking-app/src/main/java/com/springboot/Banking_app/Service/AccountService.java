package com.springboot.Banking_app.Service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.springboot.Banking_app.Model.Account;
import com.springboot.Banking_app.Model.Transaction;
import com.springboot.Banking_app.Model.TransactionsDto;
import com.springboot.Banking_app.Model.TransferDto;
import com.springboot.Banking_app.Respository.AccountRespository;
import com.springboot.Banking_app.Respository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private AccountRespository accountRespository;
    @Autowired
    private TransactionRepository transactionRepository;

    private static final String TRANSACTION_TYPE_DEPOSIT="DEPOSIT";
    private static final String TRANSACTION_TYPE_WITHDRAW="WITHDRAW";
    private static final String TRANSACTION_TYPE_TRANSACTION="TRANSACTION";


    public ResponseEntity<String> addAccount(Account account) {
        accountRespository.save(account);
        return new ResponseEntity<>("Sucess", HttpStatus.CREATED);
    }


    public ResponseEntity<Account> getByAccountID(Long id) {
        try {
            Optional<Account> accountOptional = accountRespository.findById(id);
            if (accountOptional.isPresent()) {
                return new ResponseEntity<>(accountOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> amountDeposit(Long id, double amount) {
        try {
            Account account=accountRespository.findById(id).
                    orElseThrow(()->new RuntimeException("User not Found"));
            double total=account.getBalance()+amount;
            account.setBalance(total);
            accountRespository.save(account);

            Transaction transaction=new Transaction();
            transaction.setAccountId(account.getId());
            transaction.setAmount(amount);
            transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
            transaction.setTimeStamp(LocalDateTime.now());
            transactionRepository.save(transaction);
            return new ResponseEntity<>("Deposited",HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> amountWithdraw(Long id, double amount) {
        try {
            Account account=accountRespository.findById(id).
                    orElseThrow(()->new RuntimeException("User not Found"));
            if(account.getBalance()<amount){
                throw new RuntimeException("Insufficient Amount");
            }
            double total=account.getBalance()-amount;
            account.setBalance(total);
            accountRespository.save(account);
            Transaction transaction=new Transaction();
            transaction.setAccountId(id);
            transaction.setAmount(amount);
            transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
            transaction.setTimeStamp(LocalDateTime.now());
            transactionRepository.save(transaction);

            return new ResponseEntity<>("Withdraw",HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accountList=accountRespository.findAll();
        try{
return new ResponseEntity<>(accountList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }


    public String deleteAccount(Long id) {
        accountRespository.deleteById(id);
        return "success";

    }

    public void tranferFund(TransferDto transferDto) {
        Account fromAccount=accountRespository.findById(transferDto.fromAccountId()).
                orElseThrow(()->new RuntimeException("Account not Found"));

        Account toAccount=accountRespository.findById(transferDto.toAccountId()).
                orElseThrow(()->new RuntimeException("Account not Found"));

        fromAccount.setBalance(fromAccount.getBalance()-transferDto.amount());

        toAccount.setBalance(toAccount.getBalance()+transferDto.amount());

        if(fromAccount.getBalance()<transferDto.amount()){
            throw new RuntimeException("Insuffient Balance");
        }

        accountRespository.save(fromAccount);
        accountRespository.save(toAccount);

Transaction transaction=new Transaction();

transaction.setAccountId(fromAccount.getId());
transaction.setAmount(transferDto.amount());
transaction.setTransactionType(TRANSACTION_TYPE_TRANSACTION);
transaction.setTimeStamp(LocalDateTime.now());
transactionRepository.save(transaction);

    }

    public List<TransactionsDto> getAccountTransactions(Long id) {
        List<Transaction> transaction=transactionRepository.findByAccountIdOrderByTimeStampDesc(id);
        return transaction.stream().
                map((transactions)->convertEntitytoDto(transactions)).collect(Collectors.toList());
    }

    private TransactionsDto convertEntitytoDto(Transaction transaction){
        return  new TransactionsDto(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimeStamp()
        );
    }
}


