
package azh.bkd.demo.service;

import azh.bkd.demo.model.Admin;
import azh.bkd.demo.model.AuthorizedUser;
import azh.bkd.demo.repository.AdminRepository;
import azh.bkd.demo.repository.AuthorizedUserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class AuthorizedUserService {

    private final AuthorizedUserRepository authorizedUserRepository;
    private final AdminRepository adminRepository;

    public AuthorizedUserService(
            AuthorizedUserRepository authorizedUserRepository,
            AdminRepository adminRepository) {
        this.authorizedUserRepository = authorizedUserRepository;
        this.adminRepository = adminRepository;
    }

    private Admin getCurrentAdmin() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new IllegalStateException("Admin authentication required.");
        }

        String email = authentication.getName();

        return adminRepository.findByEmail(email)
                .filter(Admin::isActive)
                .orElseThrow(() ->
                        new IllegalStateException("Active admin not found."));
    }

    private String normalizeRole(String role) {

        if (role == null) {
            throw new IllegalArgumentException("Role is required.");
        }

        String normalized = role.trim().toUpperCase(Locale.ROOT);

        if (!List.of("STUDENT", "TEACHER", "CR_GR").contains(normalized)) {
            throw new IllegalArgumentException("Invalid user role.");
        }

        return normalized;
    }

    @Transactional(readOnly = true)
    public List<AuthorizedUser> getAllUsers() {

        Admin admin = getCurrentAdmin();

        return authorizedUserRepository.findAllByAdminId(admin.getId());
    }

    public AuthorizedUser addUser(
            String universityId,
            String fullName,
            String role,
            String department,
            String email) {

        Admin admin = getCurrentAdmin();

        if (universityId == null || universityId.isBlank()) {
            throw new IllegalArgumentException("University ID is required.");
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }

        String normalizedId = universityId.trim().toUpperCase(Locale.ROOT);
        String normalizedRole = normalizeRole(role);

        if (authorizedUserRepository
                .existsByAdminIdAndUniversityIdAndRole(
                        admin.getId(), normalizedId, normalizedRole)) {
            throw new IllegalArgumentException(
                    "This university ID and role already exist.");
        }

        AuthorizedUser user = new AuthorizedUser();

        user.setAdmin(admin);
        user.setUniversityId(normalizedId);
        user.setFullName(fullName.trim());
        user.setRole(normalizedRole);
        user.setDepartment(
                department == null || department.isBlank()
                        ? null : department.trim());
        user.setEmail(
                email == null || email.isBlank()
                        ? null : email.trim().toLowerCase(Locale.ROOT));
        user.setActive(true);

        return authorizedUserRepository.save(user);
    }

    public AuthorizedUser updateStatus(Long id, boolean active) {

        Admin admin = getCurrentAdmin();

        AuthorizedUser user =
                authorizedUserRepository.findByIdAndAdminId(
                        id, admin.getId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Roster user not found."));

        user.setActive(active);

        return authorizedUserRepository.save(user);
    }
}
