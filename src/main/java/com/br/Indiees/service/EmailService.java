package com.br.Indiees.service;

import com.br.Indiees.model.entity.Usuario;

import java.util.Optional;

public interface EmailService {

	String envioSenha(String email, String senha);

	
}
