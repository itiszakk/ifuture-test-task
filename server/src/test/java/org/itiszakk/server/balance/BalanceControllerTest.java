package org.itiszakk.server.balance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.itiszakk.server.exception.BalanceNotFoundException;
import org.itiszakk.shared.balance.BalanceDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BalanceController.class)
class BalanceControllerTest {

    @MockBean
    private BalanceService service;

    @Autowired
    private MockMvc mockMvc;

    private final static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getBalance_ExistentId_Status200AndReturnsAmount() throws Exception {
        // given
        when(service.getBalance(1L)).thenReturn(Optional.of(100L));

        // when
        ResultActions response = mockMvc.perform(
                get("/balance/{id}", 1L));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(100L));
    }

    @Test
    public void getBalance_NonExistentId_Status404AndThrowsException() throws Exception {
        // given
        when(service.getBalance(1L)).thenReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(
                get("/balance/{id}", 1L));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertThat(result.andReturn().getResolvedException()).isInstanceOf(BalanceNotFoundException.class);
    }

    @Test
    public void changeBalance_NonNullDtoWithExistentIdAndNonNullAmount_Status200() throws Exception {
        // given
        BalanceDTO dto = new BalanceDTO(1L, 100L);

        // when
        ResultActions result = mockMvc.perform(patch("/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    public void changeBalance_NullDto_Status400() throws Exception {
        // given
        BalanceDTO dto = null;

        // when
        ResultActions result = mockMvc.perform(patch("/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void changeBalance_NonNullDtoWithNullFields_Status400() throws Exception {
        // given
        BalanceDTO dto = new BalanceDTO(null, null);

        // when
        ResultActions result = mockMvc.perform(patch("/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)));

        // then
        result.andExpect(status().isBadRequest());
    }
}