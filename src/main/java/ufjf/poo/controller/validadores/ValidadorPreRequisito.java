package ufjf.poo.controller.validadores;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;

public interface ValidadorPreRequisito {
    boolean validar(Aluno aluno, Disciplina disciplina);
}
