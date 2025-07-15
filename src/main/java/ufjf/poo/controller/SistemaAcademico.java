package ufjf.poo.controller;

import ufjf.poo.exception.*;
import ufjf.poo.model.Aluno;
import ufjf.poo.model.DiaHorario;
import ufjf.poo.model.Turma;
import ufjf.poo.model.disciplina.Disciplina;
import ufjf.poo.model.disciplina.NotaDisciplina;
import ufjf.poo.model.disciplina.tipoDisciplina;

import java.util.*;
import java.util.stream.Collectors;

public class SistemaAcademico {
    private final Map<String, Disciplina> disciplinas;
    private final Map<Integer, Turma> turmas;
    private final Map<String, Aluno> alunos;
    private final Map<Turma, Disciplina> turmaDisciplina;

    public SistemaAcademico() {
        this.turmaDisciplina = new HashMap<>();
        this.disciplinas = new HashMap<>();
        this.turmas = new HashMap<>();
        this.alunos = new HashMap<>();
    }

    public RelatorioMatricula planejamentoMatricula(String matriculaAluno,
                                                          List<Integer> idsTurmasDesejadas) {
        Aluno aluno = alunos.get(matriculaAluno);
        if (aluno == null)
            throw new IllegalArgumentException("Aluno não encontrado");

        List<ResultadoMatricula> resultados = new ArrayList<>();
        List<Turma> turmasValidadas = new ArrayList<>();
        int cargaHorariaAcumulada = 0;
        final int cargaHorariaMaximaPadrao = 22;

        List<Turma> turmasOrdenadas = obterTurmasOrdenadas(idsTurmasDesejadas);

        for (Turma turma : turmasOrdenadas) {
            Disciplina disciplina = turmaDisciplina.get(turma);
            if (disciplina == null) {
                resultados.add(new ResultadoMatricula(null, turma, false, "Disciplina não encontrada para a turma"));
                continue;
            }
            try {
                if (jaMatriculadoNaDisciplina(disciplina, turmasValidadas)) {
                    resultados.add(new ResultadoMatricula(disciplina, turma, false,
                            "Já matriculado em outra turma desta disciplina"));
                    continue;
                }
                if (cargaHorariaAcumulada + disciplina.getCargaHoraria() > cargaHorariaMaximaPadrao) {
                    resultados.add(new ResultadoMatricula(disciplina, turma, false,
                            "Carga horária máxima excedida"));
                    continue;
                }
                validarVagasNaTurma(turma);
                validarPreRequisitos(aluno, disciplina);
                validarCoRequisitos(disciplina, idsTurmasDesejadas);
                validarConflitoHorario(turma, turmasValidadas);
                turmasValidadas.add(turma);
                cargaHorariaAcumulada += disciplina.getCargaHoraria();
                resultados.add(new ResultadoMatricula(disciplina, turma, true,
                        "Matrícula realizada com sucesso"));

            } catch (MatriculaException e) {
                resultados.add(new ResultadoMatricula(disciplina, turma, false, e.getMessage()));
            }
        }
        efetuarMatriculas(aluno, turmasValidadas);
        return new RelatorioMatricula(resultados);
    }

    private List<Turma> obterTurmasOrdenadas(List<Integer> idsTurmasDesejadas) {
        return idsTurmasDesejadas.stream()
                .map(turmas::get)
                .filter(Objects::nonNull)
                .sorted(this::compararPrecedencia)
                .collect(Collectors.toList());
    }

    private int compararPrecedencia(Turma t1, Turma t2) {
        Disciplina d1 = turmaDisciplina.get(t1);
        Disciplina d2 = turmaDisciplina.get(t2);

        if (d1 == null || d2 == null) return 0;

        int prec1 = obterPrecedencia(d1.getTipo());
        int prec2 = obterPrecedencia(d2.getTipo());

        return Integer.compare(prec1, prec2);
    }

    private int obterPrecedencia(tipoDisciplina tipo) {
        return switch (tipo) {
            case OBRIGATORIA -> 1;
            case ELETIVA -> 2;
            case OPTATIVA -> 3;
        };
    }

    private boolean jaMatriculadoNaDisciplina(Disciplina disciplina, List<Turma> turmasValidadas) {
        return turmasValidadas.stream()
                .anyMatch(turma -> turmaDisciplina.get(turma).equals(disciplina));
    }

    private void validarVagasNaTurma(Turma turma) throws TurmaCheiaException {
        if (turma.getNumeroAlunosMatriculados() >= turma.getCapacidadeMaxima())
            throw new TurmaCheiaException("Turma " + turma.getId() + " não possui vagas");
    }

    private void validarPreRequisitos(Aluno aluno, Disciplina disciplina) throws PreRequisitoNaoCumpridoException {
        if (disciplina.getPreRequisitos() == null || disciplina.getPreRequisitos().isEmpty())
            return;

        for (List<Disciplina> preRequisito : disciplina.getPreRequisitos()) { //pre requisitos
            boolean concluiu = false;
            for(Disciplina preRequisitoAux : preRequisito) { // equivalencias dos requisitos
                concluiu |= aluno.concluiu(preRequisitoAux);
            }
            if (!concluiu) {
                throw new PreRequisitoNaoCumpridoException(
                        "Pré-requisito não cumprido: " + preRequisito.getFirst().getCodigo() +
                                " - " + preRequisito.getFirst().getNome());
            }
        }
    }

