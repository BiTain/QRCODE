package com.bitain.qrcode.config;

import com.bitain.qrcode.service.imp.UserDetailServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserNamePwdAuthenticationProvider implements AuthenticationProvider {
    private UserDetailServiceImpl userDetailService;
    private PasswordEncoder passwordEncoder;
    public UserNamePwdAuthenticationProvider(UserDetailServiceImpl userDetailService,PasswordEncoder passwordEncoder){
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName().toString();
        String pwd = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailService.loadUserByUsername(userName);
        if(userDetails!=null){
            if(passwordEncoder.matches(pwd,userDetails.getPassword())){
                Object principalToReturn = userDetails;
                UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.authenticated(principalToReturn,
                        authentication.getCredentials(),userDetails.getAuthorities());
                return result;
            }else {
                throw new BadCredentialsException("Invalid password");
            }
        }else {
            throw new BadCredentialsException("No user registered with this details");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
