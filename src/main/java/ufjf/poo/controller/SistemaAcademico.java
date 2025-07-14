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
                            turma.getId(),
                            false,
                            "Carga horária máxima excedida"
                    ));
                    continue;
                }

                if (!turma.getDisciplina().validarPreRequisitos(aluno)) {
                    resultados.add(new ResultadoMatricula(
                            turma.getDisciplina().getCodigo(),
                            turma.getId(),
                            false,
                            "Pré-requisitos não atendidos"
                    ));
                    continue;
                }

                if (!turma.temVaga()) {
                    resultados.add(new ResultadoMatricula(
                            turma.getDisciplina().getCodigo(),
                            turma.getId(),
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
                                turma.getId(),
                                false,
                                msgConflito
                        ));
                        continue;
                    }
                }

                if (!verificarCoRequisitos(turma, idsTurmasDesejadas)) {
                    resultados.add(new ResultadoMatricula(
                            turma.getDisciplina().getCodigo(),
                            turma.getId(),
                            false,
                            "Co-requisitos não atendidos"
                    ));
                    continue;
                }

                turmasValidadas.add(turma);
                cargaHorariaAcumulada += turma.getDisciplina().getCargaHoraria();

                resultados.add(new ResultadoMatricula(
                        turma.getDisciplina().getCodigo(),
                        turma.getId(),
                        true,
                        "Matrícula realizada com sucesso"
                ));

            } catch (Exception e) {
                resultados.add(new ResultadoMatricula(
                        turma.getDisciplina().getCodigo(),
                        turma.getId(),
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
        List<String> coRequisitos = turma.getDisciplina().getCoRequisitos();
        if (coRequisitos.isEmpty()) {
            return true;
        }

        for (String coRequisito : coRequisitos) {
            boolean encontrado = idsTurmasDesejadas.stream()
                    .anyMatch(idTurma -> {
                        Turma t = turmas.get(idTurma);
                        return t != null && t.getDisciplina().getCodigo().equals(coRequisito);
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
}