    private void validarCoRequisitos(Disciplina disciplina, List<Integer> idsTurmasDesejadas)
            throws CoRequisitoNaoAtendidoException {
        if (disciplina.getCoRequisitos() == null || disciplina.getCoRequisitos().isEmpty())
            return;

        Set<String> disciplinasDesejadas = idsTurmasDesejadas.stream()
                .map(turmas::get)
                .filter(Objects::nonNull)
                .map(turmaDisciplina::get)
                .filter(Objects::nonNull)
                .map(Disciplina::getCodigo)
                .collect(Collectors.toSet());

        for (Disciplina coRequisito : disciplina.getCoRequisitos()) {
            if (!disciplinasDesejadas.contains(coRequisito.getCodigo())) {
                throw new CoRequisitoNaoAtendidoException(
                        "Co-requisito não selecionado: " + coRequisito.getCodigo() + " - " + coRequisito.getNome());
            }
        }
    }

    private void validarConflitoHorario(Turma novaTurma, List<Turma> turmasAceitas)
            throws ConflitoDeHorarioException {
        for (Turma turmaExistente : turmasAceitas) {
            if (temConflitoHorario(novaTurma, turmaExistente)) {
                Disciplina disciplinaNova = turmaDisciplina.get(novaTurma);
                Disciplina disciplinaExistente = turmaDisciplina.get(turmaExistente);

                int precedenciaNova = obterPrecedencia(disciplinaNova.getTipo());
                int precedenciaExistente = obterPrecedencia(disciplinaExistente.getTipo());

                if (precedenciaNova > precedenciaExistente) {
                    throw new ConflitoDeHorarioException(
                            "Conflito de horário com disciplina de maior precedência: " +
                                    disciplinaExistente.getCodigo());
                } else if (precedenciaNova == precedenciaExistente) {
                    throw new ConflitoDeHorarioException(
                            "Conflito de horário entre disciplinas de mesma precedência: " +
                                    disciplinaExistente.getCodigo());
                }
                turmasAceitas.remove(turmaExistente);
                break;
            }
        }
    }

    private boolean temConflitoHorario(Turma turma1, Turma turma2) {
        Set<DiaHorario> horarios1Set = new HashSet<>(turma1.getHorarios());
        return turma2.getHorarios().stream().anyMatch(horarios1Set::contains);
    }

    private void efetuarMatriculas(Aluno aluno, List<Turma> turmasAceitas) {
        for (Turma turma : turmasAceitas) {
            turma.setNumeroAlunosMatriculados(turma.getNumeroAlunosMatriculados() + 1);
            Disciplina disciplina = turmaDisciplina.get(turma);
            if (disciplina != null) {
                NotaDisciplina notaDisciplina = new NotaDisciplina(0.0f, disciplina);
                aluno.adicionarDisciplina(notaDisciplina);
            }
        }
    }

    public void concluirPeriodo(String matriculaAluno, Map<String, Float> notasDisciplinas) {
        Aluno aluno = alunos.get(matriculaAluno);
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não encontrado: " + matriculaAluno);
        }

        Set<NotaDisciplina> disciplinasPeriodo = new HashSet<>(aluno.getDisciplinasPeriodo());

        for (NotaDisciplina notaDisciplina : disciplinasPeriodo) {
            String codigoDisciplina = notaDisciplina.disciplina().getCodigo();
            Float nota = notasDisciplinas.get(codigoDisciplina);

            if (nota != null) {
                NotaDisciplina disciplinaConcluida = new NotaDisciplina(nota, notaDisciplina.disciplina());
                aluno.adicionarConcluida(disciplinaConcluida);
            }
        }
        aluno.getDisciplinasPeriodo().clear();
    }

    public List<Turma> buscarTurmasPorDisciplina(String codigoDisciplina) {
        return turmaDisciplina.entrySet().stream()
                .filter(entry -> entry.getValue().getCodigo().equals(codigoDisciplina))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void addTurma(Turma turma) {
        turmas.put(turma.getId(), turma);
        turmaDisciplina.put(turma, turma.getDisciplina());
    }
    public void addAluno(Aluno aluno) { alunos.put(aluno.getMatricula(), aluno); }
    public void addDisciplina(Disciplina disciplina) { disciplinas.put(disciplina.getCodigo(), disciplina); }
    public Disciplina buscarDisciplina(String codigo) { return disciplinas.get(codigo); }
    public Turma buscarTurma(int id) { return turmas.get(id); }
    public Aluno buscarAluno(String matricula) { return alunos.get(matricula); }
    public Map<String, Disciplina> getDisciplinas() { return new HashMap<>(disciplinas); }
    public Map<Integer, Turma> getTurmas() { return new HashMap<>(turmas); }
    public Map<String, Aluno> getAlunos() { return new HashMap<>(alunos); }
    public Map<Turma, Disciplina> getTurmaDisciplina() { return new HashMap<>(turmaDisciplina); }
}