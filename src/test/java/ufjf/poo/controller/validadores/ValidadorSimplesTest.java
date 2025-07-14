package ufjf.poo.controller.validadores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;
import ufjf.poo.model.disciplina.DisciplinaObrigatoria;
import ufjf.poo.model.disciplina.NotaDisciplina;

@DisplayName("Testes do ValidadorSimples")
class ValidadorSimplesTest {
    
    private ValidadorSimples validador;
    private Aluno aluno;
    private Disciplina disciplinaPreRequisito;
    private Disciplina disciplinaAtual;
    
    @BeforeEach
    void setUp() {
        disciplinaPreRequisito = new DisciplinaObrigatoria("MAT001", "Cálculo I", 60);
        disciplinaAtual = new DisciplinaObrigatoria("MAT002", "Cálculo II", 60);
        validador = new ValidadorSimples(disciplinaPreRequisito);
        aluno = new Aluno("João Silva", "202501001");
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando disciplina pré-requisito foi cursada com nota >= 60")
    void testValidacaoSucessoNotaSuficiente() {
        // aluno cursou a disciplina pré-requisito com nota 70
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplinaPreRequisito));
        
        assertTrue(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando disciplina pré-requisito foi cursada com nota exatamente 60")
    void testValidacaoSucessoNotaMinima() {
        // aluno cursou a disciplina pré-requisito com nota 60 (nota mínima)
        aluno.adicionarDisciplina(new NotaDisciplina(60.0f, disciplinaPreRequisito));
        
        assertTrue(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve falhar quando disciplina pré-requisito não foi cursada")
    void testValidacaoFalhaDisciplinaNaoCursada() {
        // aluno não cursou a disciplina pré-requisito
        assertFalse(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve falhar quando disciplina pré-requisito foi cursada com nota < 60")
    void testValidacaoFalhaNotaInsuficiente() {
        // aluno cursou a disciplina pré-requisito com nota 59
        aluno.adicionarDisciplina(new NotaDisciplina(59.0f, disciplinaPreRequisito));
        
        assertFalse(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve falhar quando disciplina pré-requisito foi cursada com nota 0")
    void testValidacaoFalhaNotaZero() {
        // aluno cursou a disciplina pré-requisito com nota 0
        aluno.adicionarDisciplina(new NotaDisciplina(0.0f, disciplinaPreRequisito));
        
        assertFalse(validador.validar(aluno, disciplinaAtual));
    }
}
