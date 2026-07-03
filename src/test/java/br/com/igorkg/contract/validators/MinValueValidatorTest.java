package br.com.igorkg.contract.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MinValueValidatorTest {

    private final MinValueValidator validator = new MinValueValidator();

    @MinValue(5)
    private int annotatedField;

    /// Lê a anotação real via reflexão, do mesmo jeito que
    /// [br.com.igorkg.contract.core.ContractInvocationHandler] faz em runtime,
    /// em vez de tentar instanciar `@MinValue` manualmente.
    private MinValue minValueOfFive() throws NoSuchFieldException {
        return getClass().getDeclaredField("annotatedField").getAnnotation(MinValue.class);
    }

    @Test
    void acceptsAValueAboveTheMinimum() throws NoSuchFieldException {
        assertDoesNotThrow(() -> validator.validate(minValueOfFive(), 10));
    }

    @Test
    void acceptsAValueEqualToTheMinimum() throws NoSuchFieldException {
        assertDoesNotThrow(() -> validator.validate(minValueOfFive(), 5));
    }

    @Test
    void rejectsAValueBelowTheMinimum() throws NoSuchFieldException {
        MinValue annotation = minValueOfFive();

        IllegalArgumentException violation =
                assertThrows(IllegalArgumentException.class, () -> validator.validate(annotation, 4));
        assertEquals("value must be >= 5 but was 4", violation.getMessage());
    }

    @Test
    void rejectsANullValue() throws NoSuchFieldException {
        MinValue annotation = minValueOfFive();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(annotation, null));
    }
}
