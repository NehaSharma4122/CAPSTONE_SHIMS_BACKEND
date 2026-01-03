package com.usermanager.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
public class JWTUtils {


	private static final String SECRET =
        "ThisIsAStrongJwtSecretKeyWithAtLeast64CharactersLengthForHS512Algorithm";

	private SecretKey getSigningKey() {
	    return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}


    public String extractUserId(String token) {
	    Claims claims = getClaims(token);

	    Object id = claims.get("userId");

	    if (id != null)
	        return String.valueOf(id);

	    return claims.getSubject();
    	
    }

    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    private Claims getClaims(String token) {

        return Jwts.parser()             
                .verifyWith(getSigningKey()) 
                .build()
                .parseSignedClaims(token) 
                .getPayload();            
    }
}


