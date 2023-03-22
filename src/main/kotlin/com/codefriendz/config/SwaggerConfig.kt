package com.codefriendz.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openapi() = OpenAPI().info(Info().title("TbdAppServer").version("0.1-alpha"))
}
