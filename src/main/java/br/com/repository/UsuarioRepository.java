package br.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
		
	public boolean existsByEmail(String email);
	
	public Optional<Usuario> findByEmail(String email);
}
