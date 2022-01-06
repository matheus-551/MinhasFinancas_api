package br.com.service.test;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.exception.ErroAutenticacao;
import br.com.exception.RegraNegocioException;
import br.com.model.Usuario;
import br.com.repository.UsuarioRepository;
import br.com.service.impl.UsuarioServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@MockBean
	UsuarioRepository repository;
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@Test
	public void DeveSalvarUmUsuario() {
		//cenário
		Mockito.doNothing().when(service).ValidarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(11)
				.nome("nome")
				.email("email@email.com")
				.senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//ação
		Usuario usuarioSalvo = service.SalvarUsuario(new Usuario());
		
		//verificação
		Assertions.assertNotNull(usuarioSalvo);
		Assertions.assertEquals(usuarioSalvo.getId(), 11);
		Assertions.assertEquals(usuarioSalvo.getNome(), "nome");
		Assertions.assertEquals(usuarioSalvo.getEmail(), "email@email.com");
		Assertions.assertEquals(usuarioSalvo.getSenha(), "senha");
	}
	
	@Test
	public void NaoDeveSalvarUsuarioComEmailJaCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			//cenário
			String email = "email@email.com";
			Usuario usuario = Usuario.builder().email(email).build();
			Mockito.doThrow(RegraNegocioException.class).when(service).ValidarEmail(email);
			
			//ação
			service.SalvarUsuario(usuario);
			
			//verificação
			Mockito.verify( repository, Mockito.never()).save(usuario);
		});
	}
	
	@Test
	public void DeveAutenticarUmUsuarioComSucesso() {
		//cenário
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(11).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//ação 
		Usuario result = service.autenticar(email, senha);
		
		//verificação
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void DeveLancarErroQuandoNaoEncontrarUsuarioComOEmailInformado() {
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			
			//cenário
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			//ação
			service.autenticar("email@email.com", "senha");
		});	
	}
	
	@Test
	public void DeveLancarErroQuandoNaoEncontrarUsuarioComASenhaInformada() {
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			//cenário
			String senha = "senha";
			Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
			
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
			
			//ação
			service.autenticar("email@email.com", "123");
		});
	}
	
	@Test
	public void DeveValidarEmail() {
		Assertions.assertDoesNotThrow(() -> {
			//Cenário
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
			
			//Ação
			service.ValidarEmail("email@email.com");
		});
	}
	
	@Test
	public void DeveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			//Cenário 
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			//Ação
			service.ValidarEmail("Email@email.com");
		});
	}
}
