package br.com.igorkg.contract.core;

/// Implementação propositalmente quebrada: `getBalance` sempre devolve um
/// valor negativo, violando a pós-condição `@MinValue(0)` de [Account]
/// independentemente do que foi depositado.
final class BrokenPostConditionAccountImpl implements Account {

    private int balance;

    @Override
    public void deposit(Integer amount) {
        balance += amount;
    }

    @Override
    public int getBalance() {
        return -1;
    }
}
