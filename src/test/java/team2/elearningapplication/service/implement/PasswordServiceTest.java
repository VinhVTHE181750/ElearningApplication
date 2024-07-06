package team2.elearningapplication.service.implement;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {
    private static List<String> passwords;
    private static List<String> hashes;

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        passwordService = new PasswordService();
        MockitoAnnotations.openMocks(this);
    }

    @BeforeAll
    static void setData() {
        Yaml yaml = new Yaml();
        try (InputStream in = PasswordServiceTest.class.getClassLoader().getResourceAsStream("test-cases.yml")) {
            Map<String, Object> yamlData = yaml.load(in);

            // Extract passwords
            Map<String, List<String>> createUserSection = (Map<String, List<String>>) yamlData.get("createUser");
            passwords = createUserSection.get("password");

            // Extract hashes - assuming the structure is similar and will be filled later
            Map<String, List<String>> hashPasswordSection = (Map<String, List<String>>) yamlData.get("hashPassword");
            hashes = hashPasswordSection != null ? hashPasswordSection.get("hashes") : new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<String> passwords() {
        return passwords;
    }

    @ParameterizedTest
    @MethodSource("passwords")
    void testHashPassword(String password) {

        int index = passwords.indexOf(password);
        if (password == null) {
            assertNull(passwordService.hashPassword(password));
        } else if (hashes.get(index).startsWith("EXCEPTION")) {
            assertThrows(RuntimeException.class, () -> passwordService.hashPassword(password));
        } else {
            assertEquals(hashes.get(index), passwordService.hashPassword(password));
        }
    }
}
