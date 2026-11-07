
package azh.bkd.demo.service;

import azh.bkd.demo.dto.MobileLoginRequest;
import azh.bkd.demo.dto.MobileLoginResponse;
import azh.bkd.demo.model.User;
import azh.bkd.demo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.Optional;

@Service
public class MobileAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final Set<String> ALLOWED_ROLES =
            Set.of("STUDENT", "TEACHER", "CR_GR");

    public MobileAuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
         JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
         this.jwtService = jwtService;
    }

    public MobileLoginResponse login(MobileLoginRequest request) {

        if (request.getUsername() == null ||
            request.getPassword() == null ||
            request.getRole() == null) {

            return failure("Please provide all login details.");
        }

        String username = request.getUsername().trim();
        String role = request.getRole().trim().toUpperCase();

        if (username.isEmpty() ||
            request.getPassword().isEmpty() ||
            !ALLOWED_ROLES.contains(role)) {

            return failure("Invalid login details.");
        }

        Optional<User> optionalUser =
                userRepository.findByUsernameAndRole(username, role);

        if (optionalUser.isEmpty()) {
            return failure("Invalid ID, password or role.");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            return failure("Invalid ID, password or role.");
        }

        return new MobileLoginResponse(
                true,
                "Login successful!",
                user.getId(),
                user.getUsername(),
                user.getRole(),
                null
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
