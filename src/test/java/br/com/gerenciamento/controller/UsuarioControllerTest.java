package br.com.gerenciamento.controller;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.service.ServiceUsuario;
import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UsuarioControllerTest {

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private ServiceUsuario serviceUsuario;

    @Test
    public void loginCredenciaisCorretas() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("joao.silva@ufape.edu.br");
        usuario.setUser("João Silva");
        usuario.setSenha("password");
        serviceUsuario.salvarUsuario(usuario);

        // Criar usuário para login com os mesmos dados
        Usuario usuarioLogin = new Usuario();
        usuario.setEmail("joao.silva@ufape.edu.br");
        usuarioLogin.setUser("João Silva");
        usuarioLogin.setSenha("password");
        BindingResult bindingResult = new BeanPropertyBindingResult(usuarioLogin, "usuario");
        MockHttpSession session = new MockHttpSession();

        // When
        ModelAndView resultado = usuarioController.login(usuarioLogin, bindingResult, session);

        // Then
        assertEquals("home/index", resultado.getViewName());
        assertNotNull(resultado.getModel().get("aluno"));
        assertNotNull(session.getAttribute("usuarioLogado"));
        
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        assertEquals("João Silva", usuarioLogado.getUser());
    }

    @Test
    public void loginCredenciaisIncorretas() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setEmail("joao.silva@ufape.edu.br");
        usuario.setUser("João Silva");
        usuario.setSenha("password");
        BindingResult bindingResult = new BeanPropertyBindingResult(usuario, "usuario");
        MockHttpSession session = new MockHttpSession();

        // When
        ModelAndView resultado = usuarioController.login(usuario, bindingResult, session);

        // Then
        assertEquals("login/cadastro", resultado.getViewName());
        assertNotNull(resultado.getModel().get("usuario"));
        assertNull(session.getAttribute("usuarioLogado"));
    }


    @Test
    public void salvarUsuario() throws Exception {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUser("novoUsuario");
        usuario.setEmail("novo@email.com");
        usuario.setSenha("minhaSenh@123");

        // When
        ModelAndView resultado = usuarioController.cadastrar(usuario);

        // Then
        assertEquals("redirect:/", resultado.getViewName());
        
        // Verificar se o usuário foi realmente salvo no banco
        Usuario usuarioSalvo = serviceUsuario.loginUser("novoUsuario", usuario.getSenha());
        assertNotNull(usuarioSalvo);
        assertEquals("novoUsuario", usuarioSalvo.getUser());
        assertEquals("novo@email.com", usuarioSalvo.getEmail());
    }

    @Test
    public void logout_deveInvalidarSessaoERedirecionarParaLogin_quandoChamado() {
        // Given - criar sessão com usuário logado
        MockHttpSession session = new MockHttpSession();
        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setUser("admin");
        usuarioLogado.setSenha("password123");
        usuarioLogado.setEmail("admin@teste.com");
        session.setAttribute("usuarioLogado", usuarioLogado);
        
        // Verificar que o usuário está na sessão
        assertNotNull(session.getAttribute("usuarioLogado"));

        // When
        ModelAndView resultado = usuarioController.logout(session);

        // Then
        assertEquals("login/login", resultado.getViewName());
        assertNotNull(resultado.getModel().get("usuario"));
        
        // Verificar que a sessão foi invalidada
        assertTrue(session.isInvalid());
    }
}
