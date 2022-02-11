package Email;

import Connection.DBControl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static java.util.concurrent.TimeUnit.SECONDS;
/**
 * This class allow system to handle notification user through email
 * @author  Chau Truong Vinh Hoang
 */
public class EmailControl {
    private static Properties properties;
    private static String username= "ehealthvgu.noreply@gmail.com";
    private static String pwd="vgu123456";
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    /**
     * Constructor to set up the default system mail
     * @throws java.security.NoSuchAlgorithmException, MessagingException catch exception if any
     */
    public EmailControl() throws NoSuchProviderException, MessagingException {
        this.properties = new Properties();
        properties.put("mail.pop3.host", "pop.gmail.com");
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");

        //smtp for sending mail
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.host","smtp.gmail.com");

    }

    /**
     * Constructor set up the sender mail
     * @param username specify mail address
     * @param pwd specify mail corresponding password.
     * @throws java.security.NoSuchAlgorithmException, MessagingException catch exception if any
     */
    public EmailControl(String username, String pwd) throws NoSuchProviderException, MessagingException {
        this.username = username;
        this.pwd=pwd;
        //this.host=host;
        this.properties = new Properties();
        //pop3 for checking mail
        properties.put("mail.pop3.host", "pop.gmail.com");
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");

        //smtp for sending mail
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.host","smtp.gmail.com");

    }
    private static Session getSession() throws NoSuchProviderException, MessagingException{
        Session emailSession = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                username, pwd);
                    }
                });
        return emailSession;
    }
    /**
     * Method to allow check mail through terminal
     */
    public static void checkMail()
    {
        try {
            Session emailSession = getSession();
            System.out.println("You start checkMail method!");
            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect("pop.gmail.com", username, pwd);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Method to set up an email for sending
     * @param recipientAddress specify recipient mail address
     * @param mailSubject specify mail subject
     * @param mailContent specify mail content
     */
    public static void sendMail(String recipientAddress, String mailSubject, String mailContent){
        try {
            Session emailSession = getSession();
            // Create a default MimeMessage object
            Message message = new MimeMessage(emailSession);

            //Set From: header field of the header
            message.setFrom();

            //Set To: header field of the header
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress));

            //Set subject: header field
            message.setSubject(mailSubject);
            // Now set the actual message
            message.setText(mailContent);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException ex) {
            //Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void sendScheduledMail(int time, String recipientAddress, String mailSubject, String content){
        final Runnable beeper = new Runnable() {
            public void run() {
                sendMail(recipientAddress, mailSubject, content); }
        }; // Creating a new runnable task which will be passed as an argument to scheduler
        scheduler.schedule(beeper, time, SECONDS); // Creates and executes a one-shot action that becomes enabled after the given delay.
        scheduler.shutdown();
        System.out.println("Send scheduled message after "+time+ " seconds waiting...");
    }
    /**
     * Method to send an email with attached file (including pdf file)
     * @param filename specify the file path
     * @param recipientAddress specify recipient mail address
     * @param mailSubject specify mail subject
     * @param mailContent specify mail content
     */
    public static void sendMailAttachedPDF(String filename, String recipientAddress, String mailSubject, String mailContent){
        try {
            Session emailSession = getSession();
            Message message = new MimeMessage(emailSession);

            message.setFrom();

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress));

            message.setSubject(mailSubject);
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(mailContent);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(new File(filename).getName());
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully!!!");
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }

    }
    /**
     * Method to notify user through email when they create an appointment successfully
     * @param username specify the username
     * @throws SQLException, MessagingException catch exception if any
     */
    public static void sendAppointmentCreateSuccessfully(String username) throws SQLException, MessagingException {
        String recipientAddress = getUserEmail(username);
        String destinationFilePath = System.getProperty("user.dir")+"/src/main/java/Appointment/PatientAppointment.pdf";
        new EmailControl().sendMailAttachedPDF(destinationFilePath,recipientAddress,
                "[Ehealth Service] You have created an appointment with our doctor",
                "Dear value customer,\nThank you for choosing our service.\n Please check your appointment" +
                        " detail below for more information\nRegards,\nEhealth Service Team");
    }
    /**
     * Method to notify user through email when they update their appointment successfully
     * @param username specify the username
     * @throws SQLException, MessagingException catch exception if any
     */
    public static void sendAppointmentUpdatedSuccessfully(String username) throws SQLException, MessagingException {
        String recipientAddress = getUserEmail(username);
        String destinationFilePath = System.getProperty("user.dir")+"/src/main/java/Appointment/PatientAppointment.pdf";
        new EmailControl().sendMailAttachedPDF(destinationFilePath,recipientAddress,
                "[Ehealth Service] Updated Appointment",
                "Dear value customer,\nYour appointment has been updated as your request.\n Please check your appointment" +
                        " detail below for more information\nRegards,\nEhealth Service Team");
    }
    /**
     * Method to notify user through email about their upcomming appointment
     * @param username specify the username
     * @param reminder_time specify the reminder time
     * @throws SQLException, MessagingException catch exception if any
     */
    public static void sendMailReminder(String username, int reminder_time) throws SQLException, MessagingException {
        String recipientAddress = getUserEmail(username);
        new EmailControl().sendScheduledMail(reminder_time,recipientAddress,
                "Appointment Reminder","Dear value customer,\n" +
                        "Please note that you have the upcoming appointment with our doctor after "+reminder_time+"s\n"+
                        "Please check your appointment detail we sent you\nRegards\nEhealth Service Team");
    }
    private static String getUserEmail(String username) throws SQLException {
        String sql ="select email from user where username='"+username+"'";
        Statement stmt = DBControl.dbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String email="";
        while(rs.next()){
            email = rs.getString(1);
        }
        System.out.println("User mail:"+email);
        return email;
    }


}
