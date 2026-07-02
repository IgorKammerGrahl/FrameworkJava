package br.com.igorkg.contract.validators;

/// Aplica a regra de [MinValue]: rejeita um inteiro abaixo do limite
/// declarado na anotação.
public final class MinValueValidator implements ContractValidator<MinValue, Integer> {

    @Override
    public void validate(MinValue annotation, Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null to satisfy @MinValue");
        }
        int minimum = annotation.value();
        if (value < minimum) {
            throw new IllegalArgumentException(
                    "value must be >= " + minimum + " but was " + value);
        }
    }
}
