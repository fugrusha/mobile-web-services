package com.udemycourse.mobileappws.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UtilsTest {

    @Autowired
    private Utils utils;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testGenerateUserId() {
        String userId1 = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        Assertions.assertNotNull(userId1);
        Assertions.assertEquals(30, userId1.length());

        Assertions.assertFalse(userId1.equalsIgnoreCase(userId2));
    }

    @Test
    public void testIsTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("4564fdsf");
        Assertions.assertNotNull(token);

        boolean tokenExpired = Utils.isTokenExpired(token);
        Assertions.assertFalse(tokenExpired);
    }

    @Test
    public void testIsTokenExpired() {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0NTY0ZmRzZiIsImV4cCI6NjE0ODY4OTQ4MDB9.F0drhfqPFvVFsF8gHvgg3jt9K_fk77pRy543HteSgSLD6TfXRXxJosGjwce2C7RwKJznd2_xwPBY3oW-ODQb8w";

        boolean tokenExpired = Utils.isTokenExpired(expiredToken);

        Assertions.assertTrue(tokenExpired);
    }
}
