package azh.bkd.demo.service;

import azh.bkd.demo.dto.MobileLoginRequest;
import azh.bkd.demo.dto.MobileLoginResponse;
import azh.bkd.demo.dto.MobileRegisterRequest;
import azh.bkd.demo.model.AuthorizedUser;
import azh.bkd.demo.model.User;
import azh.bkd.demo.repository.AuthorizedUserRepository;
import azh.bkd.demo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class MobileAuthService {

    private final UserRepository userRepository;
    private final AuthorizedUserRepository authorizedUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final Set<String> ALLOWED_ROLES =
            Set.of("STUDENT", "TEACHER", "CR_GR");

    public MobileAuthService(
            UserRepository userRepository,
            AuthorizedUserRepository authorizedUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.authorizedUserRepository = authorizedUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public MobileLoginResponse register(MobileRegisterRequest request) {

        if (request == null ||
            request.getUniversityId() == null ||
            request.getEmail() == null ||
            request.getRole() == null ||
            request.getClaimCode() == null ||
            request.getPassword() == null ||
            request.getConfirmPassword() == null) {

            return failure("Please provide all registration details.");
        }

        String universityId = request.getUniversityId().trim().toUpperCase(Locale.ROOT);
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String role = request.getRole().trim().toUpperCase(Locale.ROOT);
        String claimCode = request.getClaimCode().trim();
        String password = request.getPassword();

        if (universityId.isEmpty() ||
            email.isEmpty() ||
            !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") ||
            claimCode.isEmpty() ||
            !ALLOWED_ROLES.contains(role)) {

            return failure("Invalid registration details.");
        }

        if (password.length() < 8) {
            return failure("Password must contain at least 8 characters.");
        }

        if (!password.equals(request.getConfirmPassword())) {
            return failure("Passwords do not match.");
        }

        Optional<AuthorizedUser> optionalRoster =
                authorizedUserRepository
                        .findByUniversityIdAndEmailIgnoreCaseAndRole(
                                universityId, email, role);

        if (optionalRoster.isEmpty()) {
            return failure("Your details could not be verified. Contact your university administrator.");
        }

        AuthorizedUser roster = optionalRoster.get();

        if (!roster.isActive()) {
            return failure("Your university account is inactive.");
        }

        if (roster.isRegistered()) {
            return failure("This university account is already registered.");
        }

        if (roster.getClaimCodeHash() == null ||
            roster.getClaimCodeHash().isBlank() ||
            !passwordEncoder.matches(claimCode, roster.getClaimCodeHash())) {

            return failure("Invalid or expired claim code. Contact your administrator.");
        }

        if (userRepository.findByUsername(email).isPresent()) {
            return failure("An account with this email already exists.");
        }

        User user = new User();
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        User savedUser = userRepository.save(user);

        // Consume the one-time claim code after successful verification.
        roster.setClaimCodeHash(null);
        roster.setRegistered(true);
        authorizedUserRepository.save(roster);

        String token = jwtService.generateToken(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );

        return new MobileLoginResponse(
                true,
                "Registration successful!",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole(),
                token
        );
    }

    public MobileLoginResponse login(MobileLoginRequest request) {

        if (request == null ||
            request.getUsername() == null ||
            request.getPassword() == null ||
            request.getRole() == null) {

            return failure("Please provide all login details.");
        }

        String email = request.getUsername().trim().toLowerCase(Locale.ROOT);
        String role = request.getRole().trim().toUpperCase(Locale.ROOT);

        if (email.isEmpty() ||
            request.getPassword().isEmpty() ||
            !ALLOWED_ROLES.contains(role)) {

            return failure("Invalid login details.");
        }

        Optional<User> optionalUser =
                userRepository.findByUsernameAndRole(email, role);

        if (optionalUser.isEmpty()) {
            return failure("Invalid email, password or role.");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {

            return failure("Invalid email, password or role.");
        }

        Optional<AuthorizedUser> optionalRoster =
                authorizedUserRepository.findByEmailIgnoreCaseAndRole(email, role);

        if (optionalRoster.isEmpty() ||
            !optionalRoster.get().isActive() ||
            !optionalRoster.get().isRegistered()) {

            return failure("Your university account is inactive or not registered.");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        return new MobileLoginResponse(
                true,
                "Login successful!",
                user.getId(),
                user.getUsername(),
                user.getRole(),
                token
        );
    }

    private MobileLoginResponse failure(String message) {
        return new MobileLoginResponse(
                false,
                message,
                null,
                null,
                null,
                null
        );
    }
}
