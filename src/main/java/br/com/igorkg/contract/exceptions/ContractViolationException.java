package br.com.igorkg.contract.exceptions;

/// Base da hierarquia de violações de Projeto por Contrato.
///
/// É uma exceção não verificada (unchecked) porque uma violação de contrato
/// indica um defeito de programação (um acordo quebrado entre quem chama e
/// quem é chamado), e não uma condição de runtime recuperável. A hierarquia é
/// `sealed`: um contrato só pode ser quebrado em um de três momentos bem
/// definidos, então os subtipos permitidos são fechados e conhecidos em
/// tempo de compilação.
///
/// As subclasses não constroem a mensagem sozinhas; elas apenas declaram
/// *qual* cláusula foi violada e delegam a formatação para cá, mantendo o
/// formato da mensagem centralizado em um único lugar.
public sealed abstract class ContractViolationException extends RuntimeException
        permits PreConditionException, PostConditionException, InvariantViolationException {

    private static final String MESSAGE_FORMAT = "[Contract Violation] %s: %s";

    protected ContractViolationException(String violatedClause, String details) {
        super(format(violatedClause, details));
    }

    protected ContractViolationException(String violatedClause, String details, Throwable cause) {
        super(format(violatedClause, details), cause);
    }

    private static String format(String violatedClause, String details) {
        return MESSAGE_FORMAT.formatted(violatedClause, details);
    }
}
