package sevensmurfs.rehub.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EmailProperties {

    @Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private Integer port;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String tlsEnable;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;
}

