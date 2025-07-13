package ufjf.poo.controller.validadores;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;

import java.util.List;

public class ValidadorLogicoOR implements ValidadorPreRequisito {
    private final List<Disciplina> alternativas;

    public ValidadorLogicoOR(List<Disciplina> alternativas) {
        this.alternativas = alternativas;
    }

    @Override
    public boolean validar(Aluno aluno, Disciplina disciplina) {
        return alternativas.stream()
                .anyMatch(aluno::concluiu);
    }
}
