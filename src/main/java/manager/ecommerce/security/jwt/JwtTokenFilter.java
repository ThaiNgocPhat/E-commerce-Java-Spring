package manager.ecommerce.security.jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manager.ecommerce.security.principle.MyUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter
{
    private final MyUserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String path = request.getServletPath();
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/api-docs") || path.startsWith("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }
        try
        {
            String token = getTokenFromRequest(request);
            if (token != null)
            {
                String username = jwtProvider.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtProvider.validateToken(token, userDetails) )
                {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch (Exception e)
        {
            log.error("Un Authentication {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    public String getTokenFromRequest(HttpServletRequest request)
    {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer "))
        {
            return header.substring(7);
        }
        return null;
    }
}

