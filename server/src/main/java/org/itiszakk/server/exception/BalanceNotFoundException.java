package org.itiszakk.server.exception;

import java.text.MessageFormat;

public class BalanceNotFoundException extends RuntimeException {

    public BalanceNotFoundException(Long id) {
        super(MessageFormat.format("Balance with id={0} not found", id));
    }
}
