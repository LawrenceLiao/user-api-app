package co.zip.candidate.userapi.controller;


import co.zip.candidate.userapi.dto.client.ClientGetDto;
import co.zip.candidate.userapi.dto.client.ClientPostDto;
import co.zip.candidate.userapi.exception.DuplicateElementsException;
import co.zip.candidate.userapi.exception.UserNotFoundException;
import co.zip.candidate.userapi.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;



    @Test
    void shouldReturnClientGetDtoWhenSuccessfullyCreateAClient() throws Exception {
        ClientPostDto clientPostDto = ClientPostDto.builder()
                .clientName("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(7000))
                .build();

        ClientGetDto clientGetDto = ClientGetDto.builder()
                .id(1L)
                .clientName("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(7000))
                .build();
        String json = objectMapper.writeValueAsString(clientPostDto);

        when(clientService.createClient(clientPostDto)).thenReturn(clientGetDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/clients")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientName").value(clientGetDto.getClientName()))
                .andExpect(jsonPath("$.email").value(clientGetDto.getEmail()))
                .andExpect(jsonPath("$.monthlySalary").value(clientGetDto.getMonthlySalary()))
                .andExpect(jsonPath("$.monthlyExpenses").value(clientGetDto.getMonthlyExpenses()));
    }

    @Test
    void shouldReturnBadRequestResponseWithErrorDetailsWhenClientNameIsMissing() throws Exception {
        ClientPostDto clientPostDto = ClientPostDto.builder()
                .clientName(" ")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(7000))
                .build();

        String json = objectMapper.writeValueAsString(clientPostDto);


        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/clients")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("INVALID_ARGUMENT"))
                .andExpect(jsonPath("$.details").value("Client name is not presented"));

    }

    @Test
    void shouldReturnBadRequestResponseWithErrorDetailsWhenEmailIsInvalid() throws Exception {
        ClientPostDto clientPostDto = ClientPostDto.builder()
                .clientName("John Doe")
                .email("john.doe.test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(7000))
                .build();

        String json = objectMapper.writeValueAsString(clientPostDto);


        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/clients")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("INVALID_ARGUMENT"))
                .andExpect(jsonPath("$.details").value("Email address is invalid"));

    }


    @Test
    void shouldReturnBadRequestResponseWithErrorDetailsWhenMonthlySalaryIsNegative() throws Exception {
        ClientPostDto clientPostDto = ClientPostDto.builder()
                .clientName("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(-10000))
                .monthlyExpenses(BigDecimal.valueOf(7000))
                .build();

        String json = objectMapper.writeValueAsString(clientPostDto);


        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/clients")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("INVALID_ARGUMENT"))
                .andExpect(jsonPath("$.details").value("Monthly salary has to be positive"));

    }

    @Test
    void shouldReturnBadRequestResponseWithErrorDetailsWhenMonthlyExpensesIsNegative() throws Exception {
        ClientPostDto clientPostDto = ClientPostDto.builder()
                .clientName("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(-7000))
                .build();

        String json = objectMapper.writeValueAsString(clientPostDto);


        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/clients")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("INVALID_ARGUMENT"))
                .andExpect(jsonPath("$.details").value("Monthly expenses has to be positive"));
    }

    @Test
    void shouldReturnConflictResponseWithErrorDetailsWhenDuplicateElementsExceptionThrows() throws Exception {
        ClientPostDto clientPostDto = ClientPostDto.builder()
                .clientName("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(7000))
                .build();

        String json = objectMapper.writeValueAsString(clientPostDto);

        when(clientService.createClient(clientPostDto)).thenThrow(DuplicateElementsException.class);


        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/clients")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("DUPLICATE_ELEMENTS"));
    }

    @Test
    void shouldReturnClientGetDtoWhenClientIdGiven() throws Exception {
        Long clientId = 1L;
        ClientGetDto clientGetDto = ClientGetDto.builder()
                .id(clientId)
                .clientName("John Doe")
                .email("john.doe@test.com")
                .monthlySalary(BigDecimal.valueOf(10000))
                .monthlyExpenses(BigDecimal.valueOf(7000))
                .build();

        when(clientService.getClient(clientId)).thenReturn(clientGetDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/clients/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value(clientGetDto.getClientName()))
                .andExpect(jsonPath("$.email").value(clientGetDto.getEmail()))
                .andExpect(jsonPath("$.monthlySalary").value(clientGetDto.getMonthlySalary()))
                .andExpect(jsonPath("$.monthlyExpenses").value(clientGetDto.getMonthlyExpenses()));
    }

    @Test
    void shouldReturnNotFoundResponseWithErrorDetailsWhenNoClientWithIdGivenFound() throws Exception {
        Long clientId = 1L;

        when(clientService.getClient(clientId)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/clients/{clientId}", clientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("USER_NOT_FOUND"));
    }


    @Test
    void shouldReturnClientListWhenRetrievingAllClients() throws Exception {
        List<ClientGetDto> dtoList = List.of(
                ClientGetDto.builder()
                        .id(1L)
                        .clientName("John Doe")
                        .email("john.doe@test.com")
                        .monthlySalary(BigDecimal.valueOf(10000))
                        .monthlyExpenses(BigDecimal.valueOf(7000))
                        .build(),
                ClientGetDto.builder()
                        .id(2L)
                        .clientName("Elon Mush")
                        .email("elon.musk@test.com")
                        .monthlySalary(BigDecimal.valueOf(80000))
                        .monthlyExpenses(BigDecimal.valueOf(74500))
                        .build()
        );

        when(clientService.getAllClients()).thenReturn(dtoList);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientName").value(dtoList.get(0).getClientName()))
                .andExpect(jsonPath("$[0].email").value(dtoList.get(0).getEmail()))
                .andExpect(jsonPath("$[0].monthlySalary").value(dtoList.get(0).getMonthlySalary()))
                .andExpect(jsonPath("$[1].clientName").value(dtoList.get(1).getClientName()))
                .andExpect(jsonPath("$[1].email").value(dtoList.get(1).getEmail()))
                .andExpect(jsonPath("$[1].monthlyExpenses").value(dtoList.get(1).getMonthlyExpenses()));
    }



}
