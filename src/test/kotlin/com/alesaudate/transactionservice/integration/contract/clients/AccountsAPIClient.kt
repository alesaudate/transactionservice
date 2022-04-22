package com.alesaudate.transactionservice.integration.contract.clients

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.springframework.stereotype.Component

@Component
class AccountsAPIClient {

    companion object {
        const val ACCOUNTS_API_URL = "/api/v1/accounts"
    }

    fun createAccount(initialBalance: Long = 0L): ValidatableResponse =
        given()
            .contentType(ContentType.JSON)
            .body(createAccountRequest(initialBalance))
            .post(ACCOUNTS_API_URL)
            .then()
            .statusCode(200)

    fun retrieveAccount(id: String): ValidatableResponse = given()
        .get("$ACCOUNTS_API_URL/$id")
        .then()

    private fun createAccountRequest(balance: Long) = """
        { 
           "balance": $balance
        }
    """.trimIndent()
}
