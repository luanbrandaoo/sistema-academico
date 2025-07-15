package ufjf.poo.controller.validadores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;
import ufjf.poo.model.disciplina.DisciplinaObrigatoria;
import ufjf.poo.model.disciplina.NotaDisciplina;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do ValidadorLogicoAND")
class ValidadorLogicoANDTest {
    
    private ValidadorLogicoAND validador;
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
        disciplinaAtual = new DisciplinaObrigatoria("MAT003", "Cálculo III", 60);
        
        List<Disciplina> disciplinasRequeridas = Arrays.asList(disciplina1, disciplina2, disciplina3);
        validador = new ValidadorLogicoAND(disciplinasRequeridas);
        assertDoesNotThrow(
                () -> aluno = new Aluno("João Silva", "202501001"),
                "Teste de criar aluno falhou"
        );
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando todas as disciplinas pré-requisito foram cursadas com nota >= 60")
    void testValidacaoSucessoTodasDisciplinas() {
        // aluno cursou todas as disciplinas pré-requisito com notas suficientes
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1));
        aluno.adicionarDisciplina(new NotaDisciplina(65.0f, disciplina2));
        aluno.adicionarDisciplina(new NotaDisciplina(80.0f, disciplina3));
        
        assertTrue(validador.validar(aluno));
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando todas as disciplinas foram cursadas com nota exatamente 60")
    void testValidacaoSucessoNotasMinimas() {
        // aluno cursou todas as disciplinas pré-requisito com nota mínima
        aluno.adicionarDisciplina(new NotaDisciplina(60.0f, disciplina1));
        aluno.adicionarDisciplina(new NotaDisciplina(60.0f, disciplina2));
        aluno.adicionarDisciplina(new NotaDisciplina(60.0f, disciplina3));
        
        assertTrue(validador.validar(aluno));
    }
    
    @Test
    @DisplayName("Deve falhar quando uma disciplina pré-requisito não foi cursada")
    void testValidacaoFalhaUmaDisciplinaNaoCursada() {
        // aluno cursou apenas duas das três disciplinas
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1));
        aluno.adicionarDisciplina(new NotaDisciplina(65.0f, disciplina2));
        // disciplina3 não foi cursada
        
        assertFalse(validador.validar(aluno));
    }
    
    @Test
    @DisplayName("Deve falhar quando uma disciplina foi cursada com nota < 60")
    void testValidacaoFalhaUmaNotaInsuficiente() {
        // aluno cursou todas as disciplinas, mas uma com nota insuficiente
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1));
        aluno.adicionarDisciplina(new NotaDisciplina(59.0f, disciplina2)); // Nota insuficiente
        aluno.adicionarDisciplina(new NotaDisciplina(80.0f, disciplina3));
        
        assertFalse(validador.validar(aluno));
    }
    
    @Test
    @DisplayName("Deve falhar quando nenhuma disciplina foi cursada")
    void testValidacaoFalhaNenhumaDisciplinaCursada() {
        // aluno não cursou nenhuma disciplina
        assertFalse(validador.validar(aluno));
    }
    
    @Test
    @DisplayName("Deve falhar quando todas as disciplinas foram cursadas com notas insuficientes")
    void testValidacaoFalhaTodasNotasInsuficientes() {
        // aluno cursou todas as disciplinas com notas insuficientes
        aluno.adicionarDisciplina(new NotaDisciplina(50.0f, disciplina1));
        aluno.adicionarDisciplina(new NotaDisciplina(45.0f, disciplina2));
        aluno.adicionarDisciplina(new NotaDisciplina(30.0f, disciplina3));
        
        assertFalse(validador.validar(aluno));
    }
}
