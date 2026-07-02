package br.com.igorkg.contract.core;

import br.com.igorkg.contract.validators.ContractValidator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/// Agrega os validadores conhecidos pelo framework, indexados pela anotação
/// que cada um trata.
///
/// Sua única responsabilidade é gerenciar essas dependências: armazenar e
/// recuperar validadores, nada além disso. Não realiza nenhuma verificação
/// de contrato e não possui lógica de proxy. Os validadores que guarda são
/// criados em outro lugar e apenas *agregados* aqui, então o ciclo de vida
/// deles é independente do ciclo de vida do registro.
public final class ValidatorRegistry {

    private final Map<Class<? extends Annotation>, ContractValidator<?, ?>> validators = new HashMap<>();

    /// Associa um tipo de anotação ao validador responsável por ela.
    ///
    /// A assinatura amarra o parâmetro de tipo `A` da anotação aos dois
    /// argumentos, de modo que o compilador rejeita registros incompatíveis
    /// (por exemplo, um validador de `@MinValue` cadastrado sob `@NotNull`).
    public <A extends Annotation> void register(Class<A> annotationType, ContractValidator<A, ?> validator) {
        validators.put(annotationType, validator);
    }

    /// Informa se uma anotação possui um validador registrado, permitindo que
    /// quem chama ignore anotações que não fazem parte do contrato sem
    /// depender de exceções para controle de fluxo.
    public boolean isRegistered(Class<? extends Annotation> annotationType) {
        return validators.containsKey(annotationType);
    }

    /// Retorna o validador registrado para a anotação informada.
    ///
    /// @throws IllegalArgumentException se nenhum validador foi registrado
    ///         para ela, garantindo assim que quem chama sempre recebe um
    ///         resultado não nulo.
    public ContractValidator<?, ?> getValidator(Class<? extends Annotation> annotationType) {
        ContractValidator<?, ?> validator = validators.get(annotationType);
        if (validator == null) {
            throw new IllegalArgumentException(
                    "No ContractValidator registered for @" + annotationType.getSimpleName());
        }
        return validator;
    }
}
