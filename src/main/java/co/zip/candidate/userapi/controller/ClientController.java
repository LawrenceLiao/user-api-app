package co.zip.candidate.userapi.controller;

import co.zip.candidate.userapi.dto.client.ClientGetDto;
import co.zip.candidate.userapi.dto.client.ClientPostDto;
import co.zip.candidate.userapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/clients")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientGetDto createClient(@Valid @RequestBody ClientPostDto clientPostDto) {
        return clientService.createClient(clientPostDto);
    }

    @GetMapping("/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientGetDto getClient(@PathVariable("clientId") @NotNull Long clientId) {
        return clientService.getClient(clientId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClientGetDto> getAllClients() {
        return clientService.getAllClients();
    }
}
