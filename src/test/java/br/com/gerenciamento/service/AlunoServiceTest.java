package br.com.gerenciamento.service;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AlunoServiceTest {

    @Autowired
    private ServiceAluno serviceAluno;

    @Test
    public void getById() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("Vinicius");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        this.serviceAluno.save(aluno);

        // When
        Long idGerado = aluno.getId();
        Aluno alunoRetorno = this.serviceAluno.getById(idGerado);
        
        // Then
        Assert.assertTrue(alunoRetorno.getNome().equals("Vinicius"));
        Assert.assertEquals(idGerado, alunoRetorno.getId());
    }

    @Test
    public void salvarSemNome() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");

        // When & Then
        Assert.assertThrows(ConstraintViolationException.class, () -> {
                this.serviceAluno.save(aluno);});
    }
    
    @Test
    public void findByNomeContainingIgnoreCase() {
        // Given
        Aluno aluno1 = new Aluno();
        aluno1.setId(1L);
        aluno1.setNome("João Vinicius Costa");
        aluno1.setTurno(Turno.MATUTINO);
        aluno1.setCurso(Curso.CONTABILIDADE);
        aluno1.setStatus(Status.ATIVO);
        aluno1.setMatricula("333123");
        this.serviceAluno.save(aluno1);

        Aluno aluno2 = new Aluno();
        aluno2.setNome("João Marcos Sales");
        aluno2.setTurno(Turno.MATUTINO);
        aluno2.setCurso(Curso.INFORMATICA);
        aluno2.setStatus(Status.ATIVO);
        aluno2.setMatricula("000001");
        this.serviceAluno.save(aluno2);
        
        // When
        List<Aluno> alunosEncontradosVinicius = this.serviceAluno.findByNomeContainingIgnoreCase("joão");

        // Then
        Assertions.assertThat(alunosEncontradosVinicius).isNotEmpty();
        Assertions.assertThat(alunosEncontradosVinicius).hasSize(2);
        Assertions.assertThat(alunosEncontradosVinicius)
            .extracting(Aluno::getNome)
            .containsExactlyInAnyOrder("João Vinicius Costa", "João Marcos Sales");

    }
    
    @Test
    public void findByNomeContainingIgnoreCaseAlunoInexistente() {
        // Given - nenhum usuário salvo
        
        // When
        List<Aluno> alunosEncontrados = this.serviceAluno.findByNomeContainingIgnoreCase("Alice");

        // Then
        Assertions.assertThat(alunosEncontrados).isEmpty();

    }
    
    @Test
    public void deleteById() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("Julia Oliveira");
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.CONTABILIDADE);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("000002");
        this.serviceAluno.save(aluno);

        // When
        Assertions.assertThat(this.serviceAluno.getById(aluno.getId())).isNotNull();
        
        Long idAluno = aluno.getId();

        this.serviceAluno.deleteById(aluno.getId());

        // Then
        Assertions.assertThatThrownBy(() -> this.serviceAluno.getById(idAluno))
	        .isInstanceOf(NoSuchElementException.class)
	        .hasMessageContaining("No value present");
    	
    }
    
    @Test
    public void findByStatusAtivo() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("Vinicius");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        this.serviceAluno.save(aluno);
        
        Aluno aluno2 = new Aluno();
        aluno2.setNome("João Marcos Sales");
        aluno2.setTurno(Turno.MATUTINO);
        aluno2.setCurso(Curso.INFORMATICA);
        aluno2.setStatus(Status.INATIVO);
        aluno2.setMatricula("000001");
        this.serviceAluno.save(aluno2);
        
        // when
        List<Aluno> alunosAtivos = this.serviceAluno.findByStatusAtivo();
        
        // Then
        Assertions.assertThat(alunosAtivos).isNotEmpty();
        Assertions.assertThat(alunosAtivos)
            .extracting(Aluno::getStatus)
            .containsOnly(Status.ATIVO);
        Assertions.assertThat(alunosAtivos)
            .extracting(Aluno::getNome)
            .contains("Vinicius")
            .doesNotContain("João Marcos Sales");
    }
}
