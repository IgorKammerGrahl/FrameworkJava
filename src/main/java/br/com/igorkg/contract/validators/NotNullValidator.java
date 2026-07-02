package br.com.igorkg.contract.validators;

/// Aplica a regra de [NotNull]: rejeita um valor nulo.
public final class NotNullValidator implements ContractValidator<NotNull, Object> {

    @Override
    public void validate(NotNull annotation, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
    }
}
