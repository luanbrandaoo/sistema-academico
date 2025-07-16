package ufjf.poo.exception;

public class MatriculaInvalidaException extends MatriculaException {
    public MatriculaInvalidaException(String message) {
        super("Matrícula inválida: " + message);
    }
}
