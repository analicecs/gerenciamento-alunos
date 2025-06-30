package br.com.gerenciamento.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.util.Util;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UsuarioServiceTest {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @Test
    public void salvarUsuario() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("jose.silva@ufape.edu.br");
        usuario.setUser("José da Silva");
        usuario.setSenha("password");
        
        // When
        this.serviceUsuario.salvarUsuario(usuario);
        
        // Then
        Assert.assertNotNull(usuario.getId());
    }

    @Test
    public void salvarUsuarioSemEmail() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUser("José da Silva Oliveira");
        usuario.setSenha("password");

        // When & Then
        Assert.assertThrows(ConstraintViolationException.class, () -> {
            this.serviceUsuario.salvarUsuario(usuario);
        });
    }

    @Test
    public void loginUser() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("jose.silva@ufape.edu.br");
        usuario.setUser("José da Silva");
        usuario.setSenha("password");
        this.serviceUsuario.salvarUsuario(usuario);

        // When
        Usuario usuarioLogado = this.serviceUsuario.loginUser("José da Silva", Util.md5("password"));
        
        // Then
        Assert.assertNotNull(usuarioLogado);
        Assert.assertEquals("jose.silva@ufape.edu.br", usuarioLogado.getEmail());
        Assert.assertEquals("José da Silva", usuarioLogado.getUser());
    }

    @Test
    public void loginUserCredenciaisInvalidas() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("jose.silva@ufape.edu.br");
        usuario.setUser("José da Silva");
        usuario.setSenha("password");
        this.serviceUsuario.salvarUsuario(usuario);

        // When
        Usuario usuarioLogado = this.serviceUsuario.loginUser("José da Silva", Util.md5("wrongpassword"));
        
        // Then
        Assert.assertNull(usuarioLogado);
    }
}
