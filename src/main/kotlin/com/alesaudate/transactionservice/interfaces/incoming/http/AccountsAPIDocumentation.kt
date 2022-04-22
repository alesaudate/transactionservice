package com.alesaudate.transactionservice.interfaces.incoming.http

import com.alesaudate.transactionservice.interfaces.outcoming.db.Account
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PathVariable

interface AccountsAPIDocumentation {

    @Operation(
        summary = "Finds an account by its ID",
        tags = ["accounts"],
        parameters = [Parameter(name = "id", description = "The account ID")],
        responses = [
            ApiResponse(
                description = "Account successfully found",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = JSON,
                        examples = [
                            ExampleObject(
                                """
                            {
                               "id":"babfea41-372b-4ed8-b8e8-e7c56dcadf57",
                               "balance": 1231
                            }
                        """
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                description = "Account not found",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = JSON,
                        examples = [
                            ExampleObject(
                                """
                            {
                                "timestamp": "2022-02-24T18:17:36.137+00:00",
                                "status": 404,
                                "error": "Not Found",
                                "path": "/api/v1/accounts/4234"
                            }
                        """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun findAccountById(@PathVariable("id") id: String): Account

    @Operation(
        summary = "Creates an account with a specified balance",
        tags = ["accounts"],
        responses = [
            ApiResponse(
                description = "Account successfully created",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = JSON,
                        examples = [
                            ExampleObject(
                                """
                            {
                               "id":"babfea41-372b-4ed8-b8e8-e7c56dcadf57",
                               "balance": 1231
                            }
                        """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun createAccount(
        @RequestBody(
            description = "Creates a new account with a predefined balance",
            content = [Content(schema = Schema(implementation = CreateAccountWithBalanceRequest::class))]
        )
        request: CreateAccountWithBalanceRequest
    ): Account
}
