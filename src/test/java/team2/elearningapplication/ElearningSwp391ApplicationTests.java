package team2.elearningapplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ElearningSwp391ApplicationTests {

    @Autowired
    private LoginService loginService; // Assuming you have a LoginService

    @Test
    void contextLoads() {
    }

    @Test
    void testLoginService() {
        // Given
        String username = "testuser";
        String password = "testpassword";

        // When
        boolean loginResult = loginService.login(username, password);

        // Then
        assertTrue(loginResult, "Login should be successful for valid credentials");
    }

}
