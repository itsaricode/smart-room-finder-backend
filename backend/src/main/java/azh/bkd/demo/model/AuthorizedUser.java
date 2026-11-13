
package azh.bkd.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "authorized_users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_admin_university_user_role",
            columnNames = {"admin_id", "university_id", "role"}
        )
    }
)
public class AuthorizedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // University/campus that owns this roster record
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(name = "university_id", nullable = false)
    private String universityId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String role;

    private String department;

    private String email;

    @Column(nullable = false)
    private boolean active = true;

    // Store only a hash of the one-time registration claim code
    private String claimCodeHash;

    @Column(nullable = false)
    private boolean registered = false;

    private LocalDateTime createdAt;

    public AuthorizedUser() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getClaimCodeHash() {
        return claimCodeHash;
    }

    public void setClaimCodeHash(String claimCodeHash) {
        this.claimCodeHash = claimCodeHash;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
