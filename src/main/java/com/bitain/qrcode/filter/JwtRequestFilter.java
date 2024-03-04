package com.bitain.qrcode.filter;

import com.bitain.qrcode.service.imp.UserDetailServiceImpl;
import com.bitain.qrcode.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@AllArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;
    private UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        if(requestTokenHeader.startsWith("Bearer ")){
            String token = requestTokenHeader.substring(7);
            try{
                //get userName from Token
                String userName = jwtTokenUtil.getUsernameFromToken(token);
                if(StringUtils.isNotEmpty(userName) && null == SecurityContextHolder.getContext().getAuthentication()){
                    UserDetails userDetails = userDetailService.loadUserByUsername(userName);
                    if(jwtTokenUtil.validateToken(token,userDetails)){
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }catch (IllegalArgumentException  e){
                logger.error("Unable to fetch JWT Token");
            }catch (ExpiredJwtException e) {
                logger.error("JWT Token is expired");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }else{
            logger.warn("JWT Token does not begin with Bearer String");
        }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/api/authenticate/");
    }
}
