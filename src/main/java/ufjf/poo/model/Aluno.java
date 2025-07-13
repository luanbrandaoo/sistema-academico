package ufjf.poo.model;

import ufjf.poo.model.disciplina.Disciplina;
import ufjf.poo.model.disciplina.NotaDisciplina;

import java.util.ArrayList;
import java.util.HashSet;

public class Aluno {
    private String nome;
    private String matricula;
    private HashSet<NotaDisciplina> disciplinas;
    private ArrayList<HashSet<Disciplina>> planejamento;

    public Aluno(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
        this.disciplinas = new HashSet<>();
        this.planejamento = new ArrayList<>();
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
    public void setMatricula(String matricula) {
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


}
