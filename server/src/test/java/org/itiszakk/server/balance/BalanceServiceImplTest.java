package org.itiszakk.server.balance;

import org.itiszakk.server.exception.BalanceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceImplTest {

    @Mock
    private BalanceRepository repository;

    @InjectMocks
    private BalanceServiceImpl service;

    @Test
    public void getBalance_ExistentId_ReturnsPresentOptionalAmount() {
        // given
        Balance entity = new Balance(1L, 100L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        // when
        Optional<Long> amount = service.getBalance(1L);

        // then
        assertThat(amount).hasValue(100L);
    }

    @Test
    public void getBalance_NonExistentId_Returns_EmptyOptional() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when
        Optional<Long> amount = service.getBalance(1L);

        // then
        assertThat(amount).isEmpty();
    }

    @Test
    public void changeBalance_ExistentIdAndNonNullAmount_UpdatesBalance() {
        // given
        Balance entity = new Balance(1L, 100L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        ArgumentCaptor<Balance> balanceCaptor = ArgumentCaptor.forClass(Balance.class);

        when(repository.save(balanceCaptor.capture())).thenAnswer(i -> i.getArgument(0));

        // when
        service.changeBalance(1L, 300L);

        // then
        assertThat(balanceCaptor.getValue().getAmount()).isEqualTo(400L);
    }

    @Test
    public void changeBalance_NonExistentId_ThrowsException() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when
        Throwable throwable = catchThrowable(() -> service.changeBalance(1L, 100L));

        // then
        assertThat(throwable).isInstanceOf(BalanceNotFoundException.class);
    }
}