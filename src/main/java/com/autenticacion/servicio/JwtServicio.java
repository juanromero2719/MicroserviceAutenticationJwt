package com.autenticacion.servicio;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServicio {

    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

    public String getToken(UserDetails usuario){

        return getToken(new HashMap<>(), usuario);
    }

    private String getToken(Map<String,Object> extraClaims, UserDetails usuario){

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(){

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String obtenerUsuarioDelToken(String token){

        return getClaim(token, Claims::getSubject);
    }

    public boolean esTokenValido(String token, UserDetails usuario_detalles){

        final String username = obtenerUsuarioDelToken(token);
        return (username.equals(usuario_detalles.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token){

        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){

        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDate(String token){

        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){

        return getExpirationDate(token).before(new Date());
    }
}