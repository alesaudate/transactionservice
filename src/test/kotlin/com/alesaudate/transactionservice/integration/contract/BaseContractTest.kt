package com.alesaudate.transactionservice.integration.contract

import com.alesaudate.transactionservice.integration.MySQLDatabaseInitialization
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [MySQLDatabaseInitialization::class])
@Testcontainers
abstract class BaseContractTest {

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setupRestAssured() {
        RestAssured.baseURI = "http://localhost:$port"
    }
}
