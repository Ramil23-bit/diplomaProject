package org.telran.web.security;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.telran.web.service.SchedulerService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    @Lazy
    UserDetailsService userDetailService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailService) {
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
    }

    private static final Logger logger = Logger.getLogger(SchedulerService.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try{
            String header = request.getHeader("Authorization");

            if(StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String jwt = header.substring("Bearer ".length());
            String email = jwtService.extractUserEmail(jwt);

            if(!email.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(email);
                if(jwtService.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch (AccessDeniedException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied in JWT Filter.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
