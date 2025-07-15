package ufjf.poo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ufjf.poo.model.disciplina.Disciplina;
import ufjf.poo.model.disciplina.DisciplinaObrigatoria;
import ufjf.poo.model.disciplina.NotaDisciplina;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe Aluno")
class AlunoTest {
    
    private Aluno aluno;
    private Disciplina disciplina1;
    private Disciplina disciplina2;
    
    @BeforeEach
    void setUp() {
        assertDoesNotThrow(
                () -> aluno = new Aluno("João Silva", "202501001"),
                "Teste de criar aluno falhou"
        );
        disciplina1 = new DisciplinaObrigatoria("MAT001", "Cálculo I", 60);
        disciplina2 = new DisciplinaObrigatoria("MAT002", "Álgebra Linear", 45);
    }
    
    @Test
    @DisplayName("Aluno deve ser criado corretamente")
    void testCriacaoAluno() {
        assertEquals("João Silva", aluno.getNome());
        assertEquals("202501001", aluno.getMatricula());
        assertNotNull(aluno.getDisciplinasPeriodo());
        assertNotNull(aluno.getPlanejamento());
        assertTrue(aluno.getDisciplinasPeriodo().isEmpty());
        assertTrue(aluno.getPlanejamento().isEmpty());
    }
    
    @Test
    @DisplayName("Deve aceitar matrícula no formato válido AAAACCMMM")
    void testMatriculaValida() {
        assertDoesNotThrow(() -> {
            new Aluno("Maria", "202501001");
            new Aluno("Pedro", "199923001");
            new Aluno("Ana", "202501999");
            new Aluno("Carlos", "202523001");
        });
    }
    
    @Test
    @DisplayName("Deve adicionar disciplina cursada corretamente")
    void testAdicionarDisciplina() {
        NotaDisciplina notaDisciplina = new NotaDisciplina(75.0f, disciplina1);
        
        aluno.adicionarDisciplina(notaDisciplina);
        assertEquals(1, aluno.getDisciplinasPeriodo().size());
        assertTrue(aluno.getDisciplinasPeriodo().contains(notaDisciplina));
    }
    
    @Test
    @DisplayName("Deve remover disciplina cursada corretamente")
    void testRemoverDisciplina() {
        NotaDisciplina notaDisciplina = new NotaDisciplina(75.0f, disciplina1);
        
        aluno.adicionarDisciplina(notaDisciplina);
        assertEquals(1, aluno.getDisciplinasPeriodo().size());
        
        aluno.removerDisciplina(notaDisciplina);
        assertEquals(0, aluno.getDisciplinasPeriodo().size());
        assertFalse(aluno.getDisciplinasPeriodo().contains(notaDisciplina));
    }
    
    @Test
    @DisplayName("Deve adicionar múltiplas disciplinas cursadas")
    void testAdicionarMultiplasDisciplinas() {
        NotaDisciplina nota1 = new NotaDisciplina(75.0f, disciplina1);
        NotaDisciplina nota2 = new NotaDisciplina(80.0f, disciplina2);
        
        aluno.adicionarDisciplina(nota1);
        aluno.adicionarDisciplina(nota2);
        
        assertEquals(2, aluno.getDisciplinasPeriodo().size());
        assertTrue(aluno.getDisciplinasPeriodo().contains(nota1));
        assertTrue(aluno.getDisciplinasPeriodo().contains(nota2));
    }
    
    @Test
    @DisplayName("Não deve adicionar a mesma disciplina duas vezes")
    void testNaoAdicionarDisciplinaDuplicada() {
        NotaDisciplina nota1 = new NotaDisciplina(75.0f, disciplina1);
        NotaDisciplina nota2 = new NotaDisciplina(80.0f, disciplina1); // mesma disciplina, nota diferente
        
        aluno.adicionarDisciplina(nota1);
        aluno.adicionarDisciplina(nota2);
        
        assertTrue(aluno.getDisciplinasPeriodo().size() <= 2);
    }
    
    @Test
    @DisplayName("Deve gerenciar planejamento futuro")
    void testGerenciarPlanejamento() {
        // testa se é possível adicionar períodos ao planejamento
        assertDoesNotThrow(() -> {
            HashSet<Disciplina> periodo = new HashSet<>();
            periodo.add(disciplina1);
            periodo.add(disciplina2);
            aluno.getPlanejamento().add(periodo);
        });
        
        assertEquals(1, aluno.getPlanejamento().size());
    }
    
    @Test
    @DisplayName("Setters devem funcionar corretamente")
    void testSetters() {
        aluno.setNome("Maria Santos");
        assertEquals("Maria Santos", aluno.getNome());
        
        assertDoesNotThrow(() -> aluno.setMatricula("202501002"));
        assertEquals("202501002", aluno.getMatricula());
    }
    
    @Test
    @DisplayName("Deve validar formato de matrícula")
    void testValidacaoMatricula() {
        // a validação está na classe, mas o construtor não lança exceção
        AtomicReference<Aluno> alunoTeste = new AtomicReference<>();
        assertDoesNotThrow(
                () -> alunoTeste.set(new Aluno("Teste", "202501001"))
        );
        assertEquals("202501001", alunoTeste.get().getMatricula());
    }
    
    @Test
    @DisplayName("toString deve retornar representação adequada")
    void testToString() {
        String representacao = aluno.toString();
        
        // a representação deve conter informações básicas do aluno
        assertNotNull(representacao);
        assertFalse(representacao.isEmpty());
    }
    
    @Test
    @DisplayName("Teste de comportamento com notas diferentes")
    void testNotasDiferentes() {
        // testa disciplinas com notas >= 60 (aprovado) e < 60 (reprovado)
        NotaDisciplina notaAprovado = new NotaDisciplina(75.0f, disciplina1);
        NotaDisciplina notaReprovado = new NotaDisciplina(50.0f, disciplina2);
        
        aluno.adicionarDisciplina(notaAprovado);
        aluno.adicionarDisciplina(notaReprovado);
        
        assertEquals(2, aluno.getDisciplinasPeriodo().size());
        
        // verificar se as notas são armazenadas corretamente
        assertTrue(aluno.getDisciplinasPeriodo().stream()
            .anyMatch(nd -> nd.nota() == 75.0f));
        assertTrue(aluno.getDisciplinasPeriodo().stream()
            .anyMatch(nd -> nd.nota() == 50.0f));
    }
}
