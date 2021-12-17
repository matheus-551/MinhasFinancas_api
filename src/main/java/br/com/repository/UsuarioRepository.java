package br.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
