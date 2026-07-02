package br.com.igorkg.contract.exceptions;

/// Lançada quando a pós-condição de um método é violada, ou seja, o valor
/// retornado não satisfaz o contrato garantido *após* a execução.
public final class PostConditionException extends ContractViolationException {

    private static final String CLAUSE = "Post-condition";

    public PostConditionException(String details) {
        super(CLAUSE, details);
    }

    public PostConditionException(String details, Throwable cause) {
        super(CLAUSE, details, cause);
    }
}
