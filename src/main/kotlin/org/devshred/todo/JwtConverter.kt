package org.devshred.todo

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class JwtConverter : Converter<Jwt, AbstractAuthenticationToken> {

    @Value("\${app.jwt-converter.resource-id}")
    lateinit var resourceId: String

    @Value("\${app.jwt-converter.principal-attribute}")
    lateinit var principalAttribute: String

    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()

    override fun convert(source: Jwt): AbstractAuthenticationToken {
        val grantedAuthorities: MutableCollection<GrantedAuthority> = mutableSetOf()

        grantedAuthorities.addAll(extractResourceRoles(source))
        jwtGrantedAuthoritiesConverter.convert(source)?.let {
            grantedAuthorities.addAll(it)
        }

        return JwtAuthenticationToken(source, grantedAuthorities, source.getClaim(principalAttribute))
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val resourceAccess = jwt.getClaim<Map<String, Any>>("resource_access") ?: return emptySet()
        val resource = resourceAccess[resourceId] as? Map<String, Any> ?: return emptySet()
        val resourceRoles = resource["roles"] as? Collection<String> ?: return emptySet()

        return resourceRoles.stream().map { role: String ->
            SimpleGrantedAuthority(
                "ROLE_$role"
            )
        }.collect(Collectors.toSet())
    }
}
