package ufjf.poo.controller.validadores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;
import ufjf.poo.model.disciplina.DisciplinaObrigatoria;
import ufjf.poo.model.disciplina.NotaDisciplina;

@DisplayName("Testes do ValidadorCreditosMinimos")
class ValidadorCreditosMininosTest {
    
    private ValidadorCreditosMinimos validador;
    private Aluno aluno;
    private Disciplina disciplina1;
    private Disciplina disciplina2;
    private Disciplina disciplina3;
    private Disciplina disciplinaAtual;
    
    @BeforeEach
    void setUp() {
        disciplina1 = new DisciplinaObrigatoria("MAT001", "Cálculo I", 60); // 4 créditos (60/15)
        disciplina2 = new DisciplinaObrigatoria("MAT002", "Álgebra Linear", 45); // 3 créditos
        disciplina3 = new DisciplinaObrigatoria("FIS001", "Física I", 75); // 5 créditos
        disciplinaAtual = new DisciplinaObrigatoria("MAT003", "Cálculo Avançado", 60);
        
        // validador que exige 10 créditos mínimos
        validador = new ValidadorCreditosMinimos(10);
        aluno = new Aluno("João Silva", "202501001");
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando aluno atingiu exatamente os créditos mínimos")
    void testValidacaoSucessoCreditosExatos() {
        // aluno cursou disciplinas que totalizam exatamente 10 créditos
        // disciplina1 (4) + disciplina2 (3) + disciplina3 (5) = 12 créditos
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1));
        aluno.adicionarDisciplina(new NotaDisciplina(65.0f, disciplina2));
        // Não adiciona disciplina3, então tem apenas 7 créditos
        
        // vou ajustar para ter exatamente 10 créditos
        Disciplina disciplinaExtra = new DisciplinaObrigatoria("MAT004", "Geometria", 45); // 3 créditos
        aluno.adicionarDisciplina(new NotaDisciplina(60.0f, disciplinaExtra));
        
        assertTrue(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve validar com sucesso quando aluno superou os créditos mínimos")
    void testValidacaoSucessoCreditosExcedentes() {
        // aluno cursou disciplinas que totalizam mais de 10 créditos
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1)); // 4 créditos
        aluno.adicionarDisciplina(new NotaDisciplina(65.0f, disciplina2)); // 3 créditos
        aluno.adicionarDisciplina(new NotaDisciplina(80.0f, disciplina3)); // 5 créditos
        // total: 12 créditos
        
        assertTrue(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve falhar quando aluno não atingiu os créditos mínimos")
    void testValidacaoFalhaCreditosInsuficientes() {
        // aluno cursou disciplinas que totalizam menos de 10 créditos
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1)); // 4 créditos
        aluno.adicionarDisciplina(new NotaDisciplina(65.0f, disciplina2)); // 3 créditos
        // total: 7 créditos (menos que 10)
        
        assertFalse(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve falhar quando aluno não cursou nenhuma disciplina")
    void testValidacaoFalhaNenhumaDisciplina() {
        // aluno não cursou nenhuma disciplina
        assertFalse(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve falhar quando disciplinas cursadas têm notas insuficientes (< 60)")
    void testValidacaoFalhaNotasInsuficientes() {
        // aluno cursou disciplinas com carga horária suficiente, mas notas insuficientes
        aluno.adicionarDisciplina(new NotaDisciplina(50.0f, disciplina1)); // Não conta
        aluno.adicionarDisciplina(new NotaDisciplina(45.0f, disciplina2)); // Não conta
        aluno.adicionarDisciplina(new NotaDisciplina(30.0f, disciplina3)); // Não conta
        
        assertFalse(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Deve contar apenas disciplinas com nota >= 60 para cálculo de créditos")
    void testValidacaoContaApenasNotasSuficientes() {
        // aluno cursou disciplinas mistas - algumas aprovado, outras reprovado
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1)); // 4 créditos - conta
        aluno.adicionarDisciplina(new NotaDisciplina(50.0f, disciplina2)); // 3 créditos - não conta
        aluno.adicionarDisciplina(new NotaDisciplina(80.0f, disciplina3)); // 5 créditos - conta
        // total de créditos válidos: 4 + 5 = 9 créditos (menos que 10)
        
        assertFalse(validador.validar(aluno, disciplinaAtual));
    }
    
    @Test
    @DisplayName("Teste com validador de créditos zero")
    void testValidadorCreditosZero() {
        ValidadorCreditosMinimos validadorZero = new ValidadorCreditosMinimos(0);
        
        // qualquer aluno deve passar com créditos mínimos zero
        assertTrue(validadorZero.validar(aluno, disciplinaAtual));
        
        // mesmo com disciplinas cursadas
        aluno.adicionarDisciplina(new NotaDisciplina(70.0f, disciplina1));
        assertTrue(validadorZero.validar(aluno, disciplinaAtual));
    }
}
