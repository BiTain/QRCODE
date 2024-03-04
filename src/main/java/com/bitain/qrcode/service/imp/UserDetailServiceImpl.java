package com.bitain.qrcode.service.imp;

import com.bitain.qrcode.entity.RoleEntity;
import com.bitain.qrcode.entity.UserEntity;
import com.bitain.qrcode.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(()->
                new UsernameNotFoundException("User detail not found for the user: " + username));
        return new User(userEntity.getEmail(),
                userEntity.getPassWord(),
                userEntity.getEnabled(),
                true,
                true,
                true,
                getGrantedAuthorities(userEntity.getRole()));
    }

    private Set<GrantedAuthority> getGrantedAuthorities(RoleEntity privilege){
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        return authorities;
    }
}
