package ufjf.poo.model.disciplina;

import java.util.LinkedList;

public class DisciplinaEletiva extends Disciplina {
    public DisciplinaEletiva(String codigo, String nome, int cargaHoraria) {
        super(codigo, nome, cargaHoraria);
    }

    /**
     * @return Código da disciplina
     */
    @Override
    public String getCodigo() {
        return codigo;
    }

    /**
     * @return Nome da disciplina
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * @return Carga horária da disciplina
     */
    @Override
    public int getCargaHoraria() {
        return cargaHoraria;
    }

    /**
     * @return Lista de pré-requisitos simples da disciplina (sem contar pré-requisitos dos pré-requisitos)
     */
    @Override
    public LinkedList<Disciplina> getPreRequisitos() {
        return preRequisitos;
    }

    /**
     * @return Lista de co-requisitos da disciplina
     */
    @Override
    public LinkedList<Disciplina> getCoRequisitos() {
        return coRequisitos;
    }

    /**
     * @param codigo novo código da disciplina
     */
    @Override
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @param nome novo nome da disciplina
     */
    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param cargaHoraria nova carga horária da disciplina
     */
    @Override
    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    /**
     * @param preRequisitos Lista com os novos pré-requisitos da disciplina
     */
    @Override
    public void setPreRequisitos(LinkedList<Disciplina> preRequisitos) {
        this.preRequisitos = preRequisitos;
    }

    /**
     * @param coRequisitos Lista com os novos co-requisitos da disciplina
     */
    @Override
    public void setCoRequisitos(LinkedList<Disciplina> coRequisitos) {
        this.coRequisitos = coRequisitos;
    }

    /**
     * @param disciplina Disciplina pré-requisito a adicionar
     */
    @Override
    public void addPreRequisito(Disciplina disciplina) {
        this.preRequisitos.add(disciplina);
    }

    /**
     * @param disciplina Disciplina co-requisito a adicionar
     */
    @Override
    public void addCoRequisito(Disciplina disciplina) {
        this.coRequisitos.add(disciplina);
    }

    /**
     * @param disciplina Disciplina para ser removida da lista de pré-requisitos
     */
    @Override
    public void removePreRequisito(Disciplina disciplina) {
        this.preRequisitos.remove(disciplina);
    }

    /**
     * @param disciplina Disciplina para ser removida da lista de co-requisitos
     */
    @Override
    public void removeCoRequisito(Disciplina disciplina) {
        this.coRequisitos.remove(disciplina);
    }

    /**
     * @return Retorna a quantidade de créditos que uma disciplina representa
     */
    @Override
    public int getCreditos() {
        return cargaHoraria/15;
    }

    /**
     * @return Retorna o tipo da disciplina para o aluno
     */
    @Override
    public tipoDisciplina getTipo() {
        return tipoDisciplina.ELETIVA;
    }
}
