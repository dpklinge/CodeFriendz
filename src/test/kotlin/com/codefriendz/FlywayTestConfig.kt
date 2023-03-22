package com.codefriendz

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestComponent
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.annotation.PostConstruct

@TestConfiguration
class FlywayTestConfig {

    @Bean
    fun flywayCleanMigrationStrategy():FlywayMigrationStrategy {
        println("Running flyway migration bean")
        return FlywayMigrationStrategy {
            println("Running flyway migration strategy")
            it.clean()
            it.migrate()
        }
    }

    @PostConstruct
    fun print(){
        println("Config run")
    }
}