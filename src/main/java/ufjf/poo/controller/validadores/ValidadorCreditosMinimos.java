package ufjf.poo.controller.validadores;

import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;

public class ValidadorCreditosMinimos implements ValidadorPreRequisito {
    private final int creditosMinimos;

    public ValidadorCreditosMinimos(int creditosMinimos) {
        this.creditosMinimos = creditosMinimos;
    }

    @Override
    public boolean validar(Aluno aluno, Disciplina disciplina) {
        int totalCreditos = aluno.getDisciplinasCursadas().entrySet().stream()
                .filter(entry -> entry.getValue() >= 60)
                .mapToInt(entry -> entry.getKey().getCargaHorariaSemanal())
                .sum();
        return totalCreditos >= creditosMinimos;
    }
}
