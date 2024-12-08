package com.project.placement_management_app.service.email;

import com.project.placement_management_app.model.EmailTemplate;
import jakarta.mail.MessagingException;

import java.util.Date;
import java.util.Map;

public interface EmailService {

     void sendEmail(String[] toEmail, String subject, String emailContext, boolean isContentHTML, String[] ccEmail) throws MessagingException;
     String createEmailContext(String emailTemplateId, Map<String, String> emailParameters);
     String createSubject(String subject, Map<String, String> subjectParameters);
     EmailTemplate getEmailTemplate(String emailTemplateId);
     void sendCalendarInvite(String uid,
                             String[] toEmails, String subject, String emailContext,
                             boolean isContentHTML, Date slotStartTime, Date slotEndTime) throws Exception;
}
