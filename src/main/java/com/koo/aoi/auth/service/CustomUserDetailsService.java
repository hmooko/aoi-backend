package com.koo.aoi.auth.service;

import com.koo.aoi.auth.AuthenticatedUser;
import com.koo.aoi.user.domain.User;
import com.koo.aoi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + uid));
        return new AuthenticatedUser(user);
    }
}
