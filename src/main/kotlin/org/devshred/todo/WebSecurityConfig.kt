package org.devshred.todo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.OPTIONS
import org.springframework.http.HttpMethod.PATCH
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

private const val API_PATH_WITH_ID = "/v1/todo/**"
private const val API_PATH_WITHOUT_ID = "/v1/todo"
private const val ROLE_USER = "user"

@Configuration
@EnableWebSecurity
@Profile("!test")
class WebSecurityConfig(private val jwtConverter: JwtConverter) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }

            authorizeHttpRequests {
                authorize(GET, API_PATH_WITH_ID, hasRole(ROLE_USER))
                authorize(PATCH, API_PATH_WITH_ID, hasRole(ROLE_USER))
                authorize(DELETE, API_PATH_WITHOUT_ID, hasRole(ROLE_USER))
                authorize(DELETE, API_PATH_WITH_ID, hasRole(ROLE_USER))
                authorize(POST, API_PATH_WITHOUT_ID, hasRole(ROLE_USER))
                authorize(OPTIONS, "/**", permitAll)
            }

            oauth2ResourceServer {
                jwt { jwtAuthenticationConverter = jwtConverter }
            }

            sessionManagement { SessionCreationPolicy.STATELESS }
        }

        return http.build()
    }
}
