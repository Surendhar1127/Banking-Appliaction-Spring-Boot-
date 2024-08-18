package com.springboot.Banking_app.Model;

import java.time.LocalDateTime;

public record TransactionsDto(Long id,
                              Long accountId,
                              double amount,
                              String transactionType,
                              LocalDateTime timeStamp) {
}
