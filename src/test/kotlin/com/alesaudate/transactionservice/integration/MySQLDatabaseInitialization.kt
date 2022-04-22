package com.alesaudate.transactionservice.integration

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.wait.strategy.DockerHealthcheckWaitStrategy

object MySQLDatabaseInitialization : ApplicationContextInitializer<ConfigurableApplicationContext> {

    private var mysql = MySQLContainer("mysql:8.0.28").withExposedPorts(3306)

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        mysql.start()
        mysql.waitingFor(DockerHealthcheckWaitStrategy())

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
            applicationContext,
            "spring.datasource.url=${mysql.jdbcUrl}",
            "spring.datasource.username=${mysql.username}",
            "spring.datasource.password=${mysql.password}"
        )
    }
}
