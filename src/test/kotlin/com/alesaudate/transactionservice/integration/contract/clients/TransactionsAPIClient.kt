package com.alesaudate.transactionservice.integration.contract.clients

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class TransactionsAPIClient {

    companion object {
        const val TRANSACTIONS_API_URL = "/api/v1/transactions"
    }

    fun createTransaction(fromAccount: String, toAccount: String, balance: Long, expectedStatusCode: HttpStatus = HttpStatus.OK): ValidatableResponse =
        given()
            .contentType(ContentType.JSON)
            .body(createTransactionRequest(fromAccount, toAccount, balance))
            .post(TRANSACTIONS_API_URL)
            .then()
            .statusCode(expectedStatusCode.value())

    private fun createTransactionRequest(fromAccount: String, toAccount: String, balance: Long) = """
        {
            "from":"$fromAccount",
            "to":"$toAccount",
            "amount":$balance
        }
    """.trimIndent()
}
