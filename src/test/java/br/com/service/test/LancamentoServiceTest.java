package br.com.service.test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.exception.RegraNegocioException;
import br.com.model.Lancamento;
import br.com.model.enums.StatusLancamento;
import br.com.repository.LancamentoRepository;
import br.com.repository.test.LancamentoRepositoryTest;
import br.com.service.impl.LancamentoServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@MockBean
	LancamentoRepository repository;
	
	@SpyBean
	LancamentoServiceImpl service;
	
	@Test
	public void DeveSalvarLancamento() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.CriaLancamento();
		Mockito.doNothing().when(service).ValidarLancamento(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.CriaLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		Lancamento lancamento = service.SalvaLancamento(lancamentoASalvar);
	
		Assertions.assertEquals(lancamento.getId(), lancamentoSalvo.getId());
		Assertions.assertEquals(lancamento.getStatus(), lancamentoSalvo.getStatus());
	}
	
	@Test
	public void NaoDeveSalvaLancamentoQuandoHouverErroDeValidacao() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			Lancamento lancamentoASalvar = LancamentoRepositoryTest.CriaLancamento();
			Mockito.doThrow(RegraNegocioException.class).when(service).ValidarLancamento(lancamentoASalvar);
			
			service.SalvaLancamento(lancamentoASalvar);
			
			Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		});		
	}
	
	@Test
	public void DeveAtualizarLancamento() {
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.CriaLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

		Mockito.doNothing().when(service).ValidarLancamento(lancamentoSalvo);
		
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		service.AtualizarLancamento(lancamentoSalvo);
		
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void DeveLancarErroAoAtualizarLancamento() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			Lancamento lancamento = LancamentoRepositoryTest.CriaLancamento();
			
			Mockito.doThrow(NullPointerException.class).when(service).AtualizarLancamento(lancamento);
			
			service.AtualizarLancamento(lancamento);
			
			Mockito.verify(repository, Mockito.never()).save(lancamento);
		});
	}
	
	@Test
	public void DeveDeletarUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.CriaLancamento();
		lancamento.setId(1l);
		
		service.deletarLancamento(lancamento);
		
		Mockito.verify(repository).delete(lancamento);
	}
	
	@Test
	public void NaoDeveDeletarLancamento() {
		Assertions.assertThrows(NullPointerException.class, () -> {			
			Lancamento lancamento = LancamentoRepositoryTest.CriaLancamento();
			
			Mockito.doThrow(NullPointerException.class).when(service).deletarLancamento(lancamento);
			
			service.deletarLancamento(lancamento);
			
			Mockito.verify(repository, Mockito.never()).delete(lancamento);
		});
	}
	
	@Test
	public void DeveFiltraLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.CriaLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> Lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(Lista);
		
		List<Lancamento> resultado = service.BuscaLancamento(lancamento);
		
		Assertions.assertNotNull(resultado);
	}
	
	@Test
	public void DeveAtualizaStatusLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.CriaLancamento();
		lancamento.setId(1l);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).AtualizarLancamento(lancamento);
		
		service.AtualizarStatus(lancamento, novoStatus);
		
		Assertions.assertEquals(lancamento.getStatus(), novoStatus);
		Mockito.verify(service).AtualizarLancamento(lancamento);
	}
	
	@Test
	public void deveObterLancamentoPorId() {
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.CriaLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		Optional<Lancamento> resultado = service.ObterPorId(id);
		
		Assertions.assertTrue(resultado.isPresent());
	}
	
	@Test
	public void DeveRetornaVazioQuandoNaoEncontraLancamentoPorId() {
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.CriaLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		Optional<Lancamento> resultado = service.ObterPorId(id);
		
		Assertions.assertFalse(resultado.isPresent());
	}
	
	@Test
	public void DevelancaUmaExcecaoAoValidarLancamento() {
		Lancamento lancamento = new Lancamento();
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			Mockito.doThrow(RegraNegocioException.class).when(service).ValidarLancamento(lancamento);
			
			service.ValidarLancamento(lancamento);
		});
	}
	
}
