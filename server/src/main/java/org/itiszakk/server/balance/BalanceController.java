package org.itiszakk.server.balance;

import org.itiszakk.shared.balance.BalanceDTO;
import org.itiszakk.server.exception.BalanceDTOException;
import org.itiszakk.server.exception.BalanceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/balance")
public class BalanceController {

    private final BalanceService service;

    public BalanceController(BalanceService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Long getBalance(@PathVariable Long id) {
        return service.getBalance(id)
                .orElseThrow(() -> new BalanceNotFoundException(id));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void changeBalance(@RequestBody BalanceDTO balance) {
        verifyBalanceDTO(balance);
        service.changeBalance(balance.id(), balance.amount());
    }

    private void verifyBalanceDTO(BalanceDTO balance) {
        if (balance.id() == null) {
            throw new BalanceDTOException("Parameter 'id' is missing");
        }

        if (balance.amount() == null) {
            throw new BalanceDTOException("Parameter 'amount' is missing");
        }
    }
}
