package br.com.igorkg.exemplo;

import br.com.igorkg.contract.core.ContractEngine;
import br.com.igorkg.contract.exceptions.ContractViolationException;
import br.com.igorkg.contract.validators.MinValue;
import br.com.igorkg.contract.validators.MinValueValidator;
import br.com.igorkg.contract.validators.NotNull;
import br.com.igorkg.contract.validators.NotNullValidator;

/// Demonstração de que o framework `contract-framework` funciona como
/// dependência externa: este projeto não contém nenhuma classe do framework,
/// apenas a declara em `build.gradle` e a consome normalmente através dos
/// pacotes `br.com.igorkg.contract.*`.
///
/// Registra os validadores em um [ContractEngine], protege uma implementação
/// simples ([WalletServiceImpl]) atrás do contrato declarado em [WalletService]
/// e então executa uma chamada válida e duas chamadas que quebram o contrato,
/// para evidenciar o comportamento fail-fast: o proxy rejeita a entrada inválida
/// antes mesmo da implementação ser executada.
public final class App {

    public static void main(String[] args) {
        WalletService wallet = ContractEngine.setup()
                .addValidator(NotNull.class, new NotNullValidator())
                .addValidator(MinValue.class, new MinValueValidator())
                .protect(WalletService.class, new WalletServiceImpl());

        System.out.println("=== Design-by-Contract demo (via contract-framework:1.0.0) ===");

        System.out.println("\n1) Valid call: deposit(100)");
        wallet.deposit(100);

        System.out.println("\n2) Invalid call: deposit(null)");
        attempt(() -> wallet.deposit(null));

        System.out.println("\n3) Invalid call: deposit(0)");
        attempt(() -> wallet.deposit(0));
    }

    /// Executa uma chamada que deve quebrar o contrato e imprime a violação
    /// lançada pelo framework. Capturar a classe base selada [ContractViolationException]
    /// comprova que a exceção veio do framework, independentemente da fase.
    private static void attempt(Runnable call) {
        try {
            call.run();
            System.out.println("  FAIL: expected a contract violation, but none was thrown");
        } catch (ContractViolationException e) {
            System.out.println("  Caught " + e.getClass().getSimpleName() + " -> " + e.getMessage());
        }
    }
}
