package br.com.sistema.financas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import br.com.sistema.financas.model.entity.Lancamento;
import br.com.sistema.financas.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);

	Lancamento atualizar(Lancamento lancamento);

	void deletar(Lancamento lancamento);

	List<Lancamento> buscar(Lancamento lancamentoFitro);

	void atualizarStatus(Lancamento lancamento, StatusLancamento status);

	void validar(Lancamento lancamento);

	Optional<Lancamento> obterPorId(Long id);

	BigDecimal obterSaldoPorUsuario(Long id);

}
