package ufjf.poo.model;

import ufjf.poo.exception.TurmaCheiaException;
import ufjf.poo.model.disciplina.Disciplina;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Turma {
    private final int id;
    private int capacidadeMaxima;
    private int numeroAlunosMatriculados;
    private final Disciplina disciplina;
    private List<DiaHorario> horarios;
    private List<String> alunosMatriculados;

    static int idTotal = 0;

    public Turma(int capacidadeMaxima, int numeroAlunosMatriculados, Disciplina disciplina,
                 List<String> alunosMatriculados, List<DiaHorario> horarios) {
        this.id = idTotal++;
        this.capacidadeMaxima = capacidadeMaxima;
        this.numeroAlunosMatriculados = numeroAlunosMatriculados;
        this.horarios = horarios;
        this.disciplina = disciplina;
        this.alunosMatriculados = alunosMatriculados;
    }

    public Turma(int capacidadeMaxima, int numeroAlunosMatriculados, List<DiaHorario> horarios) {
        this.id = idTotal++;
        this.capacidadeMaxima = capacidadeMaxima;
        this.numeroAlunosMatriculados = numeroAlunosMatriculados;
        this.horarios = horarios;
        this.disciplina = null;
        this.alunosMatriculados = new LinkedList<>();
    }

    public int getId() {
        return id;
    }
    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }
    public void setCapacidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }
    public int getNumeroAlunosMatriculados() {
        return numeroAlunosMatriculados;
    }
    public void setNumeroAlunosMatriculados(int numeroAlunosMatriculados) {
        this.numeroAlunosMatriculados = numeroAlunosMatriculados;
    }
    public List<DiaHorario> getHorarios() {
        return new ArrayList<>(horarios);
    }
    public void setHorarios(List<DiaHorario> horarios) {
        this.horarios = horarios;
    }

    public boolean temVagasDisponiveis() {
        return numeroAlunosMatriculados < capacidadeMaxima;
    }

    public int getVagasRestantes() {
        return Math.max(0, capacidadeMaxima - numeroAlunosMatriculados);
    }
    public List<String> getAlunosMatriculados() {
        return new ArrayList<>(alunosMatriculados);
    }
    public void setAlunosMatriculados(List<String> alunosMatriculados) {
        this.alunosMatriculados = alunosMatriculados;
    }

    public void matriculaAluno(String alunoMatricula) throws TurmaCheiaException {
        if (!temVagasDisponiveis()) {
            throw new TurmaCheiaException("Turma " + id + " está cheia");
        }
        alunosMatriculados.add(alunoMatricula);
        numeroAlunosMatriculados++;
    }

    public boolean removerAluno() {
        if (numeroAlunosMatriculados > 0) {
            numeroAlunosMatriculados--;
            return true;
        }
        return false;
    }

    public boolean temConflitoDeHorario(Turma outraTurma) {
        return this.horarios.stream()
            .anyMatch(horario -> outraTurma.getHorarios().contains(horario));
    }

    public Disciplina getDisciplina() {return disciplina; }

    @Override
    public String toString() {
        return "Turma{" + "id=" + id + ", capacidadeMaxima=" + capacidadeMaxima +
                ", numeroAlunosMatriculados=" + numeroAlunosMatriculados +
                ", horario=" + horarios + '}';
    }
    public List<DiaHorario> formataDiaHorario(List<DiaHorario> horarios, DayOfWeek dia, int hora) {
        if (horarios == null)
            horarios = new LinkedList<>();
        final int minutos = 0;
        DiaHorario novoHorario = new DiaHorario(dia, LocalTime.of(hora,minutos));
        horarios.add(novoHorario);
        return horarios;
    }
}
