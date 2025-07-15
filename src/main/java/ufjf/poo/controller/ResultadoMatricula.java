package ufjf.poo.controller;

public class ResultadoMatricula {
    private String codigoDisciplina;
    private String idTurma;
    private boolean aceita;
    private String motivo;

    public ResultadoMatricula(String codigoDisciplina, String idTurma, boolean aceita, String motivo) {
        this.codigoDisciplina = codigoDisciplina;
        this.idTurma = idTurma;
        this.aceita = aceita;
        this.motivo = motivo;
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public String getIdTurma() {
        return idTurma;
    }

    public boolean isAceita() {
        return aceita;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String toString() {
        return String.format("ResultadoMatricula{codigo='%s', turma='%s', aceita=%s, motivo='%s'}", 
                           codigoDisciplina, idTurma, aceita, motivo);
    }
}
