package com.example.productapi.security;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

/**
 * Converts roles from a JWT claim into Spring Security authorities.
 */
public class JwtAuthConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        List<String> roles = jwt.getClaimAsStringList("roles");

        List<SimpleGrantedAuthority> authorities =
                roles == null
                        ? List.of()
                        : roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        return new JwtAuthenticationToken(jwt, authorities);
    }
}