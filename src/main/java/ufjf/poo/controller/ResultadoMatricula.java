package ufjf.poo.controller;

import ufjf.poo.model.Turma;
import ufjf.poo.model.disciplina.Disciplina;

public record ResultadoMatricula(Disciplina disciplina, Turma turma, boolean aceita, String motivo) {
    
    public boolean isAceita() {
        return aceita;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public String getCodigoDisciplina() {
        return disciplina.getCodigo();
    }
    
    public int getIdTurma() {
        return turma.getId();
    }
}