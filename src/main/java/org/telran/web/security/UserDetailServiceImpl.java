package org.telran.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.telran.web.entity.User;
import org.telran.web.service.UserService;

import java.util.List;
 @Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity = userService.getByEmail(email);
        if(userEntity == null) {
            throw new UsernameNotFoundException("User with login " + email + " not found");
        }
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                List.of(new SimpleGrantedAuthority(userEntity.getRole().name())));
        return user;
    }
}
