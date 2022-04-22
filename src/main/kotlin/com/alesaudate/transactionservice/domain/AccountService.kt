package com.alesaudate.transactionservice.domain

import com.alesaudate.transactionservice.interfaces.outcoming.db.Account
import com.alesaudate.transactionservice.interfaces.outcoming.db.AccountsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountsRepository: AccountsRepository
) {

    fun loadAccountById(id: String) = accountsRepository.findByIdOrNull(id)

    fun createNewAccount(balance: Long = 0) = accountsRepository.save(Account(balance = balance)).also {
        LOGGER.info("Account created: {}", it.id)
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    fun saveAccounts(firstAccount: Account, secondAccount: Account): List<Account> =
        accountsRepository.saveAll(listOf(firstAccount, secondAccount))

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AccountService::class.java)
    }
}

fun Account.addBalance(amount: Long) = copy(balance = balance + amount)

fun Account.reduceBalance(amount: Long) = copy(balance = balance - amount)
