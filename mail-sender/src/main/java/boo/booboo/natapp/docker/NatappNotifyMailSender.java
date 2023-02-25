package boo.booboo.natapp.docker;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class NatappNotifyMailSender implements CommandLineRunner {
    static String host = System.getenv("MAIL_HOST");
    static String port = System.getenv("MAIL_PORT");
    static String from = System.getenv("MAIL_FROM");
    static String user = System.getenv("MAIL_USER");
    static String auth = System.getenv("MAIL_AUTH");
    static String to = System.getenv("MAIL_TO");
    static String logfile = System.getenv("LOG_FILE");

    static Authenticator authenticator = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, auth);
        }
    };

    static Properties properties = new Properties();

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(NatappNotifyMailSender.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    public static void send(String subject, String content) throws MessagingException {
        Session session = Session.getDefaultInstance(properties, authenticator);

        System.out.println(subject);
        System.out.println(content);

        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(content, "text/plain;charset=UTF-8");
        Transport.send(message);

        System.out.println("message sent");
    }

    @Override
    public void run(String... args) throws Exception {
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.transport.protocol", "SMTP");
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
        socketFactory.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.socketFactory", socketFactory);

        try {
            Thread.sleep(10_000L);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        int lineCount = 0;
        File file = new File(logfile);
        Pattern pattern = Pattern.compile("(tcp|udp|http)://[\\w.]+?:\\d+");
        while (true) {
            try (Scanner scanner = new Scanner(file)) {
                for (int i = 0; i < lineCount; i++) {
                    if (scanner.hasNext()) {
                        scanner.nextLine();
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        stringBuilder.append("\n").append(matcher.group(0)).append("\n");
                    }
                    lineCount++;
                }
                if (!stringBuilder.isEmpty()) {
                    send("NATAPP NOTIFY", stringBuilder.toString());
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
            try {
                Thread.sleep(10 * 60 * 1000L);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
