package com.br.Indiees.resource;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.Indiees.dto.TokenDTO;
import com.br.Indiees.dto.UsuarioDTO;
import com.br.Indiees.exception.ErroAutenticacao;
import com.br.Indiees.exception.RegraNegocioException;
import com.br.Indiees.model.entity.Perfil;
import com.br.Indiees.model.entity.Usuario;
import com.br.Indiees.model.repository.PerfilRepository;
import com.br.Indiees.service.JwtService;
import com.br.Indiees.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

	private final UsuarioService service;
	private final JwtService jwtService;
	private final PerfilRepository perfilRepository;
	
	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar( @RequestBody UsuarioDTO dto ) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			String token = jwtService.gerarToken(usuarioAutenticado);
			TokenDTO tokenDTO = new TokenDTO( usuarioAutenticado.getNome(), token);
			return ResponseEntity.ok(tokenDTO);
		}catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@PostMapping("/salvar")
	public ResponseEntity<?> salvar( @RequestBody UsuarioDTO dto ) {
		
		Optional<Perfil> perfil = perfilRepository.findById(Long.parseLong(dto.getPerfil()));
		
		if(!perfil.isPresent()) {
			throw new RegraNegocioException("Perfil informado não existe");
		}
		
		Usuario usuario = Usuario.builder()
					.nome(dto.getNome())
					.email(dto.getEmail())
					.senha(dto.getSenha()).build();
		try {
			usuario.setPerfil(perfil.get());
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> update( @RequestBody UsuarioDTO dto ) {
		
		Optional<Perfil> perfil = perfilRepository.findById(Long.parseLong(dto.getPerfil()));
		
		if(!perfil.isPresent()) {
			throw new RegraNegocioException("Perfil informado não existe");
		}
		
		Usuario usuario = Usuario.builder()
					.nome(dto.getNome())
					.email(dto.getEmail())
					.senha(dto.getSenha()).build();
		try {
			usuario.setPerfil(perfil.get());
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	
	
	
}
