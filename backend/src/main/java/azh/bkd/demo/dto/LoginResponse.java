package azh.bkd.demo.dto;

public class LoginResponse {

    private boolean success;
    private String message;

    private Long adminId;
    private String adminName;
    private String email;
    private String designation;

    // Future JWT Support
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(boolean success,
                         String message,
                         Long adminId,
                         String adminName,
                         String email,
                         String designation,
                         String token) {

        this.success = success;
        this.message = message;
        this.adminId = adminId;
        this.adminName = adminName;
        this.email = email;
        this.designation = designation;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}