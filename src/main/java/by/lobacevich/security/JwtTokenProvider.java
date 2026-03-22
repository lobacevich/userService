package by.lobacevich.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public static final String SECRET = "VKs3X6p9e8R2tY5w7z9C1f4H6J8kL0nP2qR4sT6uV8wX0yZ2b4c6d8e0f2g4h6j8k0";

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
