package com.alesaudate.transactionservice.domain

import com.alesaudate.transactionservice.interfaces.outcoming.db.Account
import com.alesaudate.transactionservice.interfaces.outcoming.db.TransactionsRepository
import com.alesaudate.transactionservice.utils.randomBalance
import com.alesaudate.transactionservice.utils.randomUUID
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TransactionsService::class])
internal class TransactionsServiceTest {

    @Autowired
    private lateinit var transactionsService: TransactionsService

    @MockkBean(relaxed = true)
    private lateinit var accountService: AccountService

    @MockkBean(relaxed = true)
    private lateinit var transactionsRepository: TransactionsRepository

    @BeforeEach
    fun setup() {
        givenThatTransactionsRepositoryReturnsSavedTransaction()
    }

    @Test
    fun `performMoneyTransferBetweenAccounts - id from first account is not found - AccountNotFoundException is thrown`() {
        val accountOneId = randomUUID()
        every { accountService.loadAccountById(accountOneId) } returns null

        assertThatThrownBy {
            transactionsService.performMoneyTransferBetweenAccounts(from = accountOneId, to = randomUUID(), amount = randomBalance())
        }.isInstanceOf(AccountNotFoundException::class.java)
    }

    @Test
    fun `performMoneyTransferBetweenAccounts - id from second account is not found - AccountNotFoundException is thrown`() {
        val accountTwoId = randomUUID()
        every { accountService.loadAccountById(accountTwoId) } returns null

        assertThatThrownBy {
            transactionsService.performMoneyTransferBetweenAccounts(from = randomUUID(), to = accountTwoId, amount = randomBalance())
        }.isInstanceOf(AccountNotFoundException::class.java)
    }

    @Test
    fun `performMoneyTransferBetweenAccounts - source account balance is below asked amount - InsufficientBalanceException is thrown`() {
        val fromAccount = Account(balance = randomBalance())
        val amount = fromAccount.balance + 1
        val toAccount = Account(balance = 0)

        assertThatThrownBy {
            transactionsService.performMoneyTransferBetweenAccounts(from = fromAccount, to = toAccount, amount = amount)
        }.isInstanceOf(InsufficientBalanceException::class.java)
    }

    @Test
    fun `performMoneyTransferBetweenAccounts - source account balance is equal to asked amount - amount is transferred successfully`() {
        val fromAccount = Account(balance = randomBalance())
        val amount = fromAccount.balance
        val toAccount = Account(balance = 0)

        val transaction = transactionsService.performMoneyTransferBetweenAccounts(from = fromAccount, to = toAccount, amount = amount)

        assertThat(transaction.from.balance).isZero
        assertThat(transaction.to.balance).isEqualTo(amount)
    }

    @Test
    fun `performMoneyTransferBetweenAccounts - source account balance is greater than asked amount - amount is transferred successfully`() {
        val fromAccount = Account(balance = randomBalance())
        val amount = fromAccount.balance - 1
        val toAccount = Account(balance = 0)

        val transaction = transactionsService.performMoneyTransferBetweenAccounts(from = fromAccount, to = toAccount, amount = amount)

        assertThat(transaction.from.balance).isOne
        assertThat(transaction.to.balance).isEqualTo(amount)
    }

    @Test
    fun `performMoneyTransferBetweenAccounts - transfer amount is zero - InvalidAmountException is thrown`() {
        val fromAccount = Account(balance = randomBalance())
        val toAccount = Account(balance = 0)
        val amount = 0L

        assertThatThrownBy {
            transactionsService.performMoneyTransferBetweenAccounts(from = fromAccount, to = toAccount, amount = amount)
        }.isInstanceOf(InvalidAmountException::class.java)
    }

    @Test
    fun `performMoneyTransferBetweenAccounts - transfer amount is below zero - InvalidAmountException is thrown`() {
        val fromAccount = Account(balance = randomBalance())
        val toAccount = Account(balance = 0)
        val amount = -1L

        assertThatThrownBy {
            transactionsService.performMoneyTransferBetweenAccounts(from = fromAccount, to = toAccount, amount = amount)
        }.isInstanceOf(InvalidAmountException::class.java)
    }

    private fun givenThatTransactionsRepositoryReturnsSavedTransaction() {
        every { transactionsRepository.save(any()) } answers { firstArg() }
    }
}
