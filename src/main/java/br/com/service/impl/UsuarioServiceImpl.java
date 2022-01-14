package br.com.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.exception.ErroAutenticacao;
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
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado com o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario SalvarUsuario(Usuario usuario) {
		ValidarEmail(usuario.getEmail());
		usuario.setDataCadastro(LocalDate.now());
		return usuarioRepository.save(usuario);
	}

	@Override
	public void ValidarEmail(String email) {
		Boolean EmailExiste = usuarioRepository.existsByEmail(email);
		
		if(EmailExiste) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse email");
		}
	}
	
}
