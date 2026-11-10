
package azh.bkd.demo.controller;

import azh.bkd.demo.service.RosterCsvService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/authorized-users")
public class RosterCsvController {

    private final RosterCsvService rosterCsvService;

    public RosterCsvController(RosterCsvService rosterCsvService) {
        this.rosterCsvService = rosterCsvService;
    }

    // Import university roster CSV
    @PostMapping(
        value = "/import",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> importCsv(
            @RequestParam("file") MultipartFile file) {

        try {
            return ResponseEntity.ok(
                    rosterCsvService.importCsv(file));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));

        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Unable to read CSV file."));
        }
    }

    // Export current university roster
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsv() throws IOException {

        byte[] csv = rosterCsvService.exportCsv()
                .getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                new MediaType("text", "csv", StandardCharsets.UTF_8));

        headers.setContentDispositionFormData(
                "attachment", "azhly-university-roster.csv");

        headers.setContentLength(csv.length);

        return new ResponseEntity<>(
                csv, headers, HttpStatus.OK);
    }

    // Download sample CSV template
    @GetMapping("/sample")
    public ResponseEntity<byte[]> downloadSample() {

        byte[] csv = rosterCsvService.getSampleCsv()
                .getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                new MediaType("text", "csv", StandardCharsets.UTF_8));

        headers.setContentDispositionFormData(
                "attachment", "azhly-sample-roster.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv);
    }
}
