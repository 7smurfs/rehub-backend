package sevensmurfs.rehub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sevensmurfs.rehub.model.entity.UserRole;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtGenerator {

    /**
     * Expiration time in milliseconds
     */
    private static final long JWT_EXPIRATION = 6000000L;

    /**
     * Key used for JWT token encryption
     */
    private static final String KEY =
            "D3A9F50E4CA8B2D5813C5507AF3835E75F2D5E3D69F6C7C4761F830EA6B715F0675EBBD7E0A8C1C64F4EF80421F2577FEAB0623E7C006E0F325E19FCD9DD2F0A";

    private static final String ISSUER = "REHUB";

    public String generateToken(Authentication authentication, List<UserRole> userRoles) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                   .setIssuer(ISSUER)
                   .setSubject(username)
                   .claim("username", username)
                   .claim("roles", userRoles.stream().map(roles -> roles.getName().name()).collect(Collectors.toList()))
                   .setIssuedAt(currentDate)
                   .setExpiration(expireDate)
                   .signWith(getHmacKey())
                   .compact();
    }

    private Key getHmacKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(KEY), SignatureAlgorithm.HS512.getJcaName());
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(getHmacKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(getHmacKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        try {
            return notExpired(claims.getIssuedAt(), claims.getExpiration()) && ISSUER.equals(claims.getIssuer());
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT is expired or invalid.");
        }
    }

    private boolean notExpired(Date issuedAt, Date expireAt) {
        return issuedAt.before(expireAt);
    }
}
