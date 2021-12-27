package br.com.service.test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.exception.RegraNegocioException;
import br.com.model.Usuario;
import br.com.repository.UsuarioRepository;
import br.com.service.UsuarioService;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void DeveValidarEmail() {
		Assertions.assertDoesNotThrow(() -> {
			//Cenário
			repository.deleteAll();
			
			//Ação
			service.ValidarEmail("email@email.com");
		});
	}
	
	@Test
	public void DeveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			//Cenário 
			Usuario usuario = Usuario.builder().nome("Usuario").email("Email@email.com").build();
			repository.save(usuario);
			//Ação
			service.ValidarEmail("Email@email.com");
		});
	}
}
