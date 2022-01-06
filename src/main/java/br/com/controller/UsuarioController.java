package br.com.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dto.UsuarioDTO;
import br.com.exception.ErroAutenticacao;
import br.com.exception.RegraNegocioException;
import br.com.model.Usuario;
import br.com.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	private UsuarioService usuarioService;
	
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticarUsuario(@RequestBody UsuarioDTO usuarioDto) {
		try {
			Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDto.getEmail(), usuarioDto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		}catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity salvarUsuario(@RequestBody UsuarioDTO usuarioDto) {
		
		Usuario usuario = Usuario.builder()
				.nome(usuarioDto.getNome())
				.email(usuarioDto.getEmail())
				.senha(usuarioDto.getSenha()).build();
		
		try {
			Usuario usuarioSalvo = usuarioService.SalvarUsuario(usuario);
			
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
