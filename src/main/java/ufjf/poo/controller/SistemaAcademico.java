package ufjf.poo.controller;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.Turma;
import ufjf.poo.model.disciplina.Disciplina;

import java.util.*;
import java.util.stream.Collectors;

import ufjf.poo.exception.*;


public class SistemaAcademico {
    private Map<String, Disciplina> disciplinas;
    private Map<String, Turma> turmas;
    private Map<String, Aluno> alunos;

    public SistemaAcademico() {
        this.disciplinas = new HashMap<>();
        this.turmas = new HashMap<>();
        this.alunos = new HashMap<>();
    }

    public void addDisciplina(Disciplina disciplina) {
        disciplinas.put(disciplina.getCodigo(), disciplina);
    }

    public void addTurma(Turma turma) {
        turmas.put(String.valueOf(turma.getId()), turma);
    }

    public void addAluno(Aluno aluno) {
        alunos.put(aluno.getMatricula(), aluno);
    }

    public List<ResultadoMatricula> planejamentoMatricula(String matriculaAluno, List<String> idsTurmasDesejadas) {
        Aluno aluno = alunos.get(matriculaAluno);
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não encontrado");
        }

        List<ResultadoMatricula> resultados = new ArrayList<>();
        List<Turma> turmasValidadas = new ArrayList<>();
        int cargaHorariaAcumulada = 0;


