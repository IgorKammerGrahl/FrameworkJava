package br.com.igorkg.contract.exceptions;

/// Lançada quando um invariante do objeto é violado, ou seja, o objeto
/// alcançou um estado inconsistente que o contrato exige que se mantenha
/// válido em todos os momentos observáveis do seu ciclo de vida.
public final class InvariantViolationException extends ContractViolationException {

    private static final String CLAUSE = "Invariant";

    public InvariantViolationException(String details) {
        super(CLAUSE, details);
    }

    public InvariantViolationException(String details, Throwable cause) {
        super(CLAUSE, details, cause);
    }
}
