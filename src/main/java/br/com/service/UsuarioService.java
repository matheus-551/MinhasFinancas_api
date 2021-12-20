package br.com.service;

import br.com.model.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario SalvarUsuario(Usuario usuario);
	
	void ValidarEmail(String email);
}
