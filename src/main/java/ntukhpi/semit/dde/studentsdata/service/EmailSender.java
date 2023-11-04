package ntukhpi.semit.dde.studentsdata.service;

import lombok.extern.log4j.Log4j2;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

import static ntukhpi.semit.dde.studentsdata.files.ExcelUtilities.STUDENTSDATA_FILES_FOLDER;

@Log4j2
public class EmailSender {
    // EmailSender's email ID needs to be mentioned
    private static final String from = "isppd.semit@gmail.com";//this my email => recommended to change
    private static final String password = "kvswevtrbydksxrq";//password created after turn on two-factor authorization
//  Created base on
//  https://stackoverflow.com/questions/60654561/java-mail-cannot-connect-to-smtp-using-tls-or-ssl
//  https://javarevisited.blogspot.com/2014/08/how-to-send-email-from-java-program-example.html#
    /**
     * Method for send fixed test to email 'to'
     * and attached file with 'filename'
     * @param to - email where letter will send
     * @param filename - name of file in folder 'results' what must be sent
     */
    //  String to, String filename
    public static void sendEmail(String to, String filename, String path) {


        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";
        //////////////////////
        Properties props = new Properties();
        // Set debug, so we see the whole communication with the server
        props.setProperty("mail.debug", "true");

        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", host);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", "587");

        // Enable STARTTLS
        props.setProperty("mail.smtp.starttls.enable", "true");

        // Accept only TLS 1.1 and 1.2
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.1 TLSv1.2");
        //////////////////////
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        //////////////////////
        try {
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Mail Body");
            Multipart multipart = new MimeMultipart();
            String fileNameToSend = path +filename;
            System.out.println("Email Sender: file for send path - "+fileNameToSend);
            DataSource source = new FileDataSource(fileNameToSend);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(MimeUtility.encodeWord(filename));
            multipart.addBodyPart(messageBodyPart);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Students Data: "+filename);
            message.setText("Завантажені дані про навчальну групу: "+filename);
            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * Method for send fixed test
     * All parameter for sending specified in method variables
     */
    public static void send() {
        // Recipient's email ID needs to be mentioned.
        String to = "ddimae72@gmail.com";//change accordingly
        //////////////////////
        Properties props = new Properties();
        // Set debug, so we see the whole communication with the server
        props.setProperty("mail.debug", "true");

        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", "587");

        // Enable STARTTLS
        props.put("mail.smtp.starttls.enable", "true");

        // Accept only TLS 1.1 and 1.2
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.1 TLSv1.2");
        //////////////////////
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Letters for Spring App!");

            String msg = "This is email sent used Java Mail API";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);

        } catch (Exception e) {
            log.error(e);
        }
    }

    public static String rfc5987_encode(final String s) {
        final byte[] s_bytes = s.getBytes(StandardCharsets.UTF_8);
        final int len = s_bytes.length;
        final StringBuilder sb = new StringBuilder(len << 1);
        final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final byte[] attr_char = {'!', '#', '$', '&', '+', '-', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '~'};
        for (final byte b : s_bytes) {
            if (Arrays.binarySearch(attr_char, b) >= 0)
                sb.append((char) b);
            else {
                sb.append('%');
                sb.append(digits[0x0f & (b >>> 4)]);
                sb.append(digits[b & 0x0f]);
            }
        }

        return sb.toString();
    }
}