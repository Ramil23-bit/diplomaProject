package org.telran.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.telran.web.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${jwttoken.signing.key}")String jwtSecretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if(userDetails instanceof User user) {
            claims.put("id", user);
            claims.put("email", user.getEmail());
            claims.put("role", user.getRole().name());
        }
        return generateToken(userDetails, claims);
    }

    private String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder()
                .claims()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .subject(userDetails.getUsername())
                .add(claims)
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String extractUserName(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        String userName = extractUserName(jwt);
        return userDetails.getUsername().equals(userName) && !isExpired(jwt);
    }

    private boolean isExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
