package ufjf.poo.controller.validadores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;
import ufjf.poo.model.disciplina.DisciplinaObrigatoria;
import ufjf.poo.model.disciplina.NotaDisciplina;

import java.util.Arrays;
import java.util.List;

@DisplayName("Testes do ValidadorLogicoOR")
class ValidadorLogicoORTest {
    
    private ValidadorLogicoOR validador;
    private Aluno aluno;
    private Disciplina disciplina1;
    private Disciplina disciplina2;
    private Disciplina disciplina3;
    private Disciplina disciplinaAtual;
    
    @BeforeEach
    void setUp() {
        disciplina1 = new DisciplinaObrigatoria("MAT001", "Cálculo I", 60);
        disciplina2 = new DisciplinaObrigatoria("MAT002", "Álgebra Linear", 60);
        disciplina3 = new DisciplinaObrigatoria("FIS001", "Física I", 60);
        disciplinaAtual = new DisciplinaObrigatoria("MAT003", "Cálculo Avançado", 60);
        
        List<Disciplina> disciplinasRequeridas = Arrays.asList(disciplina1, disciplina2, disciplina3);
        validador = new ValidadorLogicoOR(disciplinasRequeridas);
        aluno = new Aluno("João Silva", "202501001");
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando apenas uma disciplina foi cursada com nota >= 60")
    void testValidacaoSucessoUmaDisciplina() {
        // aluno cursou apenas uma disciplina com nota suficiente
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1));
        
        assertTrue(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando múltiplas disciplinas foram cursadas")
    void testValidacaoSucessoMultiplasDisciplinas() {
        // aluno cursou múltiplas disciplinas
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1));
        aluno.adicionarDisciplina(new NotaDisciplina(65.0f, disciplina2));
        
        assertTrue(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando todas as disciplinas foram cursadas")
    void testValidacaoSucessoTodasDisciplinas() {
        // aluno cursou todas as disciplinas
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1));
        aluno.adicionarDisciplina(new NotaDisciplina(65.0f, disciplina2));
        aluno.adicionarDisciplina(new NotaDisciplina(80.0f, disciplina3));
        
        assertTrue(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando uma disciplina foi cursada com nota exatamente 60")
    void testValidacaoSucessoNotaMinima() {
        // aluno cursou uma disciplina com nota mínima
        aluno.adicionarDisciplina(new NotaDisciplina(60.0f, disciplina2));
        
        assertTrue(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve falhar quando nenhuma disciplina foi cursada")
    void testValidacaoFalhaNenhumaDisciplinaCursada() {
        // aluno não cursou nenhuma disciplina
        assertFalse(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve falhar quando todas as disciplinas foram cursadas com notas < 60")
    void testValidacaoFalhaTodasNotasInsuficientes() {
        // aluno cursou todas as disciplinas com notas insuficientes
        aluno.adicionarDisciplina(new NotaDisciplina(50.0f, disciplina1));
        aluno.adicionarDisciplina(new NotaDisciplina(45.0f, disciplina2));
        aluno.adicionarDisciplina(new NotaDisciplina(30.0f, disciplina3));
        
        assertFalse(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando pelo menos uma disciplina tem nota >= 60, mesmo com outras insuficientes")
    void testValidacaoSucessoUmaNotaSuficienteOutrasInsuficientes() {
        // aluno cursou disciplinas com notas mistas, mas pelo menos uma suficiente
        aluno.adicionarDisciplina(new NotaDisciplina(50.0f, disciplina1)); // insuficiente
        aluno.adicionarDisciplina(new NotaDisciplina(75.0f, disciplina2)); // suficiente
        aluno.adicionarDisciplina(new NotaDisciplina(40.0f, disciplina3)); // insuficiente
        
        assertTrue(validador.validar(aluno, disciplinaAtual));
    }
}
