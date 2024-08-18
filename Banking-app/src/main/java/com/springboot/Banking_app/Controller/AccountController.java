package com.springboot.Banking_app.Controller;


import com.springboot.Banking_app.Model.Account;
import com.springboot.Banking_app.Model.Transaction;
import com.springboot.Banking_app.Model.TransactionsDto;
import com.springboot.Banking_app.Model.TransferDto;
import com.springboot.Banking_app.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<String> addAccount(@RequestBody Account account){

        return new ResponseEntity<>(accountService.addAccount(account).getStatusCode());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id){
        return accountService.getByAccountID(id);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<String> amountDeposit(@PathVariable Long id,
                                                @RequestBody Map<String,Long> request){
        double amount=request.get("amount");
        return accountService.amountDeposit(id,amount);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<String> amountWithdraw(@PathVariable Long id,
                                                @RequestBody Map<String,Long> request){
        double amount=request.get("withdraw");
        return accountService.amountWithdraw(id,amount);
    }

    @GetMapping("/allAccounts")
    public ResponseEntity<List<Account>> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id){
        return accountService.deleteAccount(id);
    }

    @PostMapping("/transfer")
        public ResponseEntity<String> tranferFund(@RequestBody TransferDto transferDto){
accountService.tranferFund(transferDto);
return ResponseEntity.ok("Transfer SuccessFull");
        }

        @GetMapping("/{id}/transaction")
    public ResponseEntity<List<TransactionsDto>> getAccountTransactions(@PathVariable Long id){
        List<TransactionsDto> transactions=accountService.getAccountTransactions(id);
        return ResponseEntity.ok(transactions);
        }

}
