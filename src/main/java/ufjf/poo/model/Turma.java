package ufjf.poo.model;
/*
ID da turma, capacidade máxima, número atual de alunos matriculados e o horário fixo da aula.
 */

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedList;

public class Turma {
    private final int id;
    private int capacidadeMaxima;
    private int numeroAlunosMatriculados;
    private LinkedList<DiaHorario> horarios;

    static int idTotal = 0;

    public Turma(int capacidadeMaxima, int numeroAlunosMatriculados, LinkedList<DiaHorario> horarios) {
        this.id = idTotal++;
        this.capacidadeMaxima = capacidadeMaxima;
        this.numeroAlunosMatriculados = numeroAlunosMatriculados;
        this.horarios = horarios;
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
    public void setHorarios(LinkedList<DiaHorario> horarios) {
        this.horarios = horarios;
    }
    
    public boolean temVagasDisponiveis() {
        return numeroAlunosMatriculados < capacidadeMaxima;
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
