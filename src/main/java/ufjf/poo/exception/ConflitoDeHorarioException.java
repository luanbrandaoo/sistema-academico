package ufjf.poo.exception;

public class ConflitoDeHorarioException extends ValidacaoMatriculaException {
  public ConflitoDeHorarioException(String mensagem) {
    super("Conflito de horário: " + mensagem);
  }
}