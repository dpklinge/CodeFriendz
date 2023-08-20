package com.codefriendz.config

import com.codefriendz.config.oauth.OauthTokenValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig (
    @Value("\${auth0.audience}")
    private val audience: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val issuer: String
){
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        httpSecurity.authorizeExchange()
            .pathMatchers("/webjars/**", "/swagger-ui.html", "/v3/**")
            .permitAll()
            .anyExchange()
            .authenticated()
            .and()
            .csrf { csrf -> csrf.disable() }
            .oauth2ResourceServer()
            .jwt()
            .jwtDecoder(getJwtDecoder())
        return httpSecurity.build()
    }

    fun getJwtDecoder(): ReactiveJwtDecoder {
        val withAudience: OAuth2TokenValidator<Jwt> = OauthTokenValidator(audience)
        val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
        val validator: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withAudience, withIssuer)
        val jwtDecoder: NimbusReactiveJwtDecoder = ReactiveJwtDecoders.fromOidcIssuerLocation(issuer) as NimbusReactiveJwtDecoder
        jwtDecoder.setJwtValidator(validator)
        return jwtDecoder
    }

}
