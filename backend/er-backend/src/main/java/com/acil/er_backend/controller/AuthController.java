package com.acil.er_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Oturum açılmamış"));
        }

        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");

        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("username", auth.getName());
        response.put("role", role);
        response.put("authenticated", true);

        return ResponseEntity.ok(response);
    }
}
