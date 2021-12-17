package br.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
