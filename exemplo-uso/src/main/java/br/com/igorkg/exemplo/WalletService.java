package br.com.igorkg.exemplo;

import br.com.igorkg.contract.validators.MinValue;
import br.com.igorkg.contract.validators.NotNull;

/// Contrato público de uma carteira. O valor de `deposit` é limitado por
/// duas pré-condições declaradas diretamente no parâmetro: ele deve estar
/// presente (`@NotNull`) e valer pelo menos uma unidade (`@MinValue(1)`).
public interface WalletService {

    void deposit(@NotNull @MinValue(1) Integer amount);
}
