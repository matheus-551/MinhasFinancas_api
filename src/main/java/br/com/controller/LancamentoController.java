package br.com.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dto.LancamentoDTO;
import br.com.exception.RegraNegocioException;
import br.com.model.Lancamento;
import br.com.model.Usuario;
import br.com.model.enums.StatusLancamento;
import br.com.model.enums.TipoLancamento;
import br.com.service.LancamentoService;
import br.com.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor 
public class LancamentoController {
	
	private final LancamentoService lancamentoService;
	private final UsuarioService usuarioService;
		
	//Método que converte o DTO em lancamento
	private Lancamento converter(LancamentoDTO lancamentoDto) {
		Lancamento lancamento = new Lancamento(	);
		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setAno(lancamentoDto.getAno());
		lancamento.setMes(lancamentoDto.getMes());
		lancamento.setValor(lancamentoDto.getValor());
		
		Usuario usuario = usuarioService
			.obterPorId(lancamentoDto.getUsuario())
			.orElseThrow( () -> new RegraNegocioException("Usuário não encontrado"));
		
		lancamento.setUsuario(usuario);
		
		if(lancamentoDto.getTipo() != null) {
			lancamento.setTipoLancamento(TipoLancamento.valueOf(lancamentoDto.getTipo()));
		}
		
		if(lancamentoDto.getStatus() != null) {
			lancamento.setStatusLancamento(StatusLancamento.valueOf(lancamentoDto.getStatus()));
		}

		return lancamento;
	}
	
	@PostMapping
	public ResponseEntity SalvarLancamento(@RequestBody LancamentoDTO lancamentoDto) {
		try {
			Lancamento lancamento = converter(lancamentoDto);
			lancamento = lancamentoService.SalvaLancamento(lancamento);
			
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizarLancamento(@PathVariable("id") Long id, @RequestBody LancamentoDTO lancamentoDto) {
		return lancamentoService.ObterPorId(id).map( entity -> {
			try {
				Lancamento lancamento = converter(lancamentoDto);
				lancamento.setId(entity.getId());
				lancamento.setDataCadastro(entity.getDataCadastro());
				lancamentoService.AtualizarLancamento(lancamento);
				
				return ResponseEntity.ok(lancamento);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet( () -> 
			new ResponseEntity("Lancamento não encontrado.", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity FiltrarLancamento(
				@RequestParam(value = "descricao", required = false) String descricao,
				@RequestParam(value = "mes", required = false) Integer mes,
				@RequestParam(value = "ano", required = false) Integer ano,
				@RequestParam("usuario") Long idUsuario
			) {
		
		System.out.println(descricao);
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuário não encontrado.");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = lancamentoService.BuscaLancamento(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity DeletarLancamento(@PathVariable("id") Long id) {
		return lancamentoService.ObterPorId(id).map( entity -> {
			lancamentoService.deletarLancamento(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> 
			new ResponseEntity("Lancamento não encontrado.", HttpStatus.BAD_REQUEST));
	}
}
