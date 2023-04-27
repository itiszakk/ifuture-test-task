package org.itiszakk.server.balance;

import java.util.Optional;

public interface BalanceService {

    Optional<Long> getBalance(Long id);
    void changeBalance(Long id, Long amount);
}
