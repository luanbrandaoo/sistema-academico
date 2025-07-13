package ufjf.poo.exception;

public class CoRequisitoNaoAtendidoException extends ValidacaoMatriculaException {
  public CoRequisitoNaoAtendidoException(String mensagem) {
    super("Co-requisito não atendido: " + mensagem);
  }
}
