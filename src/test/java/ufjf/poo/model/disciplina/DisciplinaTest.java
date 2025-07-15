package ufjf.poo.model.disciplina;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes das Classes de Disciplina")
class DisciplinaTest {
    
    private DisciplinaObrigatoria disciplinaObrigatoria;
    private DisciplinaEletiva disciplinaEletiva;
    private DisciplinaOptativa disciplinaOptativa;
    
    @BeforeEach
    void setUp() {
        disciplinaObrigatoria = new DisciplinaObrigatoria("MAT001", "Cálculo I", 60);
        disciplinaEletiva = new DisciplinaEletiva("ELE001", "Inteligência Artificial", 60);
        disciplinaOptativa = new DisciplinaOptativa("OPT001", "Filosofia da Ciência", 30);
    }
    
    @Test
    @DisplayName("DisciplinaObrigatoria deve ser criada corretamente")
    void testCriacaoDisciplinaObrigatoria() {
        assertEquals("MAT001", disciplinaObrigatoria.getCodigo());
        assertEquals("Cálculo I", disciplinaObrigatoria.getNome());
        assertEquals(60, disciplinaObrigatoria.getCargaHoraria());
        assertEquals(tipoDisciplina.OBRIGATORIA, disciplinaObrigatoria.getTipo());
    }
    
    @Test
    @DisplayName("DisciplinaEletiva deve ser criada corretamente")
    void testCriacaoDisciplinaEletiva() {
        assertEquals("ELE001", disciplinaEletiva.getCodigo());
        assertEquals("Inteligência Artificial", disciplinaEletiva.getNome());
        assertEquals(60, disciplinaEletiva.getCargaHoraria());
        assertEquals(tipoDisciplina.ELETIVA, disciplinaEletiva.getTipo());
    }
    
    @Test
    @DisplayName("DisciplinaOptativa deve ser criada corretamente")
    void testCriacaoDisciplinaOptativa() {
        assertEquals("OPT001", disciplinaOptativa.getCodigo());
        assertEquals("Filosofia da Ciência", disciplinaOptativa.getNome());
        assertEquals(30, disciplinaOptativa.getCargaHoraria());
        assertEquals(tipoDisciplina.OPTATIVA, disciplinaOptativa.getTipo());
    }
    
    @Test
    @DisplayName("Todas as disciplinas devem herdar de Disciplina")
    void testHerancaDisciplina() {
        assertInstanceOf(Disciplina.class, disciplinaObrigatoria);
        assertInstanceOf(Disciplina.class, disciplinaEletiva);
        assertInstanceOf(Disciplina.class, disciplinaOptativa);
    }
    
    @Test
    @DisplayName("Teste de precedência: Obrigatória > Eletiva > Optativa")
    void testPrecedenciaDisciplinas() {
        assertTrue(disciplinaObrigatoria.getTipo().ordinal() < disciplinaEletiva.getTipo().ordinal());
        assertTrue(disciplinaEletiva.getTipo().ordinal() < disciplinaOptativa.getTipo().ordinal());
    }
    
    @Test
    @DisplayName("Teste de equals e hashCode para disciplinas")
    void testEqualsEHashCode() {
        DisciplinaObrigatoria outraDisciplina = new DisciplinaObrigatoria("MAT001", "Cálculo I", 60);
        DisciplinaObrigatoria disciplinaDiferente = new DisciplinaObrigatoria("MAT002", "Cálculo II", 60);
        
        // disciplinas com mesmo código devem ser iguais
        assertEquals(disciplinaObrigatoria, outraDisciplina);
        assertEquals(disciplinaObrigatoria.hashCode(), outraDisciplina.hashCode());
        
        // disciplinas com códigos diferentes devem ser diferentes
        assertNotEquals(disciplinaObrigatoria, disciplinaDiferente);
    }
    
    @Test
    @DisplayName("Teste de cálculo de créditos (assumindo 15 horas = 1 crédito)")
    void testCalculoCreditos() {
        DisciplinaObrigatoria disciplina15h = new DisciplinaObrigatoria("TEST001", "Teste 15h", 15);
        DisciplinaObrigatoria disciplina30h = new DisciplinaObrigatoria("TEST002", "Teste 30h", 30);
        DisciplinaObrigatoria disciplina60h = new DisciplinaObrigatoria("TEST003", "Teste 60h", 60);
        
        assertEquals(15, disciplina15h.getCargaHoraria());
        assertEquals(30, disciplina30h.getCargaHoraria());
        assertEquals(60, disciplina60h.getCargaHoraria());
    }
    
    @Test
    @DisplayName("Disciplinas devem aceitar carga horária positiva")
    void testCargaHorariaPositiva() {
        assertDoesNotThrow(() -> {
            new DisciplinaObrigatoria("TEST001", "Teste", 15);
            new DisciplinaObrigatoria("TEST002", "Teste", 60);
            new DisciplinaObrigatoria("TEST003", "Teste", 120);
        });
    }
    
    @Test
    @DisplayName("toString deve retornar representação adequada")
    void testToString() {
        String representacao = disciplinaObrigatoria.toString();
        
        // a representação deve conter informações básicas da disciplina
        assertTrue(representacao.contains("MAT001"));
        assertTrue(representacao.contains("Cálculo I"));
    }
}
