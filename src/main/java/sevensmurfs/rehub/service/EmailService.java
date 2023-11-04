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
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.entity.TherapyResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private static final String REHUB = "ReHub";

    private final JavaMailSender javaMailSender;

    private final EmailProperties emailProperties;

    private final SpringTemplateEngine springTemplateEngine;

    /**
     *
     * TODO: Add methods for sending different templates according to our needs
     * e.g. template for password reset
     * e.g. template for notification
     * e.g. template for therapy appointment approval
     *
     */

    public void sendEmailExample(String recipientEmail, String recipientName, String rehabCenterName, List<Therapy> therapies) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            Context context = new Context(LocaleContextHolder.getLocale());
            /**
             * This function assigns value to given key from template
             */
            context.setVariable("recipientName", recipientName);
            context.setVariable("rehabCenterName", rehabCenterName);
            context.setVariable("key", "value"); // FIXME
            context.setVariable("therapies", therapies);

            String html;

            // Set right name of the template without extension
            html = springTemplateEngine.process("email-template", context);

            // Set email subject according to your needs
            helper.setSubject("Important message from ReHub!"); // FIXME
            helper.setTo(recipientEmail);   // Email of receiver FIXME
            helper.setText(html, true);
            helper.setSentDate(new Date());
            log.debug("Sending email to {}", recipientEmail);      // FIXME
            helper.setFrom(new InternetAddress(emailProperties.getEmail(), REHUB));
            javaMailSender.send(message);
        } catch (SMTPSendFailedException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.warn("Error occurred while trying to send mail.");
        }
    }

    public void sendTherapyConfirmedOrCanceled(String recipientEmail, String recipientName, Therapy therapy, LocalDateTime localDateTime, boolean confirmed) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            Context context = new Context(LocaleContextHolder.getLocale());
            /**
             * This function assigns value to given key from template
             */
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
            // Format the LocalDateTime using the formatter
            String formattedDate = localDateTime.format(formatterDate);
            String formattedTime = localDateTime.format(formatterTime);

            context.setVariable("recipientName", recipientName);
            context.setVariable("therapy", therapy);
            context.setVariable("therapyDate", formattedDate);
            context.setVariable("therapyTime", formattedTime);

            String html;

            // Set right name of the template without extension
            if (confirmed){
                html = springTemplateEngine.process("therapy-confirmed-template", context);
            }
            else{
                html = springTemplateEngine.process("therapy-canceled-template", context);

            }

            // Set email subject according to your needs
            helper.setSubject("Important message from ReHub!");
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
    public void sendTherapyResult(String recipientEmail, String recipientName, TherapyResult therapyResult) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            Context context = new Context(LocaleContextHolder.getLocale());

            context.setVariable("recipientName", recipientName);
            context.setVariable("therapyResult", therapyResult);

            String html;

            html = springTemplateEngine.process("therapy-result-template", context);

            // Set email subject according to your needs
            helper.setSubject("Important message from ReHub!");
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

    public void sendPasswordReset(String recipientEmail, String token) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            Context context = new Context(LocaleContextHolder.getLocale());

            String passwordResetlink = emailProperties.getPasswordResetLink() + token;

            context.setVariable("passwordResetLink", passwordResetlink);

            String html;

            html = springTemplateEngine.process("password-reset-template", context);

            // Set email subject according to your needs
            helper.setSubject("Important message from ReHub!");
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
}
