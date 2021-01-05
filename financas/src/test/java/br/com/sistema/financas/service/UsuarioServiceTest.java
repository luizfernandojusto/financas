package br.com.sistema.financas.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.sistema.financas.exception.ErroAutenticacao;
import br.com.sistema.financas.exception.RegraNegocioException;
import br.com.sistema.financas.model.entity.Usuario;
import br.com.sistema.financas.model.repository.UsuarioRepository;
import br.com.sistema.financas.service.imp.UsuarioServiceImp;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	UsuarioService service;

	@MockBean
	UsuarioRepository repository;

	@BeforeEach
	public void setup() {
		service = new UsuarioServiceImp(repository);
	}

	@Test
	public void deveAurenticarUmusuarioComSucesso() {
		Assertions.assertDoesNotThrow(() -> {

			String email = "email@email.com";
			String senha = "senha";

			Usuario usuario = Usuario.builder().email(email).senha(senha).build();
			Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

			Usuario resultado = service.autenticar(email, senha);
			


		});

	}

	@Test
	public void deveLancarErrorQuandoNaoEncontrarEmailCadastrado() {
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			Usuario resultado = service.autenticar("email@email.com", "senha");
		});
	}

	@Test
	public void deveLancarErrorQuandoSenhaNaoBater() {

		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha("senha").build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));


		
		
		Throwable exception  = 	Assertions.assertThrows(ErroAutenticacao.class, () -> {
			
			
		});
		
		

	}

	@Test
	public void deveValidarEmail() {
		Assertions.assertDoesNotThrow(() -> {
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
			service.validarEmail("email@email.com");
		});

	}

	@Test
	public void deveLancarErrorQuandoExistirEmailCadstrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			service.validarEmail("email@gmail.com");
		});
	}

}
