package br.com.igorkg.exemplo;

/// Uma implementação limpa de [WalletService] que se preocupa apenas com a
/// lógica de negócio. Não possui nenhuma verificação defensiva: o contrato é
/// garantido pelo proxy do framework, então a implementação pode confiar
/// nos dados que recebe.
public final class WalletServiceImpl implements WalletService {

    private int balance;

    @Override
    public void deposit(Integer amount) {
        balance += amount;
        System.out.println("  [WalletServiceImpl] deposited " + amount + " -> balance = " + balance);
    }
}