        List<Turma> turmasOrdenadas = idsTurmasDesejadas.stream()
                .map(id -> turmas.get(id))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(t -> t.getDisciplina().getPrecedencia()))
                .collect(Collectors.toList());

        for (Turma turma : turmasOrdenadas) {
            try {
                if (cargaHorariaAcumulada + turma.getDisciplina().getCargaHoraria() > aluno.getCargaHorariaMaxima()) {
                    resultados.add(new ResultadoMatricula(
                            turma.getDisciplina().getCodigo(),
                            String.valueOf(turma.getId()),
                            false,
                            "Carga horária máxima excedida"
                    ));
                    continue;
                }

                if (!turma.getDisciplina().validarPreRequisitos(aluno)) {
                    resultados.add(new ResultadoMatricula(
                            turma.getDisciplina().getCodigo(),
                            String.valueOf(turma.getId()),
                            false,
                            "Pré-requisitos não atendidos"
                    ));
                    continue;
                }

                if (!turma.temVaga()) {
                    resultados.add(new ResultadoMatricula(
                            turma.getDisciplina().getCodigo(),
                            String.valueOf(turma.getId()),
                            false,
                            "Turma cheia"
                    ));
                    continue;
                }

                if (temConflitoHorario(turma, turmasValidadas)) {
                    String msgConflito = resolverConflitoHorario(turma, turmasValidadas);
                    if (msgConflito != null) {
                        resultados.add(new ResultadoMatricula(
                                turma.getDisciplina().getCodigo(),
                                String.valueOf(turma.getId()),
                                false,
                                msgConflito
                        ));
                        continue;
                    }
                }

                if (!verificarCoRequisitos(turma, idsTurmasDesejadas)) {
                    resultados.add(new ResultadoMatricula(
                            turma.getDisciplina().getCodigo(),
                            String.valueOf(turma.getId()),
                            false,
                            "Co-requisitos não atendidos"
                    ));
                    continue;
                }

                turmasValidadas.add(turma);
                cargaHorariaAcumulada += turma.getDisciplina().getCargaHoraria();

                resultados.add(new ResultadoMatricula(
                        turma.getDisciplina().getCodigo(),
                        String.valueOf(turma.getId()),
                        true,
                        "Matrícula realizada com sucesso"
                ));

            } catch (Exception e) {
                resultados.add(new ResultadoMatricula(
                        turma.getDisciplina().getCodigo(),
                        String.valueOf(turma.getId()),
                        false,
                        e.getMessage()
                ));
            }
        }


        for (ResultadoMatricula resultado : resultados) {
            if (resultado.isAceita()) {
                try {
                    turmas.get(resultado.getIdTurma()).matricularAluno(matriculaAluno);
                } catch (TurmaCheiaException e) {
                    // Não deveria acontecer, mas por segurança
                }
            }
        }

        return resultados;
    }

    private boolean temConflitoHorario(Turma novaTurma, List<Turma> turmasValidadas) {
        return turmasValidadas.stream()
                .anyMatch(turma -> turma.getHorario().equals(novaTurma.getHorario()));
    }

    private String resolverConflitoHorario(Turma novaTurma, List<Turma> turmasValidadas) {
        for (Turma turmaExistente : turmasValidadas) {
            if (turmaExistente.getHorario().equals(novaTurma.getHorario())) {
                int precedenciaNova = novaTurma.getDisciplina().getPrecedencia();
                int precedenciaExistente = turmaExistente.getDisciplina().getPrecedencia();

                if (precedenciaNova > precedenciaExistente) {
                    return "Conflito de horário - disciplina com menor precedência";
                } else if (precedenciaNova == precedenciaExistente) {
                    return "Conflito de horário entre disciplinas de mesma precedência";
                }
            }
        }
        return null;
    }

    private boolean verificarCoRequisitos(Turma turma, List<String> idsTurmasDesejadas) {
        LinkedList<Disciplina> coRequisitos = turma.getDisciplina().getCoRequisitos();
        if (coRequisitos == null || coRequisitos.isEmpty()) {
            return true;
        }

        for (Disciplina coRequisito : coRequisitos) {
            boolean encontrado = idsTurmasDesejadas.stream()
                    .anyMatch(idTurma -> {
                        Turma t = turmas.get(idTurma);
                        return t != null && t.getDisciplina().getCodigo().equals(coRequisito.getCodigo());
                    });
            if (!encontrado) {
                return false;
            }
        }
        return true;
    }

    public String gerarRelatorio(List<ResultadoMatricula> resultados) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE MATRÍCULA ===\n\n");

        int aceitas = 0;
        int rejeitadas = 0;

        for (ResultadoMatricula resultado : resultados) {
            sb.append("Disciplina: ").append(resultado.getCodigoDisciplina()).append("\n");
            sb.append("Turma: ").append(resultado.getIdTurma()).append("\n");
            sb.append("Status: ").append(resultado.isAceita() ? "ACEITA" : "REJEITADA").append("\n");
            sb.append("Motivo: ").append(resultado.getMotivo()).append("\n");
            sb.append("---\n");

            if (resultado.isAceita()) {
                aceitas++;
            } else {
                rejeitadas++;
            }
        }

        sb.append("\nRESUMO:\n");
        sb.append("Matrículas aceitas: ").append(aceitas).append("\n");
        sb.append("Matrículas rejeitadas: ").append(rejeitadas).append("\n");

        return sb.toString();
    }

    public Map<String, Disciplina> getDisciplinas() { return disciplinas; }
    public Map<String, Turma> getTurmas() { return turmas; }
    public Map<String, Aluno> getAlunos() { return alunos; }

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
            
            var idsDesejados = java.util.Arrays.asList("0", "1", "2");
            var resultados = sistema.planejamentoMatricula("202501001", idsDesejados);
            
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
            resultados = sistema.planejamentoMatricula("202501001", idsDesejados);
            
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
            
            var apenasProgI = java.util.Arrays.asList("1"); // apenas PROG001
            resultados = sistema.planejamentoMatricula("202501001", apenasProgI);
            
            for (var resultado : resultados) {
                String status = resultado.isAceita() ? "ACEITA" : "REJEITADA";
                System.out.println(status + " - " + resultado.getCodigoDisciplina() + 
                                 " (Turma " + resultado.getIdTurma() + "): " + resultado.getMotivo());
            }
            System.out.println();
            
            // teste 4: Relatório final
            System.out.println("TESTE 4: Geração de relatório completo");
            System.out.println("------------------------------------------");
            
            var relatorio = sistema.gerarRelatorio(resultados);
            System.out.println(relatorio);
            
            System.out.println("Todos os testes demonstrativos executados com sucesso!");
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("Erro durante a execução dos testes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}