package com.br.Indiees.service.impl;

import com.br.Indiees.exception.ErroAutenticacao;
import com.br.Indiees.exception.RegraNegocioException;
import com.br.Indiees.model.entity.Usuario;
import com.br.Indiees.model.repository.PerfilRepository;
import com.br.Indiees.model.repository.UsuarioRepository;
import com.br.Indiees.service.EmailService;
import com.br.Indiees.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public String envioSenha(String email, String senha) {
		try {
			MimeMessage mail = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper( mail );
			helper.setTo(email );
			helper.setSubject( "Esqueci a senha" );
			helper.setText("<p>Segue usuario e senha </p>" +
					"<p>Usuario: " + email + "</p>" +
					"<p>Senha: " + senha + "</p>", true);
			mailSender.send(mail);

			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao enviar e-mail";
		}
	}
}
