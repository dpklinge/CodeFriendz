package com.codefriendz

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class FlywayTestConfig {

    @Bean
    fun flywayCleanMigrationStrategy(): FlywayMigrationStrategy {
        return FlywayMigrationStrategy {
            it.clean()
            it.migrate()
        }
    }
}
