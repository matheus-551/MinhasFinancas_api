package br.com.service;

import java.util.List;

import br.com.model.Lancamento;
import br.com.model.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamento SalvaLancamento(Lancamento lancamento);
	
	Lancamento AtualizarLancamento(Lancamento lancamento);
	
	void deletarLancamento(Lancamento lancamento);
	
	List<Lancamento> BuscaLancamento(Lancamento lancamentoFiltro);
	
	void AtualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void ValidarLancamento(Lancamento lancamento);
}
