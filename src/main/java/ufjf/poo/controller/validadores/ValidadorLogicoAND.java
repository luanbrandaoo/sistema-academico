package ufjf.poo.controller.validadores;

import ufjf.poo.exception.PreRequisitoNaoCumpridoException;
import ufjf.poo.model.Aluno;
import ufjf.poo.model.disciplina.Disciplina;

import java.util.List;

public class ValidadorLogicoAND implements ValidadorPreRequisito {
    private final List<List<Disciplina>> disciplinasRequeridas;

    public ValidadorLogicoAND(List<List<Disciplina>> disciplinasRequeridas) {
        this.disciplinasRequeridas = disciplinasRequeridas;
    }

    @Override
    public boolean validar(Aluno aluno) {
        return disciplinasRequeridas.stream()
                .allMatch(disciplinas -> disciplinas.stream()   //pre requisitos
                        .anyMatch(disciplina -> {   //equivalencias
                            try {
                                validarPreRequisitos(aluno, disciplina);
                                return true;
                            } catch (PreRequisitoNaoCumpridoException e) {
                                return false;
                            }
                        }));
    }
    private void validarPreRequisitos(Aluno aluno, Disciplina disciplina) throws PreRequisitoNaoCumpridoException {
        if (!disciplina.validarPreRequisitos(aluno)) {
            throw new PreRequisitoNaoCumpridoException("Pré-requisitos não atendidos");
        }

        if (disciplina.getPreRequisitos() == null || disciplina.getPreRequisitos().isEmpty())
            return;

        for (List<Disciplina> preRequisito : disciplina.getPreRequisitos()) { //pre requisitos
            boolean concluiu = false;
            for(Disciplina preRequisitoAux : preRequisito) { // equivalencias dos requisitos
                concluiu |= aluno.concluiu(preRequisitoAux);
            }
            if (!concluiu) {
                throw new PreRequisitoNaoCumpridoException(
                        "Pré-requisito não cumprido: " + preRequisito.getFirst().getCodigo() +
                                " - " + preRequisito.getFirst().getNome());
            }
        }
    }
}
