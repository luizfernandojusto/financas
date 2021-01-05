package br.com.sistema.financas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sistema.financas.model.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);

}
