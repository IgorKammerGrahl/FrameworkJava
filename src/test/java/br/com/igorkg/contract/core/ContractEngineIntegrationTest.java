package br.com.igorkg.contract.core;

import br.com.igorkg.contract.exceptions.InvariantViolationException;
import br.com.igorkg.contract.exceptions.PostConditionException;
import br.com.igorkg.contract.exceptions.PreConditionException;
import br.com.igorkg.contract.validators.MinValue;
import br.com.igorkg.contract.validators.MinValueValidator;
import br.com.igorkg.contract.validators.NotNull;
import br.com.igorkg.contract.validators.NotNullValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// Testa o ciclo de vida completo do contrato através do proxy dinâmico:
/// pré-condição, pós-condição e invariante, nas fases em que cada uma é
/// verificada por [ContractInvocationHandler].
class ContractEngineIntegrationTest {

    private static Account protect(Account implementation) {
        return ContractEngine.setup()
                .addValidator(NotNull.class, new NotNullValidator())
                .addValidator(MinValue.class, new MinValueValidator())
                .protect(Account.class, implementation);
    }

    @Test
    void validCallPassesThroughTheProxy() {
        Account account = protect(new AccountImpl());

        assertDoesNotThrow(() -> account.deposit(100));
        assertEquals(100, account.getBalance());
    }

    @Test
    void nullArgumentViolatesPreCondition() {
        Account account = protect(new AccountImpl());

        assertThrows(PreConditionException.class, () -> account.deposit(null));
    }

    @Test
    void belowMinimumArgumentViolatesPreCondition() {
        Account account = protect(new AccountImpl());

        PreConditionException violation =
                assertThrows(PreConditionException.class, () -> account.deposit(0));
        assertTrue(violation.getMessage().contains("Pre-condition"));
    }

    @Test
    void implementationNeverRunsWhenPreConditionFails() {
        Account account = protect(new AccountImpl());

        assertThrows(PreConditionException.class, () -> account.deposit(null));
        assertEquals(0, account.getBalance());
    }

    @Test
    void invalidReturnValueViolatesPostCondition() {
        Account account = protect(new BrokenPostConditionAccountImpl());

        assertThrows(PostConditionException.class, account::getBalance);
    }

    @Test
    void corruptedFieldViolatesInvariant() {
        Account account = protect(new BrokenInvariantAccountImpl());

        assertThrows(InvariantViolationException.class, () -> account.deposit(1));
    }
}
