package com.grupod.docintelia.controller;

import com.grupod.docintelia.dto.AuthResponseDTO;
import com.grupod.docintelia.dto.AuthenticationRequestDTO;
import com.grupod.docintelia.dto.RegisterDTO;
import com.grupod.docintelia.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody @Valid AuthenticationRequestDTO request){
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }

}
