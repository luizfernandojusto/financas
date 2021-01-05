package br.com.sistema.financas.service.imp;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sistema.financas.exception.ErroAutenticacao;
import br.com.sistema.financas.exception.RegraNegocioException;
import br.com.sistema.financas.model.entity.Usuario;
import br.com.sistema.financas.model.repository.UsuarioRepository;
import br.com.sistema.financas.service.UsuarioService;

@Service
public class UsuarioServiceImp implements UsuarioService {

	private UsuarioRepository repository;

	public UsuarioServiceImp(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);

		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para email informado.");
		}

		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {	
		return repository.findById(id);
	}

}
