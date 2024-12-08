package com.project.placement_management_app.service.email;

import com.project.placement_management_app.model.EmailTemplate;
import com.project.placement_management_app.repository.EmailTemplateRepository;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Value("${spring.mail.username}")
    private String emailId;
    public void sendEmail(String[] toEmail, String subject, String emailContext, boolean isContentHTML, String[] ccEmail) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true, "UTF-8");

        mimeMessageHelper.setFrom(emailId);
        mimeMessageHelper.setTo(toEmail);
        if(ccEmail.length > 0) {
            mimeMessageHelper.setCc(ccEmail);
        }
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(emailContext, isContentHTML);

        mailSender.send(message);
    }

    public EmailTemplate getEmailTemplate(String emailTemplateId){
        return emailTemplateRepository.findEmailTemplateById(emailTemplateId);
    }

    public String createEmailContext(String emailContent, Map<String, String> emailParameters){
        for(Map.Entry<String, String> emailParameter : emailParameters.entrySet()){
            emailContent = emailContent.replace("${" + emailParameter.getKey() + "}", emailParameter.getValue());
        }
        return emailContent;
    }

    public String createSubject(String subject, Map<String, String> subjectParameters){
        for(Map.Entry<String, String> subjectParameter : subjectParameters.entrySet()){
            subject = subject.replace("${" + subjectParameter.getKey() + "}", subjectParameter.getValue());
        }
        return subject;
    }

    public void sendCalendarInvite(String uid,
                                   String[] toEmails, String subject, String emailContext,
                                   boolean isContentHTML, Date slotStartTime, Date slotEndTime) throws Exception{

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(emailId);
        message.setSubject(subject);
        for (String email : toEmails) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        }
        Multipart multipart = new MimeMultipart("alternative");
        BodyPart messageBodyPart = buildCalendarPart(uid, toEmails, subject, slotStartTime, slotEndTime);
        multipart.addBodyPart(buildHtmlTextPart(emailContext), 0);
        multipart.addBodyPart(messageBodyPart, 1);
        message.setContent(multipart);

        mailSender.send(message);
    }


    private BodyPart buildCalendarPart(String uid, String[] toEmails,String subject, Date startTime, Date endTime) throws Exception {

        BodyPart calendarPart = new MimeBodyPart();
        SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

        String toEmailDesc = new String();
        for(String toEmail : toEmails){
            toEmailDesc += "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:" + toEmail + "\n";
        }
        final String calendarContent = "BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n" + "PRODID: Microsoft Exchange Server 2010\n" +  "VERSION:2.0\n" + "BEGIN:VEVENT\n" + "DTSTAMP:" + iCalendarDateFormat.format(startTime) + "\n" + "DTSTART:" + iCalendarDateFormat.format(startTime) + "\n" + "DTEND:" + iCalendarDateFormat.format(endTime) + "\n" + "SUMMARY:" + subject + "\n" + uid +"\n" + toEmailDesc + "\n" + "ORGANIZER:MAILTO:"+ emailId +"\n"
                + "LOCATION:Microsoft Teams Meeting\n" + "DESCRIPTION:" + subject + "\n" + "SEQUENCE:0\n" + "PRIORITY:5\n" + "CLASS:PUBLIC\n" + "STATUS:CONFIRMED\n" + "TRANSP:OPAQUE\n" + "BEGIN:VALARM\n" + "ACTION:DISPLAY\n" + "DESCRIPTION:REMINDER\n" + "END:VALARM\n" + "TRIGGER;RELATED=START:-PT00H15M00S\n" + "END:VEVENT\n" + "END:VCALENDAR";

        calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
        calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");

        return calendarPart;
    }

    private  BodyPart buildHtmlTextPart(String emailContent) throws MessagingException {
        MimeBodyPart descriptionPart = new MimeBodyPart();
        descriptionPart.setContent(emailContent, "text/html; charset=utf-8");
        return descriptionPart;
    }
}
