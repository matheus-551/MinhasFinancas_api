package br.com.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.model.Lancamento;
import br.com.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	// Busca aproximada de lancamento através da descricao
	List<Lancamento> findByDescricaoIgnoreCaseContaining(String descricao);
	
	// Busca lancamento por ano
	List<Lancamento> findByAno(Integer ano);
	
	// Busca lancamento por mes
	List<Lancamento> findByMes(Integer mes);
	
	@Query(value = "select sum(l.valor) from Lancamento l join l.usuario u where u.id = :idUsuario and l.tipoLancamento = :tipo group by u")
	BigDecimal ObterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo);
}
