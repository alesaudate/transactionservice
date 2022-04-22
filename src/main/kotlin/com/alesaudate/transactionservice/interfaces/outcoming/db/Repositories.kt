package com.alesaudate.transactionservice.interfaces.outcoming.db

import org.springframework.data.jpa.repository.JpaRepository

interface AccountsRepository : JpaRepository<Account, String>

interface TransactionsRepository : JpaRepository<Transaction, String>
