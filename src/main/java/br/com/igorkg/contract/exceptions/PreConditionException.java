package br.com.igorkg.contract.exceptions;

/// Lançada quando a pré-condição de um método é violada, ou seja, quem chamou
/// forneceu argumentos que não satisfazem o contrato exigido *antes* da
/// execução.
public final class PreConditionException extends ContractViolationException {

    private static final String CLAUSE = "Pre-condition";

    public PreConditionException(String details) {
        super(CLAUSE, details);
    }

    public PreConditionException(String details, Throwable cause) {
        super(CLAUSE, details, cause);
    }
}
