package com.alesaudate.transactionservice.integration.contract

import com.alesaudate.transactionservice.integration.contract.clients.AccountsAPIClient
import com.alesaudate.transactionservice.integration.contract.clients.TransactionsAPIClient
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class TransactionsAPIContractTest : BaseContractTest() {

    @Autowired
    lateinit var accountsAPIClient: AccountsAPIClient

    @Autowired
    lateinit var transactionsAPIClient: TransactionsAPIClient

    @Test
    fun `given two existing accounts, when I send some amount from one to the other, then both accounts should have their balances updated`() {
        // arrange
        val fromAccountId = createAccount(100L)
        val toAccountId = createAccount(10L)
        val amount = 50L

        // act
        transactionsAPIClient.createTransaction(fromAccountId, toAccountId, amount)

        // assert
        assertAccountBalance(fromAccountId, 50)
        assertAccountBalance(toAccountId, 60)
    }

    @Test
    fun `given two existing accounts, when I try to send zero cents from one to the other, then I should receive HTTP Bad Request return code`() {
        // arrange
        val fromAccountId = createAccount(100L)
        val toAccountId = createAccount(10L)
        val amount = 0L

        // act & assert
        transactionsAPIClient.createTransaction(fromAccountId, toAccountId, amount, expectedStatusCode = HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `given two existing accounts, when I try to send a negative amount from one to the other, then I should receive HTTP Bad Request return code`() {
        // arrange
        val fromAccountId = createAccount(100L)
        val toAccountId = createAccount(10L)
        val amount = -1L

        // act & assert
        transactionsAPIClient.createTransaction(fromAccountId, toAccountId, amount, expectedStatusCode = HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `given two existing accounts, when I try to send an amount higher than the source account has, then I should receive HTTP Bad Request return code`() {
        // arrange
        val fromAccountId = createAccount(100L)
        val toAccountId = createAccount(10L)
        val amount = 101L

        // act & assert
        transactionsAPIClient.createTransaction(fromAccountId, toAccountId, amount, expectedStatusCode = HttpStatus.BAD_REQUEST)
    }

    fun createAccount(initialBalance: Long): String =
        accountsAPIClient.createAccount(initialBalance)
            .extract()
            .body()
            .path("id")

    fun assertAccountBalance(id: String, expectedBalance: Long): ValidatableResponse =
        accountsAPIClient.retrieveAccount(id)
            .body("balance", equalTo(expectedBalance.toInt()))
}
