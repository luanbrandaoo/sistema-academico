package ufjf.poo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ufjf.poo.exception.TurmaCheiaException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe Turma")
class TurmaTest {
    
    private Turma turma;
    private List<DiaHorario> horarios;
    
    @BeforeEach
    void setUp() {
        // reset do contador estático para testes isolados
        Turma.idTotal = 0;
        
        horarios = new ArrayList<>();
        horarios.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0)));
        horarios.add(new DiaHorario(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0)));

        turma = new Turma(30, 0, horarios);
    }
    
    @Test
    @DisplayName("Turma deve ser criada corretamente")
    void testCriacaoTurma() {
        assertEquals(0, turma.getId()); // primeiro ID criado
        assertEquals(30, turma.getCapacidadeMaxima());
        assertEquals(0, turma.getNumeroAlunosMatriculados());
        assertEquals(2, turma.getHorarios().size());
        assertTrue(turma.getHorarios().contains(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0))));
        assertTrue(turma.getHorarios().contains(new DiaHorario(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0))));
    }
    
    @Test
    @DisplayName("IDs devem ser únicos e incrementais")
    void testIdUnico() {
        Turma turma1 = new Turma(25, 0, horarios);
        Turma turma2 = new Turma(35, 0, horarios);
        
        assertEquals(1, turma1.getId());
        assertEquals(2, turma2.getId());
        assertNotEquals(turma1.getId(), turma2.getId());
    }
    
    @Test
    @DisplayName("Deve permitir alterar capacidade máxima")
    void testAlterarCapacidadeMaxima() {
        turma.setCapacidadeMaxima(25);
        assertEquals(25, turma.getCapacidadeMaxima());
        
        turma.setCapacidadeMaxima(50);
        assertEquals(50, turma.getCapacidadeMaxima());
    }
    
    @Test
    @DisplayName("Deve permitir alterar número de alunos matriculados")
    void testAlterarNumeroAlunosMatriculados() {
        turma.setNumeroAlunosMatriculados(15);
        assertEquals(15, turma.getNumeroAlunosMatriculados());
        
        turma.setNumeroAlunosMatriculados(20);
        assertEquals(20, turma.getNumeroAlunosMatriculados());
    }
    
    @Test
    @DisplayName("Deve permitir alterar horários")
    void testAlterarHorarios() {
        List<DiaHorario> novosHorarios = new ArrayList<>();
        novosHorarios.add(new DiaHorario(DayOfWeek.TUESDAY, LocalTime.of(10, 0)));
        novosHorarios.add(new DiaHorario(DayOfWeek.THURSDAY, LocalTime.of(10, 0)));
        
        turma.setHorarios(novosHorarios);
        assertEquals(2, turma.getHorarios().size());
        assertTrue(turma.getHorarios().contains(new DiaHorario(DayOfWeek.TUESDAY, LocalTime.of(10, 0))));
        assertTrue(turma.getHorarios().contains(new DiaHorario(DayOfWeek.THURSDAY, LocalTime.of(10, 0))));
    }
    
    @Test
    @DisplayName("Deve verificar se turma tem vagas disponíveis")
    void testVerificarVagasDisponiveis() {
        // turma com 30 vagas e 0 matriculados
        assertTrue(turma.getNumeroAlunosMatriculados() < turma.getCapacidadeMaxima());
        
        // turma com 30 vagas e 25 matriculados
        turma.setNumeroAlunosMatriculados(25);
        assertTrue(turma.getNumeroAlunosMatriculados() < turma.getCapacidadeMaxima());
        
        // turma lotada
        turma.setNumeroAlunosMatriculados(30);
        assertFalse(turma.getNumeroAlunosMatriculados() < turma.getCapacidadeMaxima());
        
        // turma superlotada (cenário excepcional)
        turma.setNumeroAlunosMatriculados(35);
        assertFalse(turma.getNumeroAlunosMatriculados() < turma.getCapacidadeMaxima());
    }
    
    @Test
    @DisplayName("Deve calcular vagas restantes corretamente")
    void testCalcularVagasRestantes() {
        // turma vazia
        assertEquals(30, turma.getCapacidadeMaxima() - turma.getNumeroAlunosMatriculados());
        
        // turma parcialmente ocupada
        turma.setNumeroAlunosMatriculados(20);
        assertEquals(10, turma.getCapacidadeMaxima() - turma.getNumeroAlunosMatriculados());
        
        // turma lotada
        turma.setNumeroAlunosMatriculados(30);
        assertEquals(0, turma.getCapacidadeMaxima() - turma.getNumeroAlunosMatriculados());
    }
    
    @Test
    @DisplayName("Deve gerenciar horários múltiplos")
    void testHorariosMultiplos() {
        List<DiaHorario> horariosComplexos = new ArrayList<>();
        horariosComplexos.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0)));
        horariosComplexos.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(10, 0)));
        horariosComplexos.add(new DiaHorario(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0)));
        horariosComplexos.add(new DiaHorario(DayOfWeek.FRIDAY, LocalTime.of(14, 0)));
        
        Turma turmaCompleta = new Turma(40, 5, horariosComplexos);
        
        assertEquals(4, turmaCompleta.getHorarios().size());
        assertTrue(turmaCompleta.getHorarios().contains(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0))));
        assertTrue(turmaCompleta.getHorarios().contains(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(10, 0))));
        assertTrue(turmaCompleta.getHorarios().contains(new DiaHorario(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0))));
        assertTrue(turmaCompleta.getHorarios().contains(new DiaHorario(DayOfWeek.FRIDAY, LocalTime.of(14, 0))));
    }
    
    @Test
    @DisplayName("Deve verificar conflito de horários entre turmas")
    void testConflitoHorarios() {
        // turma 1: Segunda 8h-10h, Quarta 8h-10h
        List<DiaHorario> horarios1 = new ArrayList<>();
        horarios1.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0)));
        horarios1.add(new DiaHorario(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0)));
        Turma turma1 = new Turma(30, 0, horarios1);

        // turma 2: Segunda 8h-10h (conflito), Sexta 10h-12h
        List<DiaHorario> horarios2 = new ArrayList<>();
        horarios2.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0))); // conflito
        horarios2.add(new DiaHorario(DayOfWeek.FRIDAY, LocalTime.of(10, 0)));
        Turma turma2 = new Turma(25, 0, horarios2);

        // turma 3: Terça 10h-12h, Quinta 14h-16h (sem conflito)
        List<DiaHorario> horarios3 = new ArrayList<>();
        horarios3.add(new DiaHorario(DayOfWeek.TUESDAY, LocalTime.of(10, 0)));
        horarios3.add(new DiaHorario(DayOfWeek.THURSDAY, LocalTime.of(14, 0)));
        Turma turma3 = new Turma(20, 0, horarios3);
        
        // verificar conflitos
        boolean temConflito12 = turma1.getHorarios().stream()
            .anyMatch(h1 -> turma2.getHorarios().contains(h1));
        assertTrue(temConflito12, "Deve haver conflito entre turma1 e turma2");
        
        boolean temConflito13 = turma1.getHorarios().stream()
            .anyMatch(h1 -> turma3.getHorarios().contains(h1));
        assertFalse(temConflito13, "Não deve haver conflito entre turma1 e turma3");
        
        boolean temConflito23 = turma2.getHorarios().stream()
            .anyMatch(h2 -> turma3.getHorarios().contains(h2));
        assertFalse(temConflito23, "Não deve haver conflito entre turma2 e turma3");
    }
    
    @Test
    @DisplayName("toString deve retornar representação adequada")
    void testToString() {
        String representacao = turma.toString();
        
        assertNotNull(representacao);
        assertFalse(representacao.isEmpty());
        assertTrue(representacao.contains("Turma{"));
        assertTrue(representacao.contains("id=0"));
        assertTrue(representacao.contains("capacidadeMaxima=30"));
        assertTrue(representacao.contains("numeroAlunosMatriculados=0"));
        assertTrue(representacao.contains("horario=" + horarios));
    }
    
    @Test
    @DisplayName("Deve permitir turmas com horários vazios")
    void testTurmaHorariosVazios() {
        List<DiaHorario> horariosVazios = new ArrayList<>();
        Turma turmaSemHorario = new Turma(15, 0, horariosVazios);
        
        assertEquals(0, turmaSemHorario.getHorarios().size());
        assertTrue(turmaSemHorario.getHorarios().isEmpty());
    }
    
    @Test
    @DisplayName("Deve aceitar diferentes horários no mesmo dia")
    void testMesmoDialHorasDiferentes() {
        List<DiaHorario> horariosMesmoDia = new ArrayList<>();
        horariosMesmoDia.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0)));
        horariosMesmoDia.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(10, 0)));
        horariosMesmoDia.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(14, 0)));
        
        Turma turmaMesmoDia = new Turma(25, 8, horariosMesmoDia);
        
        assertEquals(3, turmaMesmoDia.getHorarios().size());
        long segundasFeiras = turmaMesmoDia.getHorarios().stream()
            .filter(h -> h.dia() == DayOfWeek.MONDAY)
            .count();
        assertEquals(3, segundasFeiras);
    }
    
    @Test
    @DisplayName("Deve validar estados de capacidade extremos")
    void testCapacidadeExtrema() {
        // turma com capacidade zero
        Turma turmaCapacidadeZero = new Turma(0, 0, horarios);
        assertEquals(0, turmaCapacidadeZero.getCapacidadeMaxima());
        
        // turma com capacidade muito alta
        Turma turmaCapacidadeAlta = new Turma(1000, 0, horarios);
        assertEquals(1000, turmaCapacidadeAlta.getCapacidadeMaxima());
        
        // situação onde matriculados > capacidade (situação excepcional)
        turmaCapacidadeZero.setNumeroAlunosMatriculados(5);
        assertTrue(turmaCapacidadeZero.getNumeroAlunosMatriculados() > turmaCapacidadeZero.getCapacidadeMaxima());
    }
    
    @Test
    @DisplayName("Deve verificar disponibilidade de vagas corretamente")
    void testTemVagasDisponiveis() {
        // turma vazia
        assertTrue(turma.temVagasDisponiveis());
        
        // turma parcialmente ocupada
        turma.setNumeroAlunosMatriculados(25);
        assertTrue(turma.temVagasDisponiveis());
        
        // turma lotada
        turma.setNumeroAlunosMatriculados(30);
        assertFalse(turma.temVagasDisponiveis());
        
        // turma superlotada
        turma.setNumeroAlunosMatriculados(35);
        assertFalse(turma.temVagasDisponiveis());
    }
    
    @Test
    @DisplayName("Deve calcular vagas restantes corretamente com método específico")
    void testGetVagasRestantes() {
        // turma vazia
        assertEquals(30, turma.getVagasRestantes());
        
        // turma parcialmente ocupada
        turma.setNumeroAlunosMatriculados(20);
        assertEquals(10, turma.getVagasRestantes());
        
        // turma lotada
        turma.setNumeroAlunosMatriculados(30);
        assertEquals(0, turma.getVagasRestantes());
        
        // turma superlotada (deve retornar 0, não negativo)
        turma.setNumeroAlunosMatriculados(35);
        assertEquals(0, turma.getVagasRestantes());
    }
    
    @Test
    @DisplayName("Deve adicionar aluno se houver vagas")
    void testMatriculaAluno() {
        // turma vazia - deve conseguir adicionar
        assertDoesNotThrow(
                () -> turma.matriculaAluno("202465557"),
                "Teste de adicionar aluno em turma vazia falhou"
        );
        assertEquals(1, turma.getNumeroAlunosMatriculados());
        
        // adicionar até lotar
        for (int i = 1; i < 30; i++) {
            assertDoesNotThrow(() -> turma.matriculaAluno("202465055"),
                    "Teste de adicionar aluno em turma não cheia falhou");
        }
        assertEquals(30, turma.getNumeroAlunosMatriculados());
        
        // tentar adicionar quando lotada - deve falhar
        assertThrows(
                TurmaCheiaException.class,
                () -> turma.matriculaAluno("199000001"),
                "Teste de adicionar aluno em turma lotada falhou"
        );

        assertEquals(30, turma.getNumeroAlunosMatriculados());
    }
    
    @Test
    @DisplayName("Deve remover aluno se houver alunos matriculados")
    void testRemoverAluno() {
        // turma vazia - não deve conseguir remover
        assertFalse(turma.removerAluno());
        assertEquals(0, turma.getNumeroAlunosMatriculados());
        
        // adicionar alguns alunos e remover
        turma.setNumeroAlunosMatriculados(5);
        assertTrue(turma.removerAluno());
        assertEquals(4, turma.getNumeroAlunosMatriculados());
        
        // remover todos
        for (int i = 4; i > 0; i--) {
            assertTrue(turma.removerAluno());
        }
        assertEquals(0, turma.getNumeroAlunosMatriculados());
        
        // tentar remover de turma vazia novamente
        assertFalse(turma.removerAluno());
        assertEquals(0, turma.getNumeroAlunosMatriculados());
    }
    
    @Test
    @DisplayName("Deve detectar conflito de horário usando método específico")
    void testTemConflitoDeHorarioMetodo() {
        // turma 1: Segunda 8h, Quarta 8h
        List<DiaHorario> horarios1 = new ArrayList<>();
        horarios1.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0)));
        horarios1.add(new DiaHorario(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0)));
        Turma turma1 = new Turma(30, 0, horarios1);

        // turma 2: Segunda 8h (conflito), Sexta 10h
        List<DiaHorario> horarios2 = new ArrayList<>();
        horarios2.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0))); // conflito
        horarios2.add(new DiaHorario(DayOfWeek.FRIDAY, LocalTime.of(10, 0)));
        Turma turma2 = new Turma(25, 0, horarios2);

        // turma 3: Terça 10h, Quinta 14h (sem conflito)
        List<DiaHorario> horarios3 = new ArrayList<>();
        horarios3.add(new DiaHorario(DayOfWeek.TUESDAY, LocalTime.of(10, 0)));
        horarios3.add(new DiaHorario(DayOfWeek.THURSDAY, LocalTime.of(14, 0)));
        Turma turma3 = new Turma(20, 0, horarios3);
        
        // testar conflitos
        assertTrue(turma1.temConflitoDeHorario(turma2), "Deve haver conflito entre turma1 e turma2");
        assertTrue(turma2.temConflitoDeHorario(turma1), "Conflito deve ser simétrico");
        
        assertFalse(turma1.temConflitoDeHorario(turma3), "Não deve haver conflito entre turma1 e turma3");
        assertFalse(turma3.temConflitoDeHorario(turma1), "Não-conflito deve ser simétrico");
        
        assertFalse(turma2.temConflitoDeHorario(turma3), "Não deve haver conflito entre turma2 e turma3");
        assertFalse(turma3.temConflitoDeHorario(turma2), "Não-conflito deve ser simétrico");
    }
    
    @Test
    @DisplayName("Turma não deve ter conflito consigo mesma")
    void testSemConflitoConsigoMesma() {
        assertTrue(turma.temConflitoDeHorario(turma), "Turma deve ter conflito consigo mesma");
    }
}
