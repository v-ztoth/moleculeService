package com.zoltan.toth.molecule.controller;

import com.zoltan.toth.molecule.exceptions.CalculateApiException;
import com.zoltan.toth.molecule.service.InfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MoleculeInfoController.class)
public class MoleculeInfoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InfoService infoService;

    @Test
    void shouldReturn200WhenRequestParamValid() throws Exception {
        mockMvc.perform(get("/info/basic?molecule=aspirin")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenRequestParamInValid() throws Exception {
        mockMvc.perform(get("/info/basic")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenCalculateApiExceptionOccurs() throws Exception {
        when(infoService.getBasicInfo(ArgumentMatchers.anyString())).thenThrow(new CalculateApiException("ex"));

        mockMvc.perform(get("/info/basic?molecule=aspirin")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

}
