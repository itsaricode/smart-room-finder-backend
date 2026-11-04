package azh.bkd.demo.service;

import azh.bkd.demo.dto.LoginRequest;
import azh.bkd.demo.dto.LoginResponse;
import azh.bkd.demo.dto.RegisterRequest;
import azh.bkd.demo.model.Admin;
import azh.bkd.demo.repository.AdminRepository;


import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AdminRepository adminRepository;

    public AuthService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // ==========================
    // Register Admin
    // ==========================

    public String register(RegisterRequest request) {

        // Email already exists
        if (adminRepository.existsByEmail(request.getEmail())) {
            return "Email already registered!";
        }

        // Password check
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match!";
        }

        // Terms check
        if (!request.isAgreeTerms()) {
            return "Please accept Terms & Conditions.";
        }

        // Create Admin
        Admin admin = new Admin();

        admin.setUniversityName(request.getUniversityName());
        admin.setCampusName(request.getCampusName());
        admin.setInstituteType(request.getInstituteType());

        admin.setAdminName(request.getAdminName());
        admin.setEmail(request.getEmail());
        admin.setPhone(request.getPhone());
        admin.setDesignation(request.getDesignation());

        // NOTE:
        // For now password is saved directly.
        // Later we will use BCrypt encryption.
        admin.setPassword(request.getPassword());

        admin.setLogo(request.getLogo());
        admin.setAgreeTerms(request.isAgreeTerms());
        admin.setActive(true);

        adminRepository.save(admin);

        return "Workspace created successfully!";
    }

   // ==========================
// Login
// ==========================

public LoginResponse login(LoginRequest request) {

    System.out.println("==================================");
    System.out.println("LOGIN REQUEST RECEIVED");
    System.out.println("Email    : " + request.getEmail());
    System.out.println("Password : " + request.getPassword());
    System.out.println("==================================");

    Optional<Admin> optionalAdmin =
            adminRepository.findByEmail(request.getEmail());

    if (optionalAdmin.isEmpty()) {

        System.out.println("Admin NOT FOUND!");

        return new LoginResponse(
                false,
                "Invalid Email",
                null,
                null,
                null,
                null,
                null
        );
    }

    Admin admin = optionalAdmin.get();

    System.out.println("Admin Found : " + admin.getEmail());

    if (!admin.getPassword().equals(request.getPassword())) {

        System.out.println("Password Incorrect!");

        return new LoginResponse(
                false,
                "Invalid Password",
                null,
                null,
                null,
                null,
                null
        );
    }

    if (!admin.isActive()) {

        System.out.println("Account Disabled!");

        return new LoginResponse(
                false,
                "Account Disabled",
                null,
                null,
                null,
                null,
                null
        );
    }

    System.out.println("LOGIN SUCCESSFUL!");

    return new LoginResponse(
            true,
            "Login Successful",
            admin.getId(),
            admin.getAdminName(),
            admin.getEmail(),
            admin.getDesignation(),
            "JWT_COMING_SOON"
    );
}

}