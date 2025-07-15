package ufjf.poo.model;

import ufjf.poo.model.disciplina.Disciplina;
import ufjf.poo.model.disciplina.NotaDisciplina;

import java.util.ArrayList;
import java.util.HashSet;

import ufjf.poo.exception.MatriculaInvalidaException;

public class Aluno {
    private String nome;
    private String matricula;
    private HashSet<NotaDisciplina> disciplinasPeriodo;
    private HashSet<NotaDisciplina> disciplinasHistorico;
    private ArrayList<HashSet<Disciplina>> planejamento;
    private int cargaHorariaMaxima;

    public Aluno(String nome, String matricula) {
        this.nome = nome;
        this.planejamento = new ArrayList<>();
        this.disciplinasPeriodo = new HashSet<>();
        this.disciplinasHistorico = new HashSet<>();
        this.cargaHorariaMaxima = 24;
        try { validaMatricula(matricula); }
        catch (MatriculaInvalidaException ignored) {}
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) throws MatriculaInvalidaException {
        validaMatricula(matricula);
        this.matricula = matricula;
    }
    public HashSet<NotaDisciplina> getDisciplinasPeriodo() {
        return disciplinasPeriodo;
    }
    public void setDisciplinasPeriodo(HashSet<NotaDisciplina> disciplinasPeriodo) {
        this.disciplinasPeriodo = disciplinasPeriodo;
    }
    public ArrayList<HashSet<Disciplina>> getPlanejamento() {
        return planejamento;
    }
    public void setPlanejamento(ArrayList<HashSet<Disciplina>> planejamento) {
        this.planejamento = planejamento;
    }

    public void adicionarDisciplina(NotaDisciplina disciplina) {
        this.disciplinasPeriodo.add(disciplina);
    }
    public void removerDisciplina(NotaDisciplina disciplina) {
        this.disciplinasPeriodo.remove(disciplina);
    }
    public void adicionarPeriodo(HashSet<Disciplina> periodo) {
        this.planejamento.add(periodo);
    }
    public void removerPeriodo(HashSet<Disciplina> periodo) {
        this.planejamento.remove(periodo);
    }
    public void adicionarConcluida(NotaDisciplina disciplina) {
        this.disciplinasHistorico.add(disciplina);
    }
    public void removerConcluida(NotaDisciplina disciplina) {
        this.disciplinasHistorico.remove(disciplina);
    }
    public HashSet<NotaDisciplina> getDisciplinasHistorico() {
        return disciplinasHistorico;
    }
    public void setDisciplinasHistorico(HashSet<NotaDisciplina> disciplinasHistorico) {
        this.disciplinasHistorico = disciplinasHistorico;
    }

    public int getCargaHorariaMaxima() {
        return cargaHorariaMaxima;
    }

    public void setCargaHorariaMaxima(int cargaHorariaMaxima) {
        this.cargaHorariaMaxima = cargaHorariaMaxima;
    }

    private void validaMatricula (String matricula) throws MatriculaInvalidaException {
        // matricula == AAAACCIII
        final int maxLengthMatricula = 9;
        final int beginIndexMatricula = 0;
        if(matricula.length() > maxLengthMatricula)
            matricula = matricula.substring(beginIndexMatricula, maxLengthMatricula);

        for(int i = beginIndexMatricula; i < matricula.length(); i++) {
            if(!Character.isDigit(matricula.charAt(i))) {
                this.matricula = null;
                throw new MatriculaInvalidaException(
                        "Matricula inválida! Todos os caracteres devem ser números." +
                        "Matricula incorreta: " + matricula);
            }
        }

        final int matriculaAno = 4;
        final int ano = Integer.parseInt(
                matricula.substring(beginIndexMatricula, matriculaAno+beginIndexMatricula)
        );
        final int anoMin = 1940;
        final int anoMax = 2025;
        if(ano < anoMin || ano > anoMax) {
            throw new MatriculaInvalidaException(
                    "Matricula inválida! Os 4 primeiros caracteres devem ser um ano válido." +
                            "Ano inválido: " + ano);
        }
    }
    
    public boolean concluiu(Disciplina disciplina) {
        // verifica tanto no histórico quanto no período atual
        boolean noHistorico = disciplinasHistorico.stream()
            .anyMatch(nd -> nd.disciplina().equals(disciplina) && nd.nota() >= 60.0f);
        
        boolean noPeriodo = disciplinasPeriodo.stream()
            .anyMatch(nd -> nd.disciplina().equals(disciplina) && nd.nota() >= 60.0f);
            
        return noHistorico || noPeriodo;
    }
    
    public java.util.Map<Disciplina, Float> getDisciplinasCursadas() {
        java.util.Map<Disciplina, Float> todasDisciplinas = new java.util.HashMap<>();
        // adiciona disciplinas do histórico e do período atual
        disciplinasHistorico.stream()
            .forEach(nd -> todasDisciplinas.put(nd.disciplina(), nd.nota()));
        
        disciplinasPeriodo.stream()
            .forEach(nd -> todasDisciplinas.put(nd.disciplina(), nd.nota()));
            
        return todasDisciplinas;
    }
}
