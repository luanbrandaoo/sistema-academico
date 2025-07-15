package ufjf.poo.controller.validadores;

import ufjf.poo.model.Aluno;

public class ValidadorCreditosMinimos implements ValidadorPreRequisito {
    private final int creditosMinimos;

    public ValidadorCreditosMinimos(int creditosMinimos) {
        this.creditosMinimos = creditosMinimos;
    }

    @Override
    public boolean validar(Aluno aluno) {
        int totalCreditos = aluno.getDisciplinasCursadas().entrySet().stream()
                .filter(entry -> entry.getValue() >= 60)
                .mapToInt(entry -> entry.getKey().getCreditos())
                .sum();
        return totalCreditos >= creditosMinimos;
    }
}
