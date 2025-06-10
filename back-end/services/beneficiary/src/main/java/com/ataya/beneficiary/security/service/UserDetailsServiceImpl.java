package com.ataya.beneficiary.security.service;

import com.ataya.beneficiary.repo.BeneficiaryRepository;
import com.ataya.contributor.repo.ContributorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final BeneficiaryRepository beneficiaryRepository;

    @Override
    public UserDetails loadUserByUsername(String identityNumber) throws UsernameNotFoundException {
        // find the user by username
        return beneficiaryRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User Not Found with")
                );
    }
}
