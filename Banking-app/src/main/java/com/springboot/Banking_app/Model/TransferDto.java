package com.springboot.Banking_app.Model;

public record TransferDto(Long fromAccountId,
                          Long toAccountId,
                          double amount) {
}
