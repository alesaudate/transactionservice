package com.alesaudate.transactionservice.domain

import com.alesaudate.transactionservice.interfaces.outcoming.db.Account
import com.alesaudate.transactionservice.interfaces.outcoming.db.Transaction
import com.alesaudate.transactionservice.interfaces.outcoming.db.TransactionsRepository
import com.alesaudate.transactionservice.utils.throwIfNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionsService(
    private val accountService: AccountService,
    private val transactionsRepository: TransactionsRepository
) {

    @Transactional
    fun performMoneyTransferBetweenAccounts(from: Account, to: Account, amount: Long): Transaction {
        LOGGER.info("Trying to transfer funds from account {} to account {}", from.id, to.id)
        if (amount <= 0) throw InvalidAmountException()
        if (from.balance < amount) throw InsufficientBalanceException()

        val sourceAccount = from.reduceBalance(amount)
        val destinationAccount = to.addBalance(amount)
        accountService.saveAccounts(sourceAccount, destinationAccount)
        val registerTransaction = Transaction(amount = amount, from = sourceAccount, to = destinationAccount)
        LOGGER.info("Registering transference from {} to {}", from.id, to.id)
        return transactionsRepository.save(registerTransaction)
    }

    @Transactional
    fun performMoneyTransferBetweenAccounts(from: String, to: String, amount: Long): Transaction {
        val fromAccount = accountService.loadAccountById(from).throwIfNull { throw AccountNotFoundException(from) }
        val toAccount = accountService.loadAccountById(to).throwIfNull { throw AccountNotFoundException(to) }
        return performMoneyTransferBetweenAccounts(from = fromAccount, to = toAccount, amount = amount)
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(TransactionsService::class.java)
    }
}

class InsufficientBalanceException : RuntimeException("")

class InvalidAmountException : RuntimeException("It's not possible to transfer the required amount")

class AccountNotFoundException(accountId: String) :
    RuntimeException("Account $accountId could not be found")
