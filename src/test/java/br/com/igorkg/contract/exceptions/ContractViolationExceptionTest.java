package br.com.igorkg.contract.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/// Garante que cada subclasse selada rotula a mensagem com sua própria
/// cláusula, mas segue o mesmo formato centralizado em
/// [ContractViolationException].
class ContractViolationExceptionTest {

    @Test
    void preConditionMessageFollowsTheStandardFormat() {
        PreConditionException violation = new PreConditionException("value must not be null");

        assertEquals("[Contract Violation] Pre-condition: value must not be null", violation.getMessage());
    }

    @Test
    void postConditionMessageFollowsTheStandardFormat() {
        PostConditionException violation = new PostConditionException("value must be >= 0 but was -1");

        assertEquals("[Contract Violation] Post-condition: value must be >= 0 but was -1", violation.getMessage());
    }

    @Test
    void invariantMessageFollowsTheStandardFormat() {
        InvariantViolationException violation =
                new InvariantViolationException("balance must be >= 0 but was -1");

        assertEquals("[Contract Violation] Invariant: balance must be >= 0 but was -1", violation.getMessage());
    }
}
