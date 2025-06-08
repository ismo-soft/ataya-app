package com.ataya.contributor.service.impl;

import com.ataya.contributor.model.Contributor;
import com.ataya.contributor.repo.ContributorRepository;
import com.ataya.contributor.service.ContributorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContributorServiceImpl implements ContributorService {

    private final ContributorRepository contributorRepository;

    @Override
    public Contributor getContributorById(String id) {
        return contributorRepository.findById(id).orElse(null);
    }
}
