package ufjf.poo.controller.validadores;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;

public class ValidadorSimples implements ValidadorPreRequisito {
    private final Disciplina disciplinaRequerida;

    public ValidadorSimples(Disciplina disciplinaRequerida) { this.disciplinaRequerida = disciplinaRequerida; }

    public boolean validar(Aluno aluno) { return aluno.concluiu(disciplinaRequerida); }
}