package com.grupod.docintelia.config;

import com.grupod.docintelia.model.Cuenta;
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
public class JwtService {

    private static final String SECRET_KEY = "17d5be262cbec1f2ad3f59a94d1880a361395b8abdcf0010a991236905255fb3";

    // Método principal para Cuenta: incluye rol como claim para que lo guarde luego en el token
    public String generateToken(Cuenta cuenta) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", cuenta.getRol().getRol());
        return generateToken(extraClaims, cuenta);
    }

    // Sobrecarga para Cuenta (no UserDetails)
    public String generateToken(Map<String, Object> extraClaims, Cuenta cuenta) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(cuenta.getEmail()) // identificador principal
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Método alternativo si todavía usás UserDetails en otros lugares
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //da una token de 24 hs
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //  Obtener email o username del token
    public String getUserName(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Obtener un claim específico
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = getUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }
}



