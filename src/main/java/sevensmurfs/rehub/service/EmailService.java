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

import java.nio.charset.StandardCharsets;
import java.util.Date;

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

    public void sendEmailExample() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                                                             MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                             StandardCharsets.UTF_8.name());

            Context context = new Context(LocaleContextHolder.getLocale());
            /**
             * This function assigns value to given key from template
             */
            context.setVariable("key", "value"); // FIXME

            String html;

            // Set right name of the template without extension
            html = springTemplateEngine.process("", context);

            // Set email subject according to your needs
            helper.setSubject(""); // FIXME
            helper.setTo("");   // Email of receiver FIXME
            helper.setText(html, true);
            helper.setSentDate(new Date());
            log.debug("Sending email to {}", "email@example.com");      // FIXME
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
