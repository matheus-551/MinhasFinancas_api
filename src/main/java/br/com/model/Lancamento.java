package br.com.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.model.enums.StatusLancamento;
import br.com.model.enums.TipoLancamento;
import lombok.Builder;
import lombok.Data;

@Entity
@Builder
@Data
public class Lancamento {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "descricao")
	private String descricao;
	@Column(name = "mes")
	private Integer mes;
	@Column(name = "ano")
	private Integer ano;
	@JoinColumn(name = "id_usuario")
	@ManyToOne
	private Usuario usuario;
	@Column(name = "valor")
	private BigDecimal valor;
	@Column(name = "data_cadastro")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataCadastro;
	@Column(name = "tipo")
	@Enumerated(EnumType.STRING)
	private TipoLancamento tipoLancamento;
	@Column(name = "status_lancamento")
	@Enumerated(EnumType.STRING)
	private StatusLancamento statusLancamento;
	
}
