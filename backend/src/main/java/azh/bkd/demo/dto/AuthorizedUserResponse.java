
package azh.bkd.demo.dto;

import azh.bkd.demo.model.AuthorizedUser;
import java.time.LocalDateTime;

public class AuthorizedUserResponse {

    private Long id;
    private String universityId;
    private String fullName;
    private String role;
    private String department;
    private String email;
    private boolean active;
    private LocalDateTime createdAt;

    public AuthorizedUserResponse(AuthorizedUser user) {
        this.id = user.getId();
        this.universityId = user.getUniversityId();
        this.fullName = user.getFullName();
        this.role = user.getRole();
        this.department = user.getDepartment();
        this.email = user.getEmail();
        this.active = user.isActive();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getUniversityId() { return universityId; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
