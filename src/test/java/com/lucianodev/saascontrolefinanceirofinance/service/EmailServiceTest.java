package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.exception.EmailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender javaMailSender;


    @Test
    public void deveEnviarEmailComSucesso() {
        emailService.sendEmail("destino@email.com", "Assunto Teste", "Corpo da Mensagem");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage msgCapturada = captor.getValue();
        assertEquals("destino@email.com", msgCapturada.getTo()[0]);
        assertEquals("Assunto Teste", msgCapturada.getSubject());
        assertEquals("Corpo da Mensagem", msgCapturada.getText());
    }

    @Test
    public void deveLancarEmailException_QuandoHouverErroDeEnvio() {
        doThrow(new MailSendException("SMTP Timeout"))
                .when(javaMailSender).send(any(SimpleMailMessage.class));

        assertThrows(EmailException.class, () ->
                emailService.sendEmail("destino@email.com", "Assunto", "Corpo"));
    }
}
