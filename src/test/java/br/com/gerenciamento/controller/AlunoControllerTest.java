package br.com.gerenciamento.controller;
import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.service.ServiceAluno;

import org.junit.runner.RunWith;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.junit.Test;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AlunoControllerTest {

    @Autowired
    private AlunoController alunoController;

    @Autowired
    private ServiceAluno serviceAluno;

    @Test
    public void inserirAluno() throws Exception {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setMatricula("11111");
        BindingResult bindingResult = new BeanPropertyBindingResult(aluno, "aluno");

        // When
        ModelAndView resultado = alunoController.inserirAluno(aluno, bindingResult);

        // Then
        Assertions.assertEquals("redirect:/alunos-adicionados", resultado.getViewName());
        
        // Verificar se o aluno foi realmente salvo no banco
        List<Aluno> alunos = serviceAluno.findAll();
        boolean alunoEncontrado = alunos.stream()
            .anyMatch(a -> "João Silva".equals(a.getNome()) && Curso.ADMINISTRACAO.equals(a.getCurso()));
        Assertions.assertTrue(alunoEncontrado);
    }

    @Test
    public void listagemAlunos() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setMatricula("11111");
        serviceAluno.save(aluno);

        Aluno aluno2 = new Aluno();
        aluno2.setNome("Maria João");
        aluno2.setStatus(Status.ATIVO);
        aluno2.setTurno(Turno.MATUTINO);
        aluno2.setCurso(Curso.ADMINISTRACAO);
        aluno2.setMatricula("22222");
        this.serviceAluno.save(aluno2);

        // When
        ModelAndView resultado = alunoController.listagemAlunos();

        // Then
        assertEquals("Aluno/listAlunos", resultado.getViewName());
        assertNotNull(resultado.getModel().get("alunosList"));
        
        @SuppressWarnings("unchecked")
        List<Aluno> alunosRetornados = (List<Aluno>) resultado.getModel().get("alunosList");
        Assertions.assertTrue(alunosRetornados.size() >= 2);
    
    }

    @Test
    public void editarAluno() throws Exception {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setMatricula("11111");
        serviceAluno.save(aluno);
        
        Long alunoId = aluno.getId();

        // When
        ModelAndView resultado = alunoController.editar(alunoId);

        // Then
        assertEquals("Aluno/editar", resultado.getViewName());
        assertNotNull(resultado.getModel().get("aluno"));
        
        Aluno alunoCarregado = (Aluno) resultado.getModel().get("aluno");
        assertEquals("João Silva", alunoCarregado.getNome());
        assertEquals(Curso.ADMINISTRACAO, alunoCarregado.getCurso());
        assertEquals(Status.ATIVO, alunoCarregado.getStatus());
        assertEquals(Turno.MATUTINO, alunoCarregado.getTurno());
    }

    @Test
    public void pesquisarAlunoFiltradosPorNome() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setMatricula("11111");
        serviceAluno.save(aluno);

        Aluno aluno2 = new Aluno();
        aluno2.setNome("Maria João");
        aluno2.setStatus(Status.ATIVO);
        aluno2.setTurno(Turno.MATUTINO);
        aluno2.setCurso(Curso.ADMINISTRACAO);
        aluno2.setMatricula("22222");
        this.serviceAluno.save(aluno2);

        // When
        ModelAndView resultado = alunoController.pesquisarAluno("João");

        // Then
        assertEquals("Aluno/pesquisa-resultado", resultado.getViewName());
        assertNotNull(resultado.getModel().get("ListaDeAlunos"));
        
        @SuppressWarnings("unchecked")
        List<Aluno> alunosEncontrados = (List<Aluno>) resultado.getModel().get("ListaDeAlunos");
        assertEquals(2, alunosEncontrados.size());
    }
}
