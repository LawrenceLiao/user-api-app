package co.zip.candidate.userapi.service;

import co.zip.candidate.userapi.dto.client.ClientGetDto;
import co.zip.candidate.userapi.dto.client.ClientPostDto;
import co.zip.candidate.userapi.exception.DuplicateElementsException;
import co.zip.candidate.userapi.exception.UserNotFoundException;
import co.zip.candidate.userapi.mapper.ClientMapper;
import co.zip.candidate.userapi.model.Client;
import co.zip.candidate.userapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    @Transactional
    public ClientGetDto createClient(ClientPostDto clientPostDto) {
        validateEmailIfExists(clientPostDto.getEmail());

        Client client = clientMapper.toEntity(clientPostDto);
        clientRepository.save(client);
        return clientMapper.fromEntity(client);
    }

    public ClientGetDto getClient(Long id) {
        return clientMapper.fromEntity(retrieveClientById(id));
    }

    public List<ClientGetDto> getAllClients() {
        return clientRepository.findByDeletedIsFalse().stream()
                .map(clientMapper::fromEntity)
                .collect(Collectors.toList());
    }

    private void validateEmailIfExists(String email) {
        if (clientRepository.existsByEmailAndDeletedIsFalse(email)) {
            log.info("Duplicate email encountered which is {}", email);
            throw new DuplicateElementsException("The email has been used by another client");
        }
    }

    public Client retrieveClientById(Long id) {
        return clientRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> {
                    log.info("No active client with id {} found", id);
                    return new UserNotFoundException("No active client with ID given exists");
                });
    }
}
