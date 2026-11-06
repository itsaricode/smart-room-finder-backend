package azh.bkd.demo.controller;

import azh.bkd.demo.dto.LoginRequest;
import azh.bkd.demo.dto.LoginResponse;
import azh.bkd.demo.dto.RegisterRequest;
import azh.bkd.demo.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import azh.bkd.demo.dto.MobileLoginRequest;
import azh.bkd.demo.dto.MobileLoginResponse;
import azh.bkd.demo.service.MobileAuthService;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;
    private final MobileAuthService mobileAuthService;

   public AuthController(
        AuthService authService,
        MobileAuthService mobileAuthService) {

    this.authService = authService;
    this.mobileAuthService = mobileAuthService;
}

    // ===================================
    // Register Admin
    // ===================================

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        String response = authService.register(request);

        return ResponseEntity.ok(response);
    }

    // ===================================
    // Login Admin
    // ===================================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/mobile-login")
public ResponseEntity<MobileLoginResponse> mobileLogin(
        @RequestBody MobileLoginRequest request) {

    MobileLoginResponse response =
            mobileAuthService.login(request);

    if (!response.isSuccess()) {
        return ResponseEntity.status(401).body(response);
    }

    return ResponseEntity.ok(response);
}

}