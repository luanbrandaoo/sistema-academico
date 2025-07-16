package ufjf.poo.model.disciplina;

import ufjf.poo.model.Aluno;
import ufjf.poo.controller.validadores.ValidadorPreRequisito;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

public abstract class Disciplina {
    protected String codigo;
    protected String nome;
    protected int cargaHoraria;
    protected List<List<Disciplina>> preRequisitos;
    protected List<Disciplina> coRequisitos;
    protected final List<ValidadorPreRequisito> validadores;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.preRequisitos = new ArrayList<>();
        this.coRequisitos = new ArrayList<>();
        this.validadores = new ArrayList<>();
    }

    public abstract String getCodigo();
    public abstract String getNome();
    public abstract int getCargaHoraria();
    public abstract List<List<Disciplina>> getPreRequisitos();
    public abstract List<Disciplina> getCoRequisitos();

    public abstract void setCodigo(String codigo);
    public abstract void setNome(String nome);
    public abstract void setCargaHoraria(int cargaHoraria);
    public abstract void setPreRequisitos(List<List<Disciplina>> preRequisitos);
    public abstract void setCoRequisitos(List<Disciplina> coRequisitos);

    public abstract void addPreRequisito(Disciplina disciplina);
    public abstract void addPreRequisito(List<Disciplina> disciplinas);
    public abstract void addCoRequisito(Disciplina disciplina);

    public abstract void removePreRequisito(Disciplina disciplina);
    public abstract void removeCoRequisito(Disciplina disciplina);

    public abstract int getCreditos();
    public abstract tipoDisciplina getTipo();

    public boolean validarPreRequisitos(Aluno aluno) {
        return validadores.stream().allMatch(validador -> validador.validar(aluno));
    }

    public void adicionarValidador(ValidadorPreRequisito validador) {
        this.validadores.add(validador);
    }

    public void removerValidador(ValidadorPreRequisito validador) {
        this.validadores.remove(validador);
    }

    public List<ValidadorPreRequisito> getValidadores() {
        return validadores;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Disciplina that = (Disciplina) obj;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return codigo != null ? codigo.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Disciplina{codigo='%s', nome='%s', cargaHoraria=%d, tipo=%s}",
                           codigo, nome, cargaHoraria, getTipo());
    }

}
