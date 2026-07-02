package br.com.igorkg.contract.core;

import br.com.igorkg.contract.exceptions.ContractViolationException;
import br.com.igorkg.contract.exceptions.InvariantViolationException;
import br.com.igorkg.contract.exceptions.PostConditionException;
import br.com.igorkg.contract.exceptions.PreConditionException;
import br.com.igorkg.contract.validators.ContractValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/// Coração reflexivo do framework: um handler de proxy dinâmico que envolve
/// um objeto alvo e garante seu contrato em torno de cada chamada de método.
///
/// O fluxo de `invoke` reflete o próprio ciclo de vida do contrato — verifica
/// as pré-condições, executa o método real, verifica a pós-condição e, por
/// fim, verifica os invariantes. Cada fase delega a verificação em si aos
/// [ContractValidator]s registrados e, em caso de falha, lança a exceção de
/// violação correspondente àquela fase.
public final class ContractInvocationHandler implements InvocationHandler {

    private final Object target;
    private final ValidatorRegistry registry;

    public ContractInvocationHandler(Object target, ValidatorRegistry registry) {
        this.target = target;
        this.registry = registry;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        validateParameters(method, args);
        Object result = invokeTarget(method, args);
        validateReturn(method, result);
        validateInvariants();
        return result;
    }

    private void validateParameters(Method method, Object[] args) {
        if (args == null) {
            return;
        }
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            check(parameters[i].getAnnotations(), args[i], PreConditionException::new);
        }
    }

    private void validateReturn(Method method, Object result) {
        check(method.getAnnotations(), result, PostConditionException::new);
    }

    private void validateInvariants() {
        for (Field field : target.getClass().getDeclaredFields()) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations.length == 0) {
                continue;
            }
            check(annotations, readFieldValue(field), InvariantViolationException::new);
        }
    }

    /// Executa todo validador registrado que se aplica a `value`, convertendo
    /// qualquer falha na exceção específica da fase produzida por `onFailure`.
    private void check(Annotation[] annotations, Object value, ViolationFactory onFailure) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (registry.isRegistered(annotationType)) {
                runValidator(registry.getValidator(annotationType), annotation, value, onFailure);
            }
        }
    }

    /// O par anotação/valor já foi casado com seu validador no momento do
    /// registro, então a chamada bruta (raw) é segura em tempo de execução.
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void runValidator(ContractValidator<?, ?> validator, Annotation annotation,
                              Object value, ViolationFactory onFailure) {
        try {
            ((ContractValidator) validator).validate(annotation, value);
        } catch (IllegalArgumentException failure) {
            throw onFailure.from(failure.getMessage(), failure);
        }
    }

    private Object invokeTarget(Method method, Object[] args) throws Throwable {
        try {
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private Object readFieldValue(Field field) {
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                    "Unable to read field '" + field.getName() + "' for invariant validation", e);
        }
    }

    /// Constrói a [ContractViolationException] de uma dada fase a partir de
    /// uma falha reportada por um validador. Implementada pelos construtores
    /// das exceções de violação (por exemplo, `PreConditionException::new`).
    @FunctionalInterface
    private interface ViolationFactory {
        ContractViolationException from(String message, Throwable cause);
    }
}
