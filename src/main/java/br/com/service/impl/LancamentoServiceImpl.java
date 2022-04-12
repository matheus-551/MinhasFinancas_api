package br.com.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.exception.RegraNegocioException;
import br.com.model.Lancamento;
import br.com.model.enums.StatusLancamento;
import br.com.model.enums.TipoLancamento;
import br.com.repository.LancamentoRepository;
import br.com.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{

	private LancamentoRepository LancamentoRepository;
	
	@Autowired
	public LancamentoServiceImpl(LancamentoRepository LancamentoRepository) {
		this.LancamentoRepository = LancamentoRepository;
	}
	
	@Override
	@Transactional
	public Lancamento SalvaLancamento(Lancamento lancamento) {
		ValidarLancamento(lancamento);
		
		lancamento.setStatus(StatusLancamento.PENDENTE);
		lancamento.setDataCadastro(LocalDate.now());
		
		return this.LancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento AtualizarLancamento(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		ValidarLancamento(lancamento);
		return this.LancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletarLancamento(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		this.LancamentoRepository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> BuscaLancamento(Lancamento lancamentoFiltro) {
		Example<Lancamento> example = Example.of( lancamentoFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		List<Lancamento> lancamentos = this.LancamentoRepository.findAll(example);
		return lancamentos;
	}

	@Override
	public void AtualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		AtualizarLancamento(lancamento);
	}

	@Override
	public void ValidarLancamento(Lancamento lancamento) {
		
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descrição válida. ");
		}
		
		if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um mês válido.");
		}
		
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		if(lancamento.getUsuario() == null) {
			throw new RegraNegocioException("Informe um Usuario válido.");
		}
		
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um Valor Válido.");
		}
		
		if(lancamento.getTipoLancamento() == null) {
			throw new RegraNegocioException("O Tipo de lançamento é obrigatório.");
		}
	}

	@Override
	public Optional<Lancamento> ObterPorId(Long id) {
		return this.LancamentoRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal ObteSaldoUsuario(Long id) {
		BigDecimal receitas = this.LancamentoRepository.ObterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesas = this.LancamentoRepository.ObterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		
		if(receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		
		if(despesas == null) {
			despesas = BigDecimal.ZERO;
		}
		
		return receitas.subtract(despesas);
	}

}