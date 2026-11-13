
package azh.bkd.demo.repository;

import azh.bkd.demo.model.AuthorizedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorizedUserRepository
        extends JpaRepository<AuthorizedUser, Long> {

    List<AuthorizedUser> findAllByAdminId(Long adminId);

    Optional<AuthorizedUser> findByIdAndAdminId(
            Long id,
            Long adminId
    );

    Optional<AuthorizedUser> findByAdminIdAndUniversityIdAndRole(
            Long adminId,
            String universityId,
            String role
    );


    Optional<AuthorizedUser> findByUniversityIdAndEmailIgnoreCaseAndRole(
            String universityId,
            String email,
            String role
    );

    Optional<AuthorizedUser> findByEmailIgnoreCaseAndRole(
            String email,
            String role
    );

    boolean existsByAdminIdAndUniversityIdAndRole(
            Long adminId,
            String universityId,
            String role
    );
}
