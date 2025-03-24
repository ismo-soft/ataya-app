package com.ataya.contributor.security.service;

import com.ataya.contributor.repo.ContributorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ContributorRepository contributorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // find the user by username
        return contributorRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User Not Found with -> username or email : " + email)
                );
    }
}
