package ufjf.poo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ufjf.poo.exception.MatriculaInvalidaException;
import ufjf.poo.model.Aluno;
import ufjf.poo.model.DiaHorario;
import ufjf.poo.model.Turma;
import ufjf.poo.model.disciplina.DisciplinaObrigatoria;
import ufjf.poo.model.disciplina.DisciplinaEletiva;
import ufjf.poo.model.disciplina.DisciplinaOptativa;
import ufjf.poo.model.disciplina.NotaDisciplina;
import ufjf.poo.controller.validadores.ValidadorSimples;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SistemaAcademicoTest {

    private SistemaAcademico sistema;
    private Aluno aluno;
    private DisciplinaObrigatoria calcI;
    private DisciplinaEletiva progI;
    private DisciplinaOptativa filosofia;
    private Turma turma1, turma2, turma3;

    @BeforeEach
    void setUp() throws MatriculaInvalidaException {
        // reset do contador estático para testes isolados
        Turma.idTotal = 0;
        
        sistema = new SistemaAcademico();
        
        // configurar aluno
        aluno = new Aluno("João Silva", "202501001");
        aluno.setCargaHorariaMaxima(24);
        sistema.addAluno(aluno);
        
        // configurar disciplinas
        calcI = new DisciplinaObrigatoria("CALC001", "Cálculo I", 4);
        progI = new DisciplinaEletiva("PROG001", "Programação I", 6);
        filosofia = new DisciplinaOptativa("FIL001", "Filosofia", 2);
        
        sistema.addDisciplina(calcI);
        sistema.addDisciplina(progI);
        sistema.addDisciplina(filosofia);
        
        // configurar horários
        LinkedList<DiaHorario> horario1 = new LinkedList<>();
        horario1.add(new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0)));
        
        LinkedList<DiaHorario> horario2 = new LinkedList<>();
        horario2.add(new DiaHorario(DayOfWeek.TUESDAY, LocalTime.of(10, 0)));
        
        LinkedList<DiaHorario> horario3 = new LinkedList<>();
        horario3.add(new DiaHorario(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0)));
        
        // configurar turmas
        turma1 = new Turma(30, 0, calcI, horario1);
        turma2 = new Turma(25, 0, progI, horario2);
        turma3 = new Turma(20, 0, filosofia, horario3);
        
        sistema.addTurma(turma1);
        sistema.addTurma(turma2);
        sistema.addTurma(turma3);
    }

    @Test
    void testMatriculaSimples() {
        List<Integer> idsDesejados = Arrays.asList(0, 1, 2);
        RelatorioMatricula relatorio = sistema.planejamentoMatricula("202501001", idsDesejados);
        
        List<ResultadoMatricula> resultados = relatorio.getResultados();
        assertEquals(3, resultados.size());
        
        // deve aceitar todas as disciplinas pois não há conflitos nem pré-requisitos
        for (ResultadoMatricula resultado : resultados) {
            assertTrue(resultado.isAceita(), "Matrícula deve ser aceita: " + resultado.getMotivo());
        }
    }

    @Test
    void testExcedeCargaHoraria() {
        aluno.setCargaHorariaMaxima(8); // apenas 8 horas permitidas
        
        List<Integer> idsDesejados = Arrays.asList(0, 1, 2);
        RelatorioMatricula relatorio = sistema.planejamentoMatricula("202501001", idsDesejados);
        List<ResultadoMatricula> resultados = relatorio.getResultados();
        
        assertEquals(3, resultados.size());
        
        // verifica se algumas disciplinas foram rejeitadas por excesso de carga horária
        long rejeitadas = resultados.stream()
                .filter(r -> !r.isAceita() && r.getMotivo().contains("Carga horária máxima excedida"))
                .count();
        
        assertTrue(rejeitadas > 0, "Algumas disciplinas deveriam ser rejeitadas por excesso de carga horária");
    }

    @Test
    void testTurmaCheia() {
        turma1.setCapacidadeMaxima(0); // turma sem vagas
        
        List<Integer> idsDesejados = Arrays.asList(0);
        RelatorioMatricula relatorio = sistema.planejamentoMatricula("202501001", idsDesejados);
        List<ResultadoMatricula> resultados = relatorio.getResultados();
        
        assertEquals(1, resultados.size());
        assertFalse(resultados.get(0).isAceita());
        assertTrue(resultados.get(0).getMotivo().contains("Turma cheia"));
    }

    @Test
    void testGerarRelatorio() {
        List<Integer> idsDesejados = Arrays.asList(0, 1);
        RelatorioMatricula relatorio = sistema.planejamentoMatricula("202501001", idsDesejados);
        
        String relatorioString = sistema.gerarRelatorio(relatorio.getResultados());
        
        assertNotNull(relatorioString);
        assertTrue(relatorioString.contains("RELATÓRIO DE MATRÍCULA"));
        assertTrue(relatorioString.contains("CALC001"));
        assertTrue(relatorioString.contains("PROG001"));
    }

    @Test
    void testPreRequisitoComValidador() {
        // configurar pré-requisito: progI requer calcI
        progI.adicionarValidador(new ValidadorSimples(calcI));
        
        List<Integer> idsDesejados = Arrays.asList(1); // Apenas PROG001
        RelatorioMatricula relatorio = sistema.planejamentoMatricula("202501001", idsDesejados);
        List<ResultadoMatricula> resultados = relatorio.getResultados();
        
        assertEquals(1, resultados.size());
        assertFalse(resultados.get(0).isAceita());
        assertTrue(resultados.get(0).getMotivo().contains("Pré-requisitos não atendidos"));
    }

    @Test
    void testPreRequisitoAtendido() {
        // configurar pré-requisito: progI requer calcI
        progI.adicionarValidador(new ValidadorSimples(calcI));
        
        // adicionar calcI ao histórico do aluno com nota suficiente
        aluno.adicionarConcluida(new NotaDisciplina(80.0f, calcI));
        
        List<Integer> idsDesejados = Arrays.asList(1); // apenas PROG001
        RelatorioMatricula relatorio = sistema.planejamentoMatricula("202501001", idsDesejados);
        List<ResultadoMatricula> resultados = relatorio.getResultados();
        
        assertEquals(1, resultados.size());
        assertTrue(resultados.get(0).isAceita());
    }
}
