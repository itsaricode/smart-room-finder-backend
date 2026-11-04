package azh.bkd.demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // Hardcoded single Admin check (Aap ise change bhi kar sakte hain)
        if ("admin@azhly.com".equals(username) && "admin123".equals(password)) {
            return Map.of("success", true, "message", "Login Successful", "role", "ADMIN");
        } else {
            return Map.of("success", false, "message", "Invalid Credentials");
        }
    }
}