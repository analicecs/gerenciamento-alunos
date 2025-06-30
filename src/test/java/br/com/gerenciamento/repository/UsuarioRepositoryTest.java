package br.com.gerenciamento.repository;
import br.com.gerenciamento.model.Usuario;

import org.junit.runner.RunWith;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UsuarioRepositoryTest {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void findByEmail() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("jose.silva@ufape.edu.br");
        usuario.setUser("José da Silva");
        usuario.setSenha("password");
        this.usuarioRepository.save(usuario);

        // When
        Usuario resultado = this.usuarioRepository.findByEmail("jose.silva@ufape.edu.br");

        // Then
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(usuario.getEmail(), resultado.getEmail());
        Assertions.assertEquals(usuario.getUser(), resultado.getUser());
        Assertions.assertEquals(usuario.getSenha(), resultado.getSenha());
    }

    @Test
    public void findByEmailInexistente() {
        // Given - nenhum usuário salvo com esse email

        // When
        Usuario resultado = this.usuarioRepository.findByEmail("jose.silva@ufape.edu.br");

        // Then
        Assertions.assertNull(resultado);
    }

    @Test
    public void buscarLoginCredenciaisCorretas() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("jose.silva@ufape.edu.br");
        usuario.setUser("José da Silva");
        usuario.setSenha("password");
        this.usuarioRepository.save(usuario);

        // When
        Usuario resultado = this.usuarioRepository.buscarLogin("José da Silva", "password");

        // Then
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(usuario.getEmail(), resultado.getEmail());
        Assertions.assertEquals(usuario.getUser(), resultado.getUser());
        Assertions.assertEquals(usuario.getSenha(), resultado.getSenha());
    }

    @Test
    public void buscarLoginCredenciaisIncorretas() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("jose.silva@ufape.edu.br");
        usuario.setUser("José da Silva");
        usuario.setSenha("password");
        this.usuarioRepository.save(usuario);

        // When
        Usuario resultadoSenhaIncorreta = this.usuarioRepository.buscarLogin("José da Silva", "wrongPassword");

        // Then
        Assertions.assertNull(resultadoSenhaIncorreta);
    }
}
