package ufjf.poo;

import ufjf.poo.controller.SistemaAcademico;

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
            var calcI = new ufjf.poo.model.disciplina.DisciplinaObrigatoria("CALC001", "Cálculo I", 4);
            var progI = new ufjf.poo.model.disciplina.DisciplinaEletiva("PROG001", "Programação I", 6);
            var filosofia = new ufjf.poo.model.disciplina.DisciplinaOptativa("FIL001", "Filosofia", 2);

            sistema.addDisciplina(calcI);
            sistema.addDisciplina(progI);
            sistema.addDisciplina(filosofia);

            // configurar aluno
            var aluno = new ufjf.poo.model.Aluno("João Silva", "202501001");
            aluno.setCargaHorariaMaxima(24);
            sistema.addAluno(aluno);

            // configurar horários
            var horario1 = new java.util.LinkedList<ufjf.poo.model.DiaHorario>();
            horario1.add(new ufjf.poo.model.DiaHorario(java.time.DayOfWeek.MONDAY, java.time.LocalTime.of(8, 0)));

            var horario2 = new java.util.LinkedList<ufjf.poo.model.DiaHorario>();
            horario2.add(new ufjf.poo.model.DiaHorario(java.time.DayOfWeek.TUESDAY, java.time.LocalTime.of(10, 0)));

            var horario3 = new java.util.LinkedList<ufjf.poo.model.DiaHorario>();
            horario3.add(new ufjf.poo.model.DiaHorario(java.time.DayOfWeek.WEDNESDAY, java.time.LocalTime.of(14, 0)));

            // configurar turmas
            var turma1 = new ufjf.poo.model.Turma(30, 0, calcI, horario1);
            var turma2 = new ufjf.poo.model.Turma(25, 0, progI, horario2);
            var turma3 = new ufjf.poo.model.Turma(20, 0, filosofia, horario3);

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

            var idsDesejados = java.util.Arrays.asList(0, 1, 2);
            var relatorio = sistema.planejamentoMatricula("202501001", idsDesejados);
            var resultados = relatorio.getResultados();

            for (var resultado : resultados) {
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

            for (var resultado : resultados) {
                String status = resultado.isAceita() ? "ACEITA" : "REJEITADA";
                System.out.println(status + " - " + resultado.getCodigoDisciplina() +
                        " (Turma " + resultado.getIdTurma() + "): " + resultado.getMotivo());
            }
            System.out.println();

            // teste 3: Pré-requisitos
            System.out.println("TESTE 3: Testando pré-requisitos (PROG001 requer CALC001)");
            System.out.println("------------------------------------------------------------");

            aluno.setCargaHorariaMaxima(24); // restaurar limite
            progI.adicionarValidador(new ufjf.poo.controller.validadores.ValidadorSimples(calcI));

            var apenasProgI = List.of(1); // apenas PROG001
            relatorio = sistema.planejamentoMatricula("202501001", apenasProgI);
            resultados = relatorio.getResultados();

            for (var resultado : resultados) {
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
