import java.io.*;
import java.net.*;
import java.util.*;

public class qhju_build_a_automat {
    public static void main(String[] args) {
        // Configuration settings
        String monitoredAppUrl = "https://example.com"; // URL of the web app to monitor
        int monitoringInterval = 30; // interval in seconds to check the app
        String notificationEmail = "admin@example.com"; // email to send notifications

        // Email sender configuration
        String emailUsername = "your_email_username";
        String emailPassword = "your_email_password";
        String smtpHost = "smtp.gmail.com";
        int smtpPort = 587;

        // Set up the email sender
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", smtpPort);
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        });

        // Start the monitoring loop
        while (true) {
            try {
                // Check if the app is available
                URL url = new URL(monitoredAppUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    // Send notification if app is down
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("no-reply@example.com"));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(notificationEmail));
                    message.setSubject("App Down: " + monitoredAppUrl);
                    message.setText("The app at " + monitoredAppUrl + " is currently down.");
                    Transport.send(message);
                }
            } catch (Exception e) {
                // Log the error
                System.err.println("Error monitoring app: " + e.getMessage());
            }
            // Wait for the next interval
            try {
                Thread.sleep(monitoringInterval * 1000);
            } catch (InterruptedException e) {
                // Handle interruption
            }
        }
    }
}