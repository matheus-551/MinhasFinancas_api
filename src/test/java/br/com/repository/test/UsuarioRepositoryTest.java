package br.com.repository.test;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.model.Usuario;
import br.com.repository.UsuarioRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	public static Usuario CriarUsuario() {
		return Usuario
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
	
	@Test
	public void verificaExistenciaEmail() {
		//cenário
		Usuario usuario = CriarUsuario();	
		entityManager.persist(usuario);
		
		//ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void DeveRetornarFalsoQuandoNaoHouverUsuarioCadastrado() {
		//cenário
		
		//ação
		boolean result = repository.existsByEmail("usuario@email.com");
		//verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void DevePersistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario = CriarUsuario();
		//ação
		Usuario UsuarioSalvo = repository.save(usuario);
		
		//verificação
		Assertions.assertThat(UsuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void DeveBuscarUmUsuarioPorEmail() {
		//cenário 
		Usuario usuario = CriarUsuario();
		entityManager.persist(usuario);
		
		//ação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void DeveRetornarVazioAoBuscarUsuarioPorEmail() {
		//cenário 
		
		//ação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result.isPresent()).isFalse();
	}
}
