
package azh.bkd.demo.config;

import azh.bkd.demo.model.User;
import azh.bkd.demo.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnProperty(
    name = "azhly.seed-demo-users",
    havingValue = "true"
)
public class DevUserSeeder {

    @Bean
    CommandLineRunner seedDemoUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            createUser(
                userRepository,
                passwordEncoder,
                "CS-TEST-001",
                "STUDENT"
            );

            createUser(
                userRepository,
                passwordEncoder,
                "EMP-TEST-001",
                "TEACHER"
            );

            createUser(
                userRepository,
                passwordEncoder,
                "CS-CR-001",
                "CR_GR"
            );

            System.out.println("AZHly demo users seeded!");
        };
    }

    private void createUser(
            UserRepository repository,
            PasswordEncoder encoder,
            String username,
            String role) {

        if (repository.findByUsernameAndRole(
                username, role).isPresent()) {
            return;
        }

        User user = new User();

        user.setUsername(username);
        user.setRole(role);
        user.setPassword(encoder.encode("Demo@12345"));

        repository.save(user);
    }
}
