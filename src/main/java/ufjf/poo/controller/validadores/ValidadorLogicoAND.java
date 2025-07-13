package ufjf.poo.controller.validadores;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;

import java.util.List;

public class ValidadorLogicoAND implements ValidadorPreRequisito {
    private final List<Disciplina> disciplinasRequeridas;

    public ValidadorLogicoAND(List<Disciplina> disciplinasRequeridas) {
        this.disciplinasRequeridas = disciplinasRequeridas;
    }

    @Override
    public boolean validar(Aluno aluno, Disciplina disciplina) {
        return disciplinasRequeridas.stream()
                .allMatch(aluno::concluiu);
    }
}
