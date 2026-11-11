
package azh.bkd.demo.service;

import azh.bkd.demo.model.AuthorizedUser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RosterCsvService {

    private final AuthorizedUserService authorizedUserService;

    private static final Set<String> REQUIRED_HEADERS =
            Set.of("universityId", "fullName", "role");

    public RosterCsvService(
            AuthorizedUserService authorizedUserService) {
        this.authorizedUserService = authorizedUserService;
    }

    @Transactional
    public Map<String, Object> importCsv(MultipartFile file)
            throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty.");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "CSV file must not exceed 5 MB.");
        }

        List<String> errors = new ArrayList<>();
        int imported = 0;
        int skipped = 0;

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .get();

        try (InputStreamReader reader = new InputStreamReader(
                    file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = format.parse(reader)) {

            Set<String> headers = parser.getHeaderMap().keySet();

            if (!headers.containsAll(REQUIRED_HEADERS)) {
                throw new IllegalArgumentException(
                        "Required CSV headers: universityId, fullName, role");
            }

            for (CSVRecord record : parser) {

                try {
                    String universityId =
                            record.get("universityId").trim();

                    String fullName =
                            record.get("fullName").trim();

                    String role =
                            record.get("role").trim();

                    String department = headers.contains("department")
                            ? record.get("department").trim() : null;

                    String email = headers.contains("email")
                            ? record.get("email").trim() : null;

                    authorizedUserService.addUser(
                            universityId,
                            fullName,
                            role,
                            department,
                            email
                    );

                    imported++;

                } catch (Exception e) {
                    skipped++;

                    errors.add(
                            "Row " + (record.getRecordNumber() + 1)
                            + ": " + e.getMessage()
                    );
                }
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "CSV import completed.");
        result.put("imported", imported);
        result.put("skipped", skipped);
        result.put("errors", errors);

        return result;
    }

    @Transactional(readOnly = true)
    public String exportCsv() throws IOException {

        List<AuthorizedUser> users =
                authorizedUserService.getAllUsers();

        StringWriter writer = new StringWriter();

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(
                        "universityId",
                        "fullName",
                        "role",
                        "department",
                        "email",
                        "active"
                )
                .get();

        try (CSVPrinter printer = new CSVPrinter(writer, format)) {

            for (AuthorizedUser user : users) {

                printer.printRecord(
                        user.getUniversityId(),
                        user.getFullName(),
                        user.getRole(),
                        user.getDepartment(),
                        user.getEmail(),
                        user.isActive()
                );
            }
        }

        return writer.toString();
    }

    public String getSampleCsv() {

    String domain =
            authorizedUserService.getCurrentAdminEmailDomain();

    return "universityId,fullName,role,department,email\n"
            + "CS-001,Ali Ahmed,STUDENT,Computer Science,ali@" + domain + "\n"
            + "EMP-001,Sara Khan,TEACHER,Computer Science,sara@" + domain + "\n"
            + "CS-CR-001,Usman Ali,CR_GR,Computer Science,usman@" + domain + "\n";
}
}
