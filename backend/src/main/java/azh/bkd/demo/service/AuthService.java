package azh.bkd.demo.service;

import azh.bkd.demo.dto.LoginRequest;
import azh.bkd.demo.dto.LoginResponse;
import azh.bkd.demo.dto.RegisterRequest;
import azh.bkd.demo.model.Admin;
import azh.bkd.demo.repository.AdminRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final Pattern BCRYPT_PATTERN =
            Pattern.compile("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
                    Pattern.CASE_INSENSITIVE);
    public AuthService(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // ==========================
    // Register Admin
    // ==========================


@Transactional
public String register(RegisterRequest request) {

    if (request.getEmail() == null
            || !EMAIL_PATTERN.matcher(request.getEmail().trim()).matches()) {
        return "Please enter a valid institutional email address.";
    }

    String normalizedEmail =
            request.getEmail().trim().toLowerCase(java.util.Locale.ROOT);

    String emailDomain =
            normalizedEmail.substring(normalizedEmail.lastIndexOf('@') + 1);

    if (adminRepository.existsByEmail(normalizedEmail)) {
        return "Email already registered!";
    }

    if (adminRepository.existsByEmailDomain(emailDomain)
            || adminRepository.existsByEmailEndingWithIgnoreCase(
                    "@" + emailDomain)) {
        return "This university email domain is already registered!";
    }

    if (!Objects.equals(
            request.getPassword(),
            request.getConfirmPassword())) {
        return "Passwords do not match!";
    }

    if (!request.isAgreeTerms()) {
        return "Please accept Terms & Conditions.";
    }

    Admin admin = new Admin();

    admin.setUniversityName(request.getUniversityName());
    admin.setCampusName(request.getCampusName());
    admin.setInstituteType(request.getInstituteType());

    admin.setAdminName(request.getAdminName());
    admin.setEmail(normalizedEmail);
    admin.setEmailDomain(emailDomain);
    admin.setPhone(request.getPhone());
    admin.setDesignation(request.getDesignation());

    admin.setPassword(passwordEncoder.encode(request.getPassword()));

    admin.setLogo(request.getLogo());
    admin.setAgreeTerms(request.isAgreeTerms());
    admin.setActive(true);

    adminRepository.save(admin);

    return "Workspace created successfully!";
}

    // ==========================
    // Login Admin
    // ==========================

    @Transactional
    public LoginResponse login(LoginRequest request) {

        Optional<Admin> optionalAdmin =
                adminRepository.findByEmail(request.getEmail());

        if (optionalAdmin.isEmpty()) {
            return failedLogin("Invalid Email");
        }

        Admin admin = optionalAdmin.get();

        if (!admin.isActive()) {
            return failedLogin("Account Disabled");
        }

        String storedPassword = admin.getPassword();
        String rawPassword = request.getPassword();

        if (storedPassword == null || rawPassword == null) {
            return failedLogin("Invalid Password");
        }

        boolean passwordMatches;

        if (BCRYPT_PATTERN.matcher(storedPassword).matches()) {

            // Existing BCrypt password.
            passwordMatches =
                    passwordEncoder.matches(rawPassword, storedPassword);

        } else {

            // Legacy plain-text password migration.
            passwordMatches = storedPassword.equals(rawPassword);

            if (passwordMatches) {
                admin.setPassword(passwordEncoder.encode(rawPassword));
                adminRepository.save(admin);
            }
        }

        if (!passwordMatches) {
            return failedLogin("Invalid Password");
        }

        // Generate a real JWT for the authenticated admin.
        String token = jwtService.generateToken(
                admin.getId(),
                admin.getEmail(),
                "ADMIN"
        );

        return new LoginResponse(
                true,
                "Login Successful",
                admin.getId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getDesignation(),
                token
        );
    }

    private LoginResponse failedLogin(String message) {
        return new LoginResponse(
                false,
                message,
                null,
                null,
                null,
                null,
                null
        );
    }
}
