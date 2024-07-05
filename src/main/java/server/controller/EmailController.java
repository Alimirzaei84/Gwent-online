package server.controller;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class EmailController {

    private static final Map<String, String> verificationCodes = new HashMap<>();

    private static void sendVerificationEmail(String toEmail, String verificationCode) {
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "sip.t.mous@gmail.com";
        String password = "nado mefp djye hgin";

        String subject = "Verification Code";
        String body = "Your verification code is " + verificationCode;

        Properties props = new Properties();
        props.put("mail.debug", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("[SUCC] Sent message successfully");
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            System.out.println("Mail server response: " + e.getNextException().getMessage());
        }
    }

    private static String generateCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(100000);
        return String.format("%06d", code);
    }

    private static void storeVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
    }

    private static String getVerificationCode(String email) {
        return verificationCodes.get(email);
    }

    public static void deleteVerificationCode(String email) {
        verificationCodes.remove(email);
    }

    public static boolean verify(String email, String submittedCode) {
        if (!verificationCodes.containsKey(email)) {
            System.out.println("[ERROR] Invalid email");
            return false;
        }

        String verificationCode = getVerificationCode(email);
        return verificationCode != null && verificationCode.equals(submittedCode);
    }

    public static void sendVerificationEmail(String toEmail) {
        String verificationCode = generateCode();
        sendVerificationEmail(toEmail, verificationCode);

        storeVerificationCode(toEmail, verificationCode);
    }
}
