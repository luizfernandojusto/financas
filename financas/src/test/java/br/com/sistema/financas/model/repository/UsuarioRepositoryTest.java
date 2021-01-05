package br.com.sistema.financas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.sistema.financas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // testar base de dados
@AutoConfigureTestDatabase(replace = Replace.NONE) // nao cria uma instancia removendo meu banco de teste(H2)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;

	@Autowired
	TestEntityManager testEntityManager;

	@Test
	public void deveVerificarAExistenciaDeEmail() {
		Usuario usuario = criarUsuario();
		testEntityManager.persist(usuario);
		boolean resultado = repository.existsByEmail("usuario@gmail.com");
		Assertions.assertThat(resultado).isTrue();
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		boolean resultado = repository.existsByEmail("usuario@gmail.com");

		Assertions.assertThat(resultado).isFalse();
	}

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		Usuario usuario = criarUsuario();
		Usuario usuarioSalvo = repository.save(usuario);
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}

	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		Usuario usuario = criarUsuario();
		testEntityManager.persist(usuario);

		Optional<Usuario> resultado = repository.findByEmail("usuario@gmail.com");
		
		Assertions.assertThat(resultado.isPresent()).isTrue();

	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExistirNaBase() {

		Optional<Usuario> resultado = repository.findByEmail("usuario@gmail.com");
		
		Assertions.assertThat(resultado.isPresent()).isFalse();

	}

	public static Usuario criarUsuario() {
		return Usuario.builder().nome("usuario").email("usuario@gmail.com").build();
	}

}
