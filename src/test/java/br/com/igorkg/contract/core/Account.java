package br.com.igorkg.contract.core;

import br.com.igorkg.contract.validators.MinValue;
import br.com.igorkg.contract.validators.NotNull;

/// Contrato de teste usado para exercitar o proxy do framework: uma
/// pré-condição em `deposit`, uma pós-condição em `getBalance`.
interface Account {

    void deposit(@NotNull @MinValue(1) Integer amount);

    @MinValue(0)
    int getBalance();
}
