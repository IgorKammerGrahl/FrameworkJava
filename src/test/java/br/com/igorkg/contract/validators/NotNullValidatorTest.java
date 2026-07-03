package br.com.igorkg.contract.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotNullValidatorTest {

    private final NotNullValidator validator = new NotNullValidator();

    @Test
    void acceptsANonNullValue() {
        assertDoesNotThrow(() -> validator.validate(null, "qualquer valor"));
    }

    @Test
    void rejectsANullValue() {
        IllegalArgumentException violation =
                assertThrows(IllegalArgumentException.class, () -> validator.validate(null, null));
        assertEquals("value must not be null", violation.getMessage());
    }
}
