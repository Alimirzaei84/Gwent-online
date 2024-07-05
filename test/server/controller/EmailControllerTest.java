package server.controller;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailControllerTest {

    @Test
    public void testSendEmail() {
        String email = "mahdiibohloul@gmail.com";
        EmailController.sendVerificationEmail(email);
    }

    @Test
    public void testVerify() {
        String email = "erfanbohloul@gmail.com";
        EmailController.sendVerificationEmail(email);
        boolean flag = EmailController.verify(email, "999999");
        assertTrue(flag);
    }

}