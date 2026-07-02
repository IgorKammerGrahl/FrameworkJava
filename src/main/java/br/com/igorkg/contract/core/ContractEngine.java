package br.com.igorkg.contract.core;

import br.com.igorkg.contract.validators.ContractValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;

/// Ponto de entrada e builder fluente do framework.
///
/// Possui (composição) o [ValidatorRegistry], permite que quem chama
/// registre validadores através de uma API encadeável e devolve proxies
/// protegidos pelo contrato:
///
/// ```
/// Account safe = ContractEngine.setup()
///         .addValidator(NotNull.class, new NotNullValidator())
///         .addValidator(MinValue.class, new MinValueValidator())
///         .protect(Account.class, new AccountImpl());
/// ```
public final class ContractEngine {

    private final ValidatorRegistry registry;

    private ContractEngine() {
        this.registry = new ValidatorRegistry();
    }

    /// Inicia a configuração fluente com um registro vazio.
    public static ContractEngine setup() {
        return new ContractEngine();
    }

    /// Registra um validador para uma anotação e retorna `this`, permitindo
    /// encadear as chamadas.
    public <A extends Annotation> ContractEngine addValidator(Class<A> annotationType,
                                                              ContractValidator<A, ?> validator) {
        registry.register(annotationType, validator);
        return this;
    }

    /// Envolve `implementation` em um proxy dinâmico que garante o contrato
    /// declarado em `interfaceType`.
    ///
    /// @throws IllegalArgumentException se `interfaceType` não for uma
    ///         interface, requisito do próprio mecanismo de proxy do JDK.
    @SuppressWarnings("unchecked")
    public <T> T protect(Class<T> interfaceType, T implementation) {
        if (!interfaceType.isInterface()) {
            throw new IllegalArgumentException(
                    interfaceType.getName() + " must be an interface to be protected");
        }
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new ContractInvocationHandler(implementation, registry));
    }
}
