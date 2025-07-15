package ufjf.poo.model;

import ufjf.poo.model.disciplina.Disciplina;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedList;

public class Turma {
    private final int id;
    private int capacidadeMaxima;
    private int numeroAlunosMatriculados;
    private final Disciplina disciplina;
    private LinkedList<DiaHorario> horarios;

    public static int idTotal = 0;

    public Turma(int capacidadeMaxima, int numeroAlunosMatriculados, Disciplina disciplina, LinkedList<DiaHorario> horarios) {
        this.id = idTotal++;
        this.capacidadeMaxima = capacidadeMaxima;
        this.numeroAlunosMatriculados = numeroAlunosMatriculados;
        this.horarios = horarios;
        this.disciplina = disciplina;
    }

    public Turma(int capacidadeMaxima, int numeroAlunosMatriculados, LinkedList<DiaHorario> horarios) {
        this.id = idTotal++;
        this.capacidadeMaxima = capacidadeMaxima;
        this.numeroAlunosMatriculados = numeroAlunosMatriculados;
        this.horarios = horarios;
        this.disciplina = null;
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
    public LinkedList<DiaHorario> getHorarios() {
        return horarios;
    }

    public String getHorario() {
        if (horarios == null || horarios.isEmpty()) {
            return "";
        }
        return horarios.toString();
    }
    public void setHorarios(LinkedList<DiaHorario> horarios) {
        this.horarios = horarios;
    }

    public boolean temVagasDisponiveis() {
        return numeroAlunosMatriculados < capacidadeMaxima;
    }

    public boolean temVaga() {
        return temVagasDisponiveis();
    }

    public int getVagasRestantes() {
        return Math.max(0, capacidadeMaxima - numeroAlunosMatriculados);
    }

    public boolean adicionarAluno() {
        if (temVagasDisponiveis()) {
            numeroAlunosMatriculados++;
            return true;
        }
        return false;
    }

    public void matricularAluno(String matriculaAluno) throws ufjf.poo.exception.TurmaCheiaException {
        if (!temVagasDisponiveis()) {
            throw new ufjf.poo.exception.TurmaCheiaException("Turma " + id + " está cheia");
        }
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

    public Disciplina getDisciplina() {
        return disciplina; 
    }

    @Override
    public String toString() {
        return "Turma{" + "id=" + id + ", capacidadeMaxima=" + capacidadeMaxima +
                ", numeroAlunosMatriculados=" + numeroAlunosMatriculados +
                ", horario=" + horarios + '}';
    }
    public LinkedList<DiaHorario> formataDiaHorario(LinkedList<DiaHorario> horarios, DayOfWeek dia, int hora) {
        if (horarios == null)
            horarios = new LinkedList<>();
        final int minutos = 0;
        DiaHorario novoHorario = new DiaHorario(dia, LocalTime.of(hora,minutos));
        horarios.add(novoHorario);
        return horarios;
    }
}
