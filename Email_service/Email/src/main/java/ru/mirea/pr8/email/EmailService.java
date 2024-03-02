package ru.mirea.pr8.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendMessage(String address) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String msg = "<div style=\"text-align: center\">\n" +
                "<h2>Requester Service</h2>\n" +
                "<h4>Ваш запрос был зарегистирован!</h4>\n" +
                "</div>";

        helper.setText(msg, true);
        helper.setTo(address);
        helper.setSubject("New Request");

        javaMailSender.send(message);
    }
}
