package br.com.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import br.com.model.Lancamento;
import br.com.model.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamento SalvaLancamento(Lancamento lancamento);
	
	Lancamento AtualizarLancamento(Lancamento lancamento);
	
	void deletarLancamento(Lancamento lancamento);
	
	List<Lancamento> ListaLancamentoUsuario(long idUsuario);
	
	List<Lancamento> BuscaLancamento(Lancamento lancamentoFiltro);
	
	void AtualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void ValidarLancamento(Lancamento lancamento);
	
	Optional<Lancamento> ObterPorId(Long id);
	
	BigDecimal ObteSaldoUsuario(Long id);
}
