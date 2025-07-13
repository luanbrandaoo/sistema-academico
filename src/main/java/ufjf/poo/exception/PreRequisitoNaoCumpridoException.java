package ufjf.poo.exception;

public class PreRequisitoNaoCumpridoException extends ValidacaoMatriculaException {
  public PreRequisitoNaoCumpridoException(String mensagem) {
    super("Pré-requisito não cumprido: " + mensagem);
  }
}