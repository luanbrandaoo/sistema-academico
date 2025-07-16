package ufjf.poo.controller;

import java.util.ArrayList;
import java.util.List;

public class RelatorioMatricula {
    private final List<ResultadoMatricula> resultados;
    private final int totalAceitas;
    private final int totalRejeitadas;
    private final int cargaHorariaTotalAceita;

    public RelatorioMatricula(List<ResultadoMatricula> resultados) {
        this.resultados = new ArrayList<>(resultados);
        this.totalAceitas = (int) resultados.stream().filter(ResultadoMatricula::aceita).count();
        this.totalRejeitadas = resultados.size() - totalAceitas;
        this.cargaHorariaTotalAceita = resultados.stream()
                .filter(ResultadoMatricula::aceita)
                .mapToInt(r -> r.disciplina().getCargaHoraria())
                .sum();
    }

    public List<ResultadoMatricula> getResultados() { return resultados; }
    public int getTotalAceitas() { return totalAceitas; }
    public int getTotalRejeitadas() { return totalRejeitadas; }
    public int getCargaHorariaTotalAceita() { return cargaHorariaTotalAceita; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE MATRÍCULA ===\n\n");

        for (ResultadoMatricula resultado : resultados) {
            sb.append("Disciplina: ").append(resultado.disciplina().getCodigo())
                    .append(" - ").append(resultado.disciplina().getNome()).append("\n");
            sb.append("Turma: ").append(resultado.turma().getId()).append("\n");
            sb.append("Tipo: ").append(resultado.disciplina().getTipo()).append("\n");
            sb.append("Carga Horária: ").append(resultado.disciplina().getCargaHoraria()).append("h\n");
            sb.append("Status: ").append(resultado.aceita() ? "ACEITA" : "REJEITADA").append("\n");
            sb.append("Motivo: ").append(resultado.motivo()).append("\n");
            sb.append("---\n");
        }

        sb.append("\nRESUMO:\n");
        sb.append("Matrículas aceitas: ").append(totalAceitas).append("\n");
        sb.append("Matrículas rejeitadas: ").append(totalRejeitadas).append("\n");
        sb.append("Carga horária total aceita: ").append(cargaHorariaTotalAceita).append("h\n");

        return sb.toString();
    }
}
