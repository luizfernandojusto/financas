package br.com.sistema.financas.service;

import java.util.Optional;

import br.com.sistema.financas.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);

	Usuario salvarUsuario(Usuario usuario);

	void validarEmail(String email);

	Optional<Usuario> obterPorId(Long id);

}
