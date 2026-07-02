package br.com.igorkg.contract.validators;

import java.lang.annotation.Annotation;

/// Estratégia que valida uma única cláusula de contrato expressa por uma anotação.
///
/// Cada implementação sabe verificar um tipo de anotação `A` contra um valor
/// do tipo `T` (um argumento de método, um valor de retorno ou um campo).
///
/// Um validador é agnóstico quanto à fase: ele não sabe se o valor é uma
/// pré-condição, uma pós-condição ou um invariante. Por isso, ele sinaliza
/// falha lançando uma [IllegalArgumentException] descrevendo *por que* o
/// valor é inválido, e o engine traduz isso na exceção
/// `br.com.igorkg.contract.exceptions.ContractViolationException` correspondente
/// à fase atual. Em caso de sucesso, o método simplesmente retorna normalmente.
///
/// @param <A> a anotação que declara a cláusula do contrato
/// @param <T> o tipo de valor que este validador consegue verificar
@FunctionalInterface
public interface ContractValidator<A extends Annotation, T> {

    void validate(A annotation, T value);
}
