package br.com.igorkg.contract.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Cláusula de contrato que exige que o valor anotado não seja nulo.
///
/// Aplicável a um parâmetro (pré-condição), ao retorno de um método
/// (pós-condição) ou a um campo (invariante). Mantida em tempo de execução
/// (RUNTIME) para que o engine baseado em reflexão consiga lê-la.
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
public @interface NotNull {
}
