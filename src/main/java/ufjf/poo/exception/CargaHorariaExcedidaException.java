package ufjf.poo.exception;

public class CargaHorariaExcedidaException extends ValidacaoMatriculaException {
  public CargaHorariaExcedidaException(String mensagem) {
    super("Carga horária excedida: " + mensagem);
  }
}