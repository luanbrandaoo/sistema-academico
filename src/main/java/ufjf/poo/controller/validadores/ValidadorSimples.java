package ufjf.poo.controller.validadores;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;

public class ValidadorSimples implements ValidadorPreRequisito {
    private final Disciplina disciplinaRequerida;

    public ValidadorSimples(Disciplina disciplinaRequerida) {
        this.disciplinaRequerida = disciplinaRequerida;
    }

    @Override
    public boolean validar(Aluno aluno, Disciplina disciplina) {
        return aluno.concluiu(disciplinaRequerida);
    }
}