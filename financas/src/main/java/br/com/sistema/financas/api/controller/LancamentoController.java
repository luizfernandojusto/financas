package br.com.sistema.financas.api.controller;

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

import br.com.sistema.financas.api.dto.AtualizaStatusDTO;
import br.com.sistema.financas.api.dto.LancamentoDTO;
import br.com.sistema.financas.exception.RegraNegocioException;
import br.com.sistema.financas.model.entity.Lancamento;
import br.com.sistema.financas.model.entity.Usuario;
import br.com.sistema.financas.model.enums.StatusLancamento;
import br.com.sistema.financas.model.enums.TipoLancamento;
import br.com.sistema.financas.service.LancamentoService;
import br.com.sistema.financas.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor // mesma coisa de construtor e passando paramento
public class LancamentoController {

	private final LancamentoService service;
	private final UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano, @RequestParam("usuario") Long idUsuario) {

		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);

		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if (!usuario.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possivel realizar a consulta. Usuário não encontrado para o ID informado.");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);

	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO dto) {
		return service.obterPorId(id).map(entidade -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entidade.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));

	}

	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable Long id, @RequestBody AtualizaStatusDTO dto) {
		return service.obterPorId(id).map(entidade -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
			if (statusSelecionado == null) {
				ResponseEntity.badRequest()
						.body("Não foi possível atualizar p status do lançamento, envie um status válido");
			}

			try {
				entidade.setStatus(statusSelecionado);
				service.atualizar(entidade);
				return ResponseEntity.ok(entidade);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));

	}

	@DeleteMapping("{id}")
	public ResponseEntity delelar(@PathVariable Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}

	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());

		lancamento.setUsuario(usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o ID informado.")));

		if (dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}

		if (dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		return lancamento;
	}

}
