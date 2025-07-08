package ufjf.poo.model.disciplina;

import java.util.LinkedList;

public abstract class Disciplina {
    protected String codigo;
    protected String nome;
    protected int cargaHoraria;
    protected LinkedList<Disciplina> preRequisitos;
    protected LinkedList<Disciplina> coRequisitos;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public abstract String getCodigo();
    public abstract String getNome();
    public abstract int getCargaHoraria();
    public abstract LinkedList<Disciplina> getPreRequisitos();
    public abstract LinkedList<Disciplina> getCoRequisitos();

    public abstract void setCodigo(String codigo);
    public abstract void setNome(String nome);
    public abstract void setCargaHoraria(int cargaHoraria);
    public abstract void setPreRequisitos(LinkedList<Disciplina> preRequisitos);
    public abstract void setCoRequisitos(LinkedList<Disciplina> coRequisitos);

    public abstract void addPreRequisito(Disciplina disciplina);
    public abstract void addCoRequisito(Disciplina disciplina);

    public abstract void removePreRequisito(Disciplina disciplina);
    public abstract void removeCoRequisito(Disciplina disciplina);

    public abstract int getCreditos();
    public abstract tipoDisciplina getTipo();
    // equiparação?
}
