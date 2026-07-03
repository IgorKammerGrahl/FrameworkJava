package br.com.igorkg.contract.core;

import br.com.igorkg.contract.validators.MinValue;

/// Implementação correta de [Account]: o saldo é um invariante que nunca
/// fica negativo enquanto os depósitos respeitarem a pré-condição.
final class AccountImpl implements Account {

    @MinValue(0)
    private int balance;

    @Override
    public void deposit(Integer amount) {
        balance += amount;
    }

    @Override
    public int getBalance() {
        return balance;
    }
}
