package br.com.igorkg.contract.core;

import br.com.igorkg.contract.validators.MinValue;
import br.com.igorkg.contract.validators.MinValueValidator;
import br.com.igorkg.contract.validators.NotNull;
import br.com.igorkg.contract.validators.NotNullValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorRegistryTest {

    @Test
    void unregisteredAnnotationIsNotRegistered() {
        ValidatorRegistry registry = new ValidatorRegistry();

        assertFalse(registry.isRegistered(NotNull.class));
    }

    @Test
    void registeredAnnotationIsRegistered() {
        ValidatorRegistry registry = new ValidatorRegistry();

        registry.register(NotNull.class, new NotNullValidator());

        assertTrue(registry.isRegistered(NotNull.class));
    }

    @Test
    void getValidatorReturnsTheRegisteredInstance() {
        ValidatorRegistry registry = new ValidatorRegistry();
        MinValueValidator validator = new MinValueValidator();

        registry.register(MinValue.class, validator);

        assertSame(validator, registry.getValidator(MinValue.class));
    }

    @Test
    void getValidatorThrowsWhenNothingIsRegistered() {
        ValidatorRegistry registry = new ValidatorRegistry();

        assertThrows(IllegalArgumentException.class, () -> registry.getValidator(NotNull.class));
    }
}
