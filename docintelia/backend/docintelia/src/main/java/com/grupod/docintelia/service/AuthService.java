package com.grupod.docintelia.service;

import com.grupod.docintelia.dto.AuthResponseDTO;
import com.grupod.docintelia.dto.AuthenticationRequestDTO;
import com.grupod.docintelia.dto.RegisterDTO;

public interface AuthService {

    AuthResponseDTO register (RegisterDTO request);
    AuthResponseDTO authenticate(AuthenticationRequestDTO request);

}