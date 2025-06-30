package br.com.gerenciamento.repository;

import org.assertj.core.api.Assertions;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    public void findByStatusAtivo() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setMatricula("11111");
        alunoRepository.save(aluno);

        Aluno aluno2 = new Aluno();
        aluno2.setNome("Maria João");
        aluno2.setStatus(Status.INATIVO);
        aluno2.setTurno(Turno.MATUTINO);
        aluno2.setCurso(Curso.ADMINISTRACAO);
        aluno2.setMatricula("22222");
        alunoRepository.save(aluno2);

        // When
        List<Aluno> resultado = this.alunoRepository.findByStatusAtivo();

        // Then
        Assertions.assertThat(resultado).hasSize(1);
        Assertions.assertThat(resultado.get(0).getStatus()).isEqualTo(Status.ATIVO);
    }

    @Test
    public void findByStatusInativo() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setMatricula("11111");
        alunoRepository.save(aluno);

        Aluno aluno2 = new Aluno();
        aluno2.setNome("Maria João");
        aluno2.setStatus(Status.INATIVO);
        aluno2.setTurno(Turno.MATUTINO);
        aluno2.setCurso(Curso.ADMINISTRACAO);
        aluno2.setMatricula("22222");
        alunoRepository.save(aluno2);

        // When
        List<Aluno> resultado = alunoRepository.findByStatusInativo();

        // Then
        Assertions.assertThat(resultado).hasSize(1);
        Assertions.assertThat(resultado.get(0).getStatus()).isEqualTo(Status.INATIVO);
    }

    @Test
    public void findByNomeContainingIgnoreCase() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setMatricula("11111");
        alunoRepository.save(aluno);

        Aluno aluno2 = new Aluno();
        aluno2.setNome("Maria João");
        aluno2.setStatus(Status.ATIVO);
        aluno2.setTurno(Turno.MATUTINO);
        aluno2.setCurso(Curso.ADMINISTRACAO);
        aluno2.setMatricula("22222");
        alunoRepository.save(aluno2);

        // When
        List<Aluno> resultado = alunoRepository.findByNomeContainingIgnoreCase("joão");

        // Then
        Assertions.assertThat(resultado).hasSize(2);
    }

    @Test
    public void findByNomeContainingIgnoreCaseNenhumResultado() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setMatricula("11111");
        alunoRepository.save(aluno);

        // When
        List<Aluno> resultado = alunoRepository.findByNomeContainingIgnoreCase("Carlos Gabriel");

        // Then
        Assertions.assertThat(resultado).isEmpty();
    }
}