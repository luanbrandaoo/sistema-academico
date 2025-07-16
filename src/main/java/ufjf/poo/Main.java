package ufjf.poo;

import ufjf.poo.controller.RelatorioMatricula;
import ufjf.poo.controller.ResultadoMatricula;
import ufjf.poo.controller.SistemaAcademico;
import ufjf.poo.controller.validadores.ValidadorSimples;
import ufjf.poo.model.Aluno;
import ufjf.poo.model.DiaHorario;
import ufjf.poo.model.Turma;
import ufjf.poo.model.disciplina.DisciplinaEletiva;
import ufjf.poo.model.disciplina.DisciplinaObrigatoria;
import ufjf.poo.model.disciplina.DisciplinaOptativa;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Sistema Acadêmico de Planejamento ===");
        System.out.println("Sistema inicializado com sucesso!");
        System.out.println();

        // criando uma instância do sistema para demonstração
        SistemaAcademico sistema = new SistemaAcademico();
        executarTestesDemo(sistema);
    }

    private static void executarTestesDemo(SistemaAcademico sistema) {
        System.out.println("Executando testes demonstrativos...");
        System.out.println("=======================================");

        try {
            // configurar disciplinas
            DisciplinaObrigatoria calcI = new DisciplinaObrigatoria("CALC001", "Cálculo I", 4);
            DisciplinaEletiva progI = new DisciplinaEletiva("PROG001", "Programação I", 6);
            DisciplinaOptativa filosofia = new DisciplinaOptativa("FIL001", "Filosofia", 2);

            sistema.addDisciplina(calcI);
            sistema.addDisciplina(progI);
            sistema.addDisciplina(filosofia);

            // configurar aluno
            Aluno aluno = new Aluno("João Silva", "202501001");
            aluno.setCargaHorariaMaxima(24);
            sistema.addAluno(aluno);

            // configurar horários
            DiaHorario horario1 = new DiaHorario(DayOfWeek.MONDAY, LocalTime.of(8, 0));
            DiaHorario horario2 = new DiaHorario(DayOfWeek.TUESDAY, LocalTime.of(10, 0));
            DiaHorario horario3 = new DiaHorario(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0));

            // configurar turmas
            Turma turma2 = new Turma(25, 0, progI, Collections.singletonList(horario2));
            Turma turma1 = new Turma(30, 0, calcI, Collections.singletonList(horario1));
            Turma turma3 = new Turma(20, 0, filosofia, Collections.singletonList(horario3));

            sistema.addTurma(turma1);
            sistema.addTurma(turma2);
            sistema.addTurma(turma3);

            System.out.println("Disciplinas cadastradas:");
            System.out.println("- " + calcI.getCodigo() + " - " + calcI.getNome() + " (" + calcI.getCargaHoraria() + "h) - " + calcI.getTipo());
            System.out.println("- " + progI.getCodigo() + " - " + progI.getNome() + " (" + progI.getCargaHoraria() + "h) - " + progI.getTipo());
            System.out.println("- " + filosofia.getCodigo() + " - " + filosofia.getNome() + " (" + filosofia.getCargaHoraria() + "h) - " + filosofia.getTipo());
            System.out.println();

            System.out.println("Aluno: " + aluno.getNome() + " (Matrícula: " + aluno.getMatricula() + ")");
            System.out.println("Carga horária máxima: " + aluno.getCargaHorariaMaxima() + "h");
            System.out.println();

            // teste 1: Matrícula simples
            System.out.println("TESTE 1: Matrícula simples em todas as disciplinas");
            System.out.println("---------------------------------------------------");

            List<Integer> idsDesejados = Arrays.asList(0, 1, 2);
            RelatorioMatricula relatorio = sistema.planejamentoMatricula("202501001", idsDesejados);
            List<ResultadoMatricula> resultados = relatorio.getResultados();

            for (ResultadoMatricula resultado : resultados) {
                String status = resultado.isAceita() ? "ACEITA" : "REJEITADA";
                System.out.println(status + " - " + resultado.getCodigoDisciplina() +
                        " (Turma " + resultado.getIdTurma() + "): " + resultado.getMotivo());
            }
            System.out.println();

            // teste 2: Excesso de carga horária
            System.out.println("TESTE 2: Testando limite de carga horária (8h máximo)");
            System.out.println("--------------------------------------------------------");

            aluno.setCargaHorariaMaxima(8);
            relatorio = sistema.planejamentoMatricula("202501001", idsDesejados);
            resultados = relatorio.getResultados();

            for (ResultadoMatricula resultado : resultados) {
                String status = resultado.isAceita() ? "ACEITA" : "REJEITADA";
                System.out.println(status + " - " + resultado.getCodigoDisciplina() +
                        " (Turma " + resultado.getIdTurma() + "): " + resultado.getMotivo());
            }
            System.out.println();

            // teste 3: Pré-requisitos
            System.out.println("TESTE 3: Testando pré-requisitos (PROG001 requer CALC001)");
            System.out.println("------------------------------------------------------------");

            aluno.setCargaHorariaMaxima(24); // restaurar limite
            progI.adicionarValidador(new ValidadorSimples(calcI));

            List<Integer> apenasProgI = List.of(1); // apenas PROG001
            relatorio = sistema.planejamentoMatricula("202501001", apenasProgI);
            resultados = relatorio.getResultados();

            for (ResultadoMatricula resultado : resultados) {
                String status = resultado.isAceita() ? "ACEITA" : "REJEITADA";
                System.out.println(status + " - " + resultado.getCodigoDisciplina() +
                        " (Turma " + resultado.getIdTurma() + "): " + resultado.getMotivo());
            }
            System.out.println();

            // teste 4: Relatório final
            System.out.println("TESTE 4: Geração de relatório completo");
            System.out.println("------------------------------------------");

            var relatorioCompleto = sistema.gerarRelatorio(resultados);
            System.out.println(relatorioCompleto);

            System.out.println("Todos os testes demonstrativos executados com sucesso!");
            System.out.println();

        } catch (Exception e) {
            System.err.println("Erro durante a execução dos testes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
