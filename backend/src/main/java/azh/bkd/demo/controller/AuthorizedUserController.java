
package azh.bkd.demo.controller;

import azh.bkd.demo.model.AuthorizedUser;
import azh.bkd.demo.service.AuthorizedUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/authorized-users")
public class AuthorizedUserController {

    private final AuthorizedUserService authorizedUserService;

    public AuthorizedUserController(
            AuthorizedUserService authorizedUserService) {
        this.authorizedUserService = authorizedUserService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorizedUser>> getAllUsers() {

        return ResponseEntity.ok(
                authorizedUserService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<?> addUser(
            @RequestBody Map<String, String> request) {

        try {
            AuthorizedUser user = authorizedUserService.addUser(
                    request.get("universityId"),
                    request.get("fullName"),
                    request.get("role"),
                    request.get("department"),
                    request.get("email")
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(user);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request) {

        try {
            Boolean active = request.get("active");

            if (active == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Active status is required."));
            }

            return ResponseEntity.ok(
                    authorizedUserService.updateStatus(id, active));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
