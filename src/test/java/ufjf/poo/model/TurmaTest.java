package ufjf.poo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedList;

@DisplayName("Testes da Classe Turma")
class TurmaTest {
    
    private Turma turma;
    private LinkedList<DiaHorario> horarios;
    
    @BeforeEach
    void setUp() {
        // Reset do contador estático para testes isolados
        Turma.idTotal = 0;
        
        horarios = new LinkedList<>();
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
        LinkedList<DiaHorario> novosHorarios = new LinkedList<>();
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
        // Turma com 30 vagas e 0 matriculados
        assertTrue(turma.getNumeroAlunosMatriculados() < turma.getCapacidadeMaxima());
        
        // Turma com 30 vagas e 25 matriculados
        turma.setNumeroAlunosMatriculados(25);
        assertTrue(turma.getNumeroAlunosMatriculados() < turma.getCapacidadeMaxima());
        
        // Turma lotada
        turma.setNumeroAlunosMatriculados(30);
        assertFalse(turma.getNumeroAlunosMatriculados() < turma.getCapacidadeMaxima());
        
        // Turma superlotada (cenário excepcional)
        turma.setNumeroAlunosMatriculados(35);
        assertFalse(turma.getNumeroAlunosMatriculados() < turma.getCapacidadeMaxima());
    }
    
    @Test
    @DisplayName("Deve calcular vagas restantes corretamente")
    void testCalcularVagasRestantes() {
        // Turma vazia
        assertEquals(30, turma.getCapacidadeMaxima() - turma.getNumeroAlunosMatriculados());
        
        // Turma parcialmente ocupada
        turma.setNumeroAlunosMatriculados(20);
        assertEquals(10, turma.getCapacidadeMaxima() - turma.getNumeroAlunosMatriculados());
        
        // Turma lotada
        turma.setNumeroAlunosMatriculados(30);
        assertEquals(0, turma.getCapacidadeMaxima() - turma.getNumeroAlunosMatriculados());
    }
    
    @Test
    @DisplayName("Deve gerenciar horários múltiplos")
    void testHorariosMultiplos() {
        LinkedList<DiaHorario> horariosComplexos = new LinkedList<>();
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
        // Turma 1: Segunda 8h-10h, Quarta 8h-10h
        LinkedList<DiaHorario> horarios1 = new LinkedList<>();
        horarios1.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0)));
        horarios1.add(new DiaHorario(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0)));
        Turma turma1 = new Turma(30, 0, horarios1);
        
        // Turma 2: Segunda 8h-10h (conflito), Sexta 10h-12h
        LinkedList<DiaHorario> horarios2 = new LinkedList<>();
        horarios2.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0))); // conflito
        horarios2.add(new DiaHorario(DayOfWeek.FRIDAY, LocalTime.of(10, 0)));
        Turma turma2 = new Turma(25, 0, horarios2);
        
        // Turma 3: Terça 10h-12h, Quinta 14h-16h (sem conflito)
        LinkedList<DiaHorario> horarios3 = new LinkedList<>();
        horarios3.add(new DiaHorario(DayOfWeek.TUESDAY, LocalTime.of(10, 0)));
        horarios3.add(new DiaHorario(DayOfWeek.THURSDAY, LocalTime.of(14, 0)));
        Turma turma3 = new Turma(20, 0, horarios3);
        
        // Verificar conflitos
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
        LinkedList<DiaHorario> horariosVazios = new LinkedList<>();
        Turma turmaSemHorario = new Turma(15, 0, horariosVazios);
        
        assertEquals(0, turmaSemHorario.getHorarios().size());
        assertTrue(turmaSemHorario.getHorarios().isEmpty());
    }
    
    @Test
    @DisplayName("Deve aceitar diferentes horários no mesmo dia")
    void testMesmoDialHorasDiferentes() {
        LinkedList<DiaHorario> horariosMesmoDia = new LinkedList<>();
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
        // Turma com capacidade zero
        Turma turmaCapacidadeZero = new Turma(0, 0, horarios);
        assertEquals(0, turmaCapacidadeZero.getCapacidadeMaxima());
        
        // Turma com capacidade muito alta
        Turma turmaCapacidadeAlta = new Turma(1000, 0, horarios);
        assertEquals(1000, turmaCapacidadeAlta.getCapacidadeMaxima());
        
        // Situação onde matriculados > capacidade (situação excepcional)
        turmaCapacidadeZero.setNumeroAlunosMatriculados(5);
        assertTrue(turmaCapacidadeZero.getNumeroAlunosMatriculados() > turmaCapacidadeZero.getCapacidadeMaxima());
    }
}
