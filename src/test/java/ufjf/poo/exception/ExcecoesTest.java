package ufjf.poo.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes das Exceções de Matrícula")
public class ExcecoesTest {
    
    @Test
    @DisplayName("PreRequisitoNaoCumpridoException deve herdar de ValidacaoMatriculaException")
    void testHerancaPreRequisitoException() {
        PreRequisitoNaoCumpridoException exception = 
            new PreRequisitoNaoCumpridoException("teste");
        
        assertTrue(exception instanceof ValidacaoMatriculaException);
        assertTrue(exception instanceof MatriculaException);
        assertTrue(exception.getMessage().contains("Pré-requisito não cumprido"));
    }
    
    @Test
    @DisplayName("CoRequisitoNaoAtendidoException deve herdar de ValidacaoMatriculaException")
    void testHerancaCoRequisitoException() {
        CoRequisitoNaoAtendidoException exception = 
            new CoRequisitoNaoAtendidoException("teste");
        
        assertTrue(exception instanceof ValidacaoMatriculaException);
        assertTrue(exception instanceof MatriculaException);
        assertTrue(exception.getMessage().contains("Co-requisito não atendido"));
    }
    
    @Test
    @DisplayName("CargaHorariaExcedidaException deve herdar de ValidacaoMatriculaException")
    void testHerancaCargaHorariaException() {
        CargaHorariaExcedidaException exception = 
            new CargaHorariaExcedidaException("teste");
        
        assertTrue(exception instanceof ValidacaoMatriculaException);
        assertTrue(exception instanceof MatriculaException);
        assertTrue(exception.getMessage().contains("Carga horária excedida"));
    }
    
    @Test
    @DisplayName("ConflitoDeHorarioException deve herdar de ValidacaoMatriculaException")
    void testHerancaConflitoHorarioException() {
        ConflitoDeHorarioException exception = 
            new ConflitoDeHorarioException("teste");
        
        assertTrue(exception instanceof ValidacaoMatriculaException);
        assertTrue(exception instanceof MatriculaException);
        assertTrue(exception.getMessage().contains("Conflito de horário"));
    }
    
    @Test
    @DisplayName("TurmaCheiaException deve herdar de GerenciamentoVagasException")
    void testHerancaTurmaCheiaException() {
        TurmaCheiaException exception = 
            new TurmaCheiaException("teste");
        
        assertTrue(exception instanceof GerenciamentoVagasException);
        assertTrue(exception instanceof MatriculaException);
        assertTrue(exception.getMessage().contains("Turma cheia"));
    }
    
    @Test
    @DisplayName("Estrutura de herança das exceções está correta")
    void testEstruturaHerancaExcecoes() {
        PreRequisitoNaoCumpridoException preReqException = 
            new PreRequisitoNaoCumpridoException("Teste");
        assertTrue(preReqException instanceof ValidacaoMatriculaException);
        assertTrue(preReqException instanceof MatriculaException);
        
        TurmaCheiaException turmaCheiaException = 
            new TurmaCheiaException("Teste");
        assertTrue(turmaCheiaException instanceof GerenciamentoVagasException);
        assertTrue(turmaCheiaException instanceof MatriculaException);
    }
}
