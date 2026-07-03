package br.com.igorkg.contract.core;

import br.com.igorkg.contract.validators.MinValue;

/// Implementação propositalmente quebrada: subtrai em vez de somar, então
/// qualquer depósito válido (que passa pela pré-condição) deixa o saldo
/// negativo e viola o invariante `@MinValue(0)`.
final class BrokenInvariantAccountImpl implements Account {

    @MinValue(0)
    private int balance;

    @Override
    public void deposit(Integer amount) {
        balance -= amount;
    }

    @Override
    public int getBalance() {
        return balance;
    }
}
