package com.br.Indiees.dto;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
public class TokenDTO {

	private String nome;
	private String email;
	private Boolean esqueci_senha;
	private String perfil;
	private String token;
	
}
