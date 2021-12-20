package br.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.exception.RegraNegocioException;
import br.com.model.Usuario;
import br.com.repository.UsuarioRepository;
import br.com.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	private UsuarioRepository usuarioRepository;
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public Usuario autenticar(String email, String senha) {
		
		return null;
	}

	@Override
	public Usuario SalvarUsuario(Usuario usuario) {
		
		return null;
	}

	@Override
	public void ValidarEmail(String email) {
		Boolean EmailExiste = usuarioRepository.existsByEmail(email);
		
		if(EmailExiste) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse email");
		}
	}
	
}
