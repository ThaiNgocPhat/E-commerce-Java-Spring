package manager.ecommerce.security.jwt;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import manager.ecommerce.entity.Role;
import manager.ecommerce.security.principle.MyUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider
{
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    @Value("${jwt.expired.access}")
    private Long EXPIRED_ACCESS;

    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String username, Set<Role> roles){
        List<String> roleNames = roles.stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(EXPIRED_ACCESS, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(MyUserDetails detailCustom){
        return Jwts.builder().setSubject(detailCustom.getUsername()).claim("role",detailCustom.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(EXPIRED_ACCESS * 10, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateOTP() {
        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000);
        return String.valueOf(otp);
    }
}
