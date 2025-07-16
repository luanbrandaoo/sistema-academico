package ufjf.poo.exception;

public abstract class GerenciamentoVagasException extends MatriculaException {
  public GerenciamentoVagasException(String mensagem) {
    super("Erro no gerenciamento de vagas: " + mensagem);
  }
}