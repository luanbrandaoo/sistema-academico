package ufjf.poo.model;

import ufjf.poo.model.disciplina.Disciplina;
import ufjf.poo.model.disciplina.NotaDisciplina;

import java.util.ArrayList;
import java.util.HashSet;

import ufjf.poo.exception.MatriculaInvalidaException;

public class Aluno {
    private String nome;
    private String matricula;
    private HashSet<NotaDisciplina> disciplinas;
    private ArrayList<HashSet<Disciplina>> planejamento;

    public Aluno(String nome, String matricula) {
        this.nome = nome;
        this.planejamento = new ArrayList<>();
        this.disciplinas = new HashSet<>();
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
    public HashSet<NotaDisciplina> getDisciplinas() {
        return disciplinas;
    }
    public void setDisciplinas(HashSet<NotaDisciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
    public ArrayList<HashSet<Disciplina>> getPlanejamento() {
        return planejamento;
    }
    public void setPlanejamento(ArrayList<HashSet<Disciplina>> planejamento) {
        this.planejamento = planejamento;
    }

    public void adicionarDisciplina(NotaDisciplina disciplina) {
        this.disciplinas.add(disciplina);
    }
    public void removerDisciplina(NotaDisciplina disciplina) {
        this.disciplinas.remove(disciplina);
    }
    public void adicionarPeriodo(HashSet<Disciplina> periodo) {
        this.planejamento.add(periodo);
    }
    public void removerPeriodo(HashSet<Disciplina> periodo) {
        this.planejamento.remove(periodo);
    }

    private void validaMatricula (String matricula) throws MatriculaInvalidaException {
        // matricula == AAAACCIII
        final int maxLengthMatricula = 9;
        final int beginIndexMatricula = 0;
        if(matricula.length() > maxLengthMatricula)
            matricula = matricula.substring(beginIndexMatricula, maxLengthMatricula);

        for(int i = beginIndexMatricula; i <= maxLengthMatricula; i++) {
            if(!Character.isDigit(matricula.charAt(i))) {
                final String matriculaRuim = matricula;
                this.matricula = null;
                throw new MatriculaInvalidaException(
                        "Matricula inválida! Todos os caracteres devem ser números." +
                        "Matricula incorreta: " + matriculaRuim);
            }
        }

        final int matriculaAno = 4;
        final int ano = Integer.parseInt(
                matricula.substring(beginIndexMatricula, matriculaAno+beginIndexMatricula)
        );
        final int anoMin = 1940;
        final int anoMax = 2025;
        if(ano < anoMin || ano > anoMax) {
            final String matriculaRuim = matricula;
            matricula = null;
            throw new MatriculaInvalidaException(
                    "Matricula inválida! Os 4 primeiros caracteres devem ser um ano válido." +
                            "Ano inválido: " + ano);
        }
    }

}
