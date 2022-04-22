package com.alesaudate.transactionservice.integration

import com.alesaudate.transactionservice.domain.AccountService
import com.alesaudate.transactionservice.domain.TransactionsService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [MySQLDatabaseInitialization::class])
@Testcontainers
class TransactionsServiceIT {

    @Autowired
    private lateinit var transactionsService: TransactionsService

    @Autowired
    private lateinit var accountService: AccountService

    @Test
    fun `performMoneyTransferBetweenAccounts - source account has enough balance - transaction is successfully created`() {
        // arrange
        var fromAccount = accountService.createNewAccount(balance = 10L)
        var toAccount = accountService.createNewAccount()
        val amount = 10L

        // act
        val transaction =
            transactionsService.performMoneyTransferBetweenAccounts(from = fromAccount, to = toAccount, amount = amount)

        // assert
        fromAccount = accountService.loadAccountById(fromAccount.id)!!
        toAccount = accountService.loadAccountById(toAccount.id)!!

        assertThat(transaction).isNotNull
        assertThat(transaction.amount).isEqualTo(amount)
        assertThat(transaction.from).isEqualTo(fromAccount)
        assertThat(transaction.to).isEqualTo(toAccount)
        assertThat(fromAccount).isNotNull
        assertThat(fromAccount.balance).isEqualTo(0L)
        assertThat(toAccount).isNotNull
        assertThat(toAccount.balance).isEqualTo(amount)
    }
}
