package ufjf.poo.exception;

public class TurmaCheiaException extends GerenciamentoVagasException {
  public TurmaCheiaException(String mensagem) {
    super("Turma cheia: " + mensagem);
  }
}
