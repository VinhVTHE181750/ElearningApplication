package team2.elearningapplication.service.implement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    @Test
    void hashPassword() {
        PasswordService passwordService = new PasswordService();
        String password = "password";
        String hashedPassword = passwordService.hashPassword(password);
        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", hashedPassword);
    }

    @Test
    void hashPasswordNullString() {
        PasswordService passwordService = new PasswordService();
        String password = null;
        String hashedPassword = passwordService.hashPassword(password);
        assertThrows(NullPointerException.class, () -> {
            throw new NullPointerException();
        });
    }

    @Test
    void hashPasswordEmpty() {
        PasswordService passwordService = new PasswordService();
        String password = "";
        String hashedPassword = passwordService.hashPassword(password);
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hashedPassword);
    }
}