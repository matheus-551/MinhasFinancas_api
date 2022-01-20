package br.com.repository.test;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.model.Lancamento;
import br.com.model.enums.StatusLancamento;
import br.com.model.enums.TipoLancamento;
import br.com.repository.LancamentoRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	public static Lancamento CriaLancamento() {
		return Lancamento.builder()
				.ano(2022)
				.mes(1)
				.descricao("lançamento total")
				.valor(BigDecimal.valueOf(10))
				.tipoLancamento(TipoLancamento.RECEITA)
				.status(StatusLancamento.PENDENTE)
				.build();
	}
	
	public Lancamento CriaEPersistiLancamento() {		
		Lancamento lancamento = CriaLancamento();
		entityManager.persist(lancamento);
		return lancamento;
	}
	
	@Test
	public void DeveSalvaLancamento() {
		Lancamento lancamento = CriaLancamento();

		lancamento = repository.save(lancamento);
		
		Assertions.assertNotNull(lancamento.getId());
	}
	
	@Test
	public void DeveBuscaLancamentoPorId() {
		Lancamento lancamento = CriaEPersistiLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		Assertions.assertTrue(lancamentoEncontrado.isPresent());
	}
	
	@Test
	public void DeveAtualizarLancamento() {
		Lancamento lancamento = CriaEPersistiLancamento();
		
		lancamento.setAno(2020);
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertEquals(lancamentoAtualizado.getAno(), 2020);
		Assertions.assertEquals(lancamentoAtualizado.getStatus(), StatusLancamento.CANCELADO);
	}
	
	@Test
	public void DeveDeletarUmLancamento() {
		Lancamento lancamento = CriaEPersistiLancamento();
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
	
		Assertions.assertNull(lancamentoInexistente);
	}
}
