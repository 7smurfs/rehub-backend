package sevensmurfs.rehub.service;

import com.sun.mail.smtp.SMTPSendFailedException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import sevensmurfs.rehub.config.EmailProperties;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.entity.TherapyResult;
import sevensmurfs.rehub.model.message.request.UserRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private static final String REHUB = "ReHub";

    private final JavaMailSender javaMailSender;

    private final EmailProperties emailProperties;

    private final SpringTemplateEngine springTemplateEngine;

    public void sendTherapyResultEmail(String email, Patient patient, Therapy therapy, TherapyResult therapyResult) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            Context context = new Context(LocaleContextHolder.getLocale());

            context.setVariable("firstName", patient.getFirstName());
            context.setVariable("lastName", patient.getLastName());
            context.setVariable("therapyType", therapy.getType());
            context.setVariable("date", therapy.getAppointment().getStartAt().format(dateFormatter));
            context.setVariable("result", therapyResult.getResult());
            context.setVariable("status", therapyResult.getStatus());

            String html;
            html = springTemplateEngine.process("therapy-result-template", context);

            helper.setSubject("Rezultati Vaše terapije");
            helper.setTo(email);
            helper.setText(html, true);
            helper.setSentDate(new Date());
            log.debug("Sending email to {}", email);
            helper.setFrom(new InternetAddress(emailProperties.getEmail(), REHUB));
            javaMailSender.send(message);
        } catch (SMTPSendFailedException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.warn("Error occurred while trying to send mail.");
        }
    }

    public void sendPasswordReset(String recipientEmail, String token) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            String passwordResetlink = emailProperties.getPasswordResetLink() + token;

            Context context = new Context(LocaleContextHolder.getLocale());
            context.setVariable("passwordResetLink", passwordResetlink);

            String html;
            html = springTemplateEngine.process("password-reset-template", context);

            helper.setSubject("Promjena lozinke");
            helper.setTo(recipientEmail);
            helper.setText(html, true);
            helper.setSentDate(new Date());
            log.debug("Sending email to {}", recipientEmail);
            helper.setFrom(new InternetAddress(emailProperties.getEmail(), REHUB));
            javaMailSender.send(message);
        } catch (SMTPSendFailedException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.warn("Error occurred while trying to send mail.");
        }
    }

    public void sendAccountCreationInformationToEmployee(UserRequest userRequest, String password) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            Context context = new Context(LocaleContextHolder.getLocale());

            // Context for template
            context.setVariable("employeeName", userRequest.getFirstName());
            context.setVariable("employeeProfession", userRequest.getProfession().toLowerCase());
            context.setVariable("employeeUsername", userRequest.getUsername());
            context.setVariable("employeePassword", password);

            String html;
            html = springTemplateEngine.process("employee-account-created-template", context);
            helper.setSubject("Dobrodosli u ReHub");
            helper.setTo(userRequest.getUsername());
            helper.setText(html, true);
            helper.setSentDate(new Date());
            log.debug("Sending email to {}", userRequest.getUsername());
            helper.setFrom(new InternetAddress(emailProperties.getEmail(), REHUB));
            javaMailSender.send(message);
        } catch (SMTPSendFailedException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.warn("Error occurred while trying to send mail.");
        }
    }

    public void sendEmailForRegisteredUser(String firstName, String lastName, String email) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            Context context = new Context(LocaleContextHolder.getLocale());

            // Context for template
            context.setVariable("firstName", firstName);
            context.setVariable("lastName", lastName);

            String html;
            html = springTemplateEngine.process("patient-registered-template", context);
            helper.setSubject("Dobrodosli u ReHub");
            helper.setTo(email);
            helper.setText(html, true);
            helper.setSentDate(new Date());
            log.debug("Sending email to {}", email);
            helper.setFrom(new InternetAddress(emailProperties.getEmail(), REHUB));
            javaMailSender.send(message);
        } catch (SMTPSendFailedException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.warn("Error occurred while trying to send mail.");
        }
    }

    public void sendTherapyConfirmedNotification(String email, Patient patient, Therapy therapy, LocalDate therapyDate, LocalDateTime startAt, LocalDateTime endAt) {
        log.debug("Sending email for therapy confirmation.");
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            Context context = new Context(LocaleContextHolder.getLocale());

            // Context for template
            context.setVariable("firstName", patient.getFirstName());
            context.setVariable("lastName", patient.getLastName());
            context.setVariable("therapyType", therapy.getType());
            context.setVariable("date", therapyDate.format(dateFormatter));
            context.setVariable("startAt", startAt.format(timeFormatter));
            context.setVariable("endAt", endAt.format(timeFormatter));
            context.setVariable("employeePhone", therapy.getEmployee().getPhoneNumber());

            String html;
            html = springTemplateEngine.process("therapy-confirmed-template", context);
            helper.setSubject("Odobrenje terapije u ReHub");
            helper.setTo(email);
            helper.setText(html, true);
            helper.setSentDate(new Date());
            log.debug("Sending email to {}", email);
            helper.setFrom(new InternetAddress(emailProperties.getEmail(), REHUB));
            javaMailSender.send(message);
        } catch (SMTPSendFailedException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.warn("Error occurred while trying to send mail.");
        }
    }

}
