package com.br.Indiees.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.br.Indiees.dto.UsuarioDTO;
import com.br.Indiees.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.Indiees.exception.ErroAutenticacao;
import com.br.Indiees.exception.RegraNegocioException;
import com.br.Indiees.model.entity.Usuario;
import com.br.Indiees.model.repository.PerfilRepository;
import com.br.Indiees.model.repository.UsuarioRepository;
import com.br.Indiees.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;
	private PasswordEncoder encoder;
	private PerfilRepository perfilRepository;

	@Autowired
	private EmailService emailService;
	
	public UsuarioServiceImpl(
			UsuarioRepository repository, 
			PasswordEncoder encoder) {
		super();
		this.repository = repository;
		this.encoder = encoder;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		
		if(encoder.matches(senha, usuario.get().getSenha())) {
			return usuario.get();
		}else {
			throw new ErroAutenticacao("Senha inválida.");
		
		}

		
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		usuario.setData_criacao(new Date());
		validarEmail(usuario.getEmail());
		criptografarSenha(usuario);
		return repository.save(usuario);
	}

	private void criptografarSenha(Usuario usuario) {
		String senha = usuario.getSenha();
		String senhaCripto = encoder.encode(senha);
		usuario.setSenha(senhaCripto);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	public Usuario esqueciSenha(String email) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		usuario.get().setEsqueci_senha(Boolean.TRUE);
		UUID uuid = UUID.randomUUID();
		String senhaAleatoria = uuid.toString().substring(0,8)	;
		String senha = encoder.encode(senhaAleatoria);
		usuario.get().setSenha(senha);
		repository.save(usuario.get());
		emailService.envioSenha(usuario.get().getEmail(),senhaAleatoria );
		return usuario.get();
	}

	@Override
	public Usuario alterarSenha(UsuarioDTO usuarioDTO) {
		Optional<Usuario> usuario = repository.findByEmail(usuarioDTO.getEmail());
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		usuario.get().setEsqueci_senha(Boolean.FALSE);
		usuario.get().setSenha(encoder.encode(usuarioDTO.getSenha()));
		repository.save(usuario.get());
		return usuario.get();
	}
}
