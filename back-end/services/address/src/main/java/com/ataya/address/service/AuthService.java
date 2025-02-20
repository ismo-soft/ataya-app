package com.ataya.address.service;

import com.ataya.address.dto.auth.AuthenticationRequest;
import com.ataya.address.dto.auth.AuthenticationResponse;
import jakarta.validation.Valid;

public interface AuthService {
    AuthenticationResponse login(@Valid AuthenticationRequest request);
}